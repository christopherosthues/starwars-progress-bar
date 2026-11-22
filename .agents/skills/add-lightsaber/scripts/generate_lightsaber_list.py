#!/usr/bin/env python3
"""Generate LIST_OF_LIGHTSABERS.md from factions.json and the fallback translation bundle.

The dataclasses below mirror the plugin's deserialization model so this script stays in
sync with how the plugin parses the same file:

    models/StarWarsFactions.kt  -> StarWarsFactions (only ``lightsabers`` is needed here)
    models/StarWarsFaction.kt   -> StarWarsFaction(id, data)
    models/Lightsabers.kt       -> Lightsabers(id, velocity, isJarKai, hilts)
    models/Lightsaber.kt        -> Hilt (only ``id`` is needed here; blades are not listed yet)

Display names are resolved from the fallback bundle (src/main/resources/messages/StarWarsBundle.properties):

    lightsabers.faction.<factionId>  -> "### <name>" section heading
    lightsabers.<factionId>.<id>     -> lightsaber display name

Each entity lists its hilts (variants) as images; blade details are intentionally omitted.

Usage (paths are derived from this file's location, so it can be run from anywhere):

    python .agents/skills/add-lightsaber/scripts/generate_lightsaber_list.py
"""

from __future__ import annotations

import json
import re
import sys
from dataclasses import dataclass, field
from pathlib import Path

START_MARKER = "<!-- Included lightsabers -->"
END_MARKER = "<!-- Included lightsabers end -->"

_UNICODE_ESCAPE = re.compile(r"\\u([0-9a-fA-F]{4})")


@dataclass(frozen=True)
class Hilt:
    """Mirrors models/Lightsaber.kt (only the hilt id is needed for the list)."""

    id: int


@dataclass(frozen=True)
class Lightsabers:
    """Mirrors models/Lightsabers.kt."""

    id: str
    velocity: float
    is_jar_kai: bool
    hilts: list[Hilt] = field(default_factory=list)

    @classmethod
    def from_json(cls, raw: dict) -> "Lightsabers":
        return cls(
            id=raw["id"],
            velocity=float(raw["velocity"]),
            is_jar_kai=bool(raw.get("isJarKai", False)),
            hilts=[Hilt(id=h["id"]) for h in raw.get("lightsabers", [])],
        )


@dataclass(frozen=True)
class StarWarsFaction:
    """Mirrors models/StarWarsFaction.kt."""

    id: str
    data: list[Lightsabers] = field(default_factory=list)

    @classmethod
    def from_json(cls, raw: dict) -> "StarWarsFaction":
        return cls(id=raw["id"], data=[Lightsabers.from_json(e) for e in raw.get("data", [])])


@dataclass(frozen=True)
class StarWarsFactions:
    """Mirrors models/StarWarsFactions.kt (vehicles intentionally omitted)."""

    lightsabers: list[StarWarsFaction] = field(default_factory=list)

    @classmethod
    def from_json(cls, raw: dict) -> "StarWarsFactions":
        return cls(lightsabers=[StarWarsFaction.from_json(f) for f in raw.get("lightsabers", [])])


def find_repo_root() -> Path:
    """Walk up from this script to the directory containing src/main/resources/json/factions.json."""
    here = Path(__file__).resolve()
    marker = Path("src") / "main" / "resources" / "json" / "factions.json"
    for parent in [here.parent, *here.parents]:
        if (parent / marker).is_file():
            return parent
    sys.exit(f"error: could not locate the repository root from {here}")


def message_format_unquote(pattern: str) -> str:
    """Apply MessageFormat quote handling (see StarWarsBundle.message()).

    Every bundle value is passed through ``MessageFormat.format`` at runtime, where a lone
    ``'`` toggles quoting mode and ``''`` produces a literal apostrophe. Lightsaber names are
    plain text (no ``{n}`` arguments), so only the quote rules matter here.
    """
    result: list[str] = []
    i, length = 0, len(pattern)
    while i < length:
        char = pattern[i]
        if char != "'":
            result.append(char)
            i += 1
            continue
        # A lone quote toggles quoting mode and is consumed; '' yields one literal '.
        if i + 1 < length and pattern[i + 1] == "'":
            result.append("'")
            i += 2
        else:
            i += 1
    return "".join(result)


