---
name: Add Lightsaber
description: Adds a new lightsaber to the Star Wars Progress Bar plugin. Use when adding, renaming, or removing a lightsaber entity — requires updating factions.json (including jar-kai blade definitions), the LFS icon set in two locations, and all translation bundles together, enforced by IconResourceTests and StarWarsResourceBundleTests.
---

# Add a lightsaber

All pieces must be added **together** — CI tests fail if any part is missing:

| Piece | Location | Enforced by |
|---|---|---|
| Entity definition | `src/main/resources/json/factions.json` (single source of truth) | JSON parsing at startup/tests |
| Runtime icons | `src/main/resources/icons/lightsabers/<faction>/` | `IconResourceTests` + runtime loader |
| @2x icons | **repo root** `icons/lightsabers/<faction>/` (NOT under `src/`) | `IconResourceTests` |
| Translations | every bundle file (`src/main/resources/messages/StarWarsBundle*.properties`) | `StarWarsResourceBundleTests` |
| Documentation | `LIST_OF_LIGHTSABERS.md` (generated) | generated from factions.json + fallback bundle — see section 5 |

Lightsabers do not appear in `LIST_OF_VEHICLES.md`; they have their own generated list. If the faction does not exist yet, run the **add-faction** skill first.

## 1. Naming rules

- Entity `id`: lowercase snake_case, **no spaces or dashes** — underscores only (e.g. `darth_maul_first`, `asajj_ventress_second`).
- Faction folder name must exactly match the faction `id` in `factions.json`.
- Display names may contain any characters; escape apostrophes as `''` in bundle values.

## 2. factions.json entry

Add to the appropriate faction's `data` array under `"lightsabers"`:

```json
{
    "type": "Lightsabers",
    "id": "my_lightsaber_id",
    "velocity": 3,
    "isJarKai": false,
    "lightsabers": [
        {
            "id": 1,
            "isDoubleBladed": false,
            "yShift": 0,
            "blades": [
                {
                    "bladeColor": "red",
                    "isShoto": false,
                    "bladeSize": 8,
                    "xBlade": 0,
                    "yBlade": 1
                }
            ]
        }
    ]
}
```

- Every hilt in `lightsabers[]` gets its own icon files (see below); `isJarKai: true` marks entities with multiple hilts. Copy an existing entry of the same kind as a template.
- Each blade's `bladeColor` must match one of the keys in the `colors` map defined by `LightsaberColor` (`src/main/kotlin/com/christopherosthues/starwarsprogressbar/ui/LightsaberColor.kt`) — unknown values silently fall back to cyan.

## 3. Icons (Git LFS PNGs)

All PNGs are Git LFS-tracked (`*.png filter=lfs`). Never commit them as regular binaries; verify with `git lfs ls-files` before pushing.

Unlike vehicles, lightsabers have **no `_r` variant**. Most lightsaber icons are **10 px** tall (some go up to **20 px**); the width is variable and depends on the hilt itself. Make the @2x version exactly double size.

Every hilt has its own icon pair, where `<bladeId>` is each `lightsabers[].id`:

| File | Notes |
|---|---|
| `src/main/resources/icons/lightsabers/<faction>/<id>_<bladeId>.png` | Base icon for that blade; loaded at runtime |
| `icons/lightsabers/<faction>/<id>_<bladeId>@2x.png` | Double-size version, repo root |

Non-jar-kai entities have a single hilt entry (`<id>_1.png`); jar-kai entities have one pair per hilt.

- `IconResourceTests` enforces exactly these paths in both locations.

## 4. Translations — every bundle file

Add the same key to **every** bundle file under `src/main/resources/messages/`: the fallback `StarWarsBundle.properties` plus one per language defined in `Language` (`configuration/Language.kt`) — files are named `StarWarsBundle_<languageCode>.properties`.

```properties
lightsabers.<factionId>.<id>=Display Name
```

- Key format is `<prefix>.<factionId>.<id>` — the faction id prefix is required (it comes from `entityId`, not just `id`).
- Escape apostrophes as `''` (e.g. `Darth Maul''s first lightsaber`).
- Tests enforce: no empty values, fallback contains every key of the language bundles, and each language bundle contains every fallback key except `name`.

## 5. LIST_OF_LIGHTSABERS.md — generate it

Run from the repo root:

```powershell
python .agents/skills/add-lightsaber/scripts/generate_lightsaber_list.py
```

The script reads `factions.json` (faction order + hilt ids) and the fallback bundle (`StarWarsBundle.properties`) for display names, then rewrites the file between the `<!-- Included lightsabers -->` / `<!-- Included lightsabers end -->` markers. Each entry looks like:

```markdown
* **Display Name**
  * ![Display Name](icons/lightsabers/<faction>/<id>_<hiltId>@2x.png)
```

- Sections follow the faction order in `factions.json`; entities are sorted case-insensitively by display name within each section, and hilts (variants) are listed in hilt id order. Blade details are not included yet.
- Image paths are relative to the repo root (they point at the @2x icons).
- Display names match what the runtime shows: apostrophes appear as a single `'` even though they are stored doubled in the bundles.
- The script fails with a clear error if a lightsaber or faction is missing its bundle key — add the translations first (section 4), then re-run.

## 6. Verify

Run from the repo root:

```powershell
.\gradlew.bat test
```

Relevant gates: `IconResourceTests` (icon existence **and** no orphan PNGs — any PNG on disk that is not referenced by a factions.json entry fails CI unless whitelisted in the test) and `StarWarsResourceBundleTests` (translation parity). Then run `.\gradlew.bat check` for the full CI gate.

Also confirm `git diff LIST_OF_LIGHTSABERS.md` shows only the expected change — the generated file should not drift from what the script produces.

## Renaming / removing an entity

- Rename: update the id everywhere above **and** rename all icon files (including per-blade icons); orphaned PNGs fail `IconResourceTests` unless added to its `iconWhitelist`.
- Remove: delete the factions.json entry, all icon pairs, and the bundle keys from all files.
- After renaming or removing, re-run the generator (section 5) so `LIST_OF_LIGHTSABERS.md` stays in sync.