def unescape(value: str) -> str:
    """Undo Java properties \\uXXXX escaping and MessageFormat quote handling."""
    value = _UNICODE_ESCAPE.sub(lambda m: chr(int(m.group(1), 16)), value)
    return message_format_unquote(value)


def load_fallback_bundle(path: Path) -> dict[str, str]:
    """Parse a Java .properties file (key=value lines; '#' and '!' start comments)."""
    values: dict[str, str] = {}
    for line in path.read_text(encoding="utf-8").splitlines():
        stripped = line.strip()
        if not stripped or stripped.startswith(("#", "!")):
            continue
        key, sep, value = stripped.partition("=")
        if sep:
            values[key.strip()] = unescape(value.strip())
    return values


def build_markdown(factions: StarWarsFactions, names: dict[str, str]) -> tuple[str, list[str], int]:
    """Return (markdown text, missing bundle keys, lightsaber count)."""
    missing: list[str] = []
    lines = [START_MARKER, "## Included lightsabers"]
    lightsaber_count = 0

    for faction in factions.lightsabers:
        if not faction.id:
            continue  # placeholder faction (see StarWarsFactionHolder.updateFactions())
        entries: list[tuple[str, Lightsabers]] = []
        for entity in faction.data:
            name = names.get(f"lightsabers.{faction.id}.{entity.id}")
            if name is None:
                missing.append(f"lightsabers.{faction.id}.{entity.id}")
                continue
            entries.append((name, entity))
        if not entries:
            continue  # no lightsabers -> no section (see add-faction skill)
        heading = names.get(f"lightsabers.faction.{faction.id}")
        if heading is None:
            missing.append(f"lightsabers.faction.{faction.id}")
            continue

        lines += ["", f"### {heading}", ""]
        for name, entity in sorted(entries, key=lambda e: (e[0].lower(), e[1].id)):
            lines.append(f"* **{name}**")
            for hilt in sorted(entity.hilts, key=lambda h: h.id):
                icon = f"icons/lightsabers/{faction.id}/{entity.id}_{hilt.id}"
                lines.append(f"  * ![{name}]({icon}@2x.png)")
            lightsaber_count += 1

    lines += ["", END_MARKER]
    return "\n".join(lines) + "\n", missing, lightsaber_count


def main() -> int:
    root = find_repo_root()
    factions_path = root / "src" / "main" / "resources" / "json" / "factions.json"
    bundle_path = root / "src" / "main" / "resources" / "messages" / "StarWarsBundle.properties"
    output_path = root / "LIST_OF_LIGHTSABERS.md"

    try:
        factions = StarWarsFactions.from_json(json.loads(factions_path.read_text(encoding="utf-8")))
    except (OSError, ValueError, KeyError) as error:
        print(f"error: failed to parse {factions_path}: {error}", file=sys.stderr)
        return 1

    names = load_fallback_bundle(bundle_path)
    text, missing, lightsaber_count = build_markdown(factions, names)

    if missing:
        print("error: missing keys in StarWarsBundle.properties:", file=sys.stderr)
        for key in missing:
            print(f"  {key}", file=sys.stderr)
        return 1

    try:
        current = output_path.read_text(encoding="utf-8")
    except FileNotFoundError:
        current = None
    if current == text:
        print(f"{output_path.name} is up to date ({lightsaber_count} lightsabers).")
        return 0

    with output_path.open("w", encoding="utf-8", newline="\n") as handle:
        handle.write(text)
    print(f"Wrote {output_path.name} ({lightsaber_count} lightsabers).")
    return 0


if __name__ == "__main__":
    sys.exit(main())
