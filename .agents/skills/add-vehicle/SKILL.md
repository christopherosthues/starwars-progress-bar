---
name: Add Vehicle
description: Adds a new vehicle to the Star Wars Progress Bar plugin. Use when adding, renaming, or removing a vehicle entity — requires updating factions.json, all translation bundles, LIST_OF_VEHICLES.md, and CHANGELOG.md together; icon PNGs are provided separately and must never be created or edited by this skill. Enforced by IconResourceTests and StarWarsResourceBundleTests.
---

# Add a vehicle

All pieces must be added **together** — CI tests fail if any part is missing:

| Piece | Location | Enforced by |
|---|---|---|
| Entity definition | `src/main/resources/json/factions.json` (single source of truth) | JSON parsing at startup/tests |
| 32x32 runtime icons | `src/main/resources/icons/vehicles/<faction>/` | `IconResourceTests` + runtime loader |
| 64x64 @2x icons | **repo root** `icons/vehicles/<faction>/` (NOT under `src/`) | `IconResourceTests` |
| Translations | every bundle file (`src/main/resources/messages/StarWarsBundle*.properties`) | `StarWarsResourceBundleTests` |
| Documentation | `LIST_OF_VEHICLES.md` (generated) | generated from factions.json + fallback bundle — see section 5 |
| Changelog entry | `CHANGELOG.md` under `[Unreleased]` → `Added` | release tooling consumes `[Unreleased]` — see section 6 |

If the faction does not exist yet, run the **add-faction** skill first.

Icon files (both locations above) are provided separately — **never create, edit, rename, or delete them**; see section 3.

## Prompt for missing data

Before making any changes, verify that the request specifies everything needed for the entry — in particular `ionEngine`, `xShift` and `yShift`, and the display name. If any of these are missing or ambiguous, **ask the user** instead of guessing or silently using template values — unknown ion engine colors fall back to blue at runtime without any error, so a wrong assumption ships invisibly. Only derive values that follow established convention (e.g., `velocity` matching similar entries, non-English translations following existing bundle patterns).

## 1. Naming rules

- Entity `id`: lowercase snake_case, **no spaces or dashes** — underscores only (e.g. `stap`, `harrower_class_dreadnought`).
- Faction folder name must exactly match the faction `id` in `factions.json`.
- Display names may contain any characters; escape apostrophes as `''` in bundle values.

## 2. factions.json entry

Add to the appropriate faction's `data` array under `"vehicles"`:

```json
{
    "type": "Vehicle",
    "id": "my_vehicle_id",
    "ionEngine": "blue",
    "xShift": -3,
    "yShift": -6,
    "velocity": 2.5
}
```

- `ionEngine` must match one of the keys in the `colors` map defined by `IonEngineColor` (`src/main/kotlin/com/christopherosthues/starwarsprogressbar/ui/IonEngineColor.kt`) — unknown values silently fall back to blue.
- Typical values: vehicles use `velocity` between 0.5 and 5 inclusive; vehicle `yShift` is usually `-6`.

## 3. Icons (Git LFS PNGs) — never touch these

**Never create, edit, rename, or delete any icon file.** Icon PNGs are provided separately by the maintainer; this skill covers everything else. Do not generate placeholder icons and do not copy existing icons to new names.

For reference, each vehicle has an icon set on disk:

| File | Size | Notes |
|---|---|---|
| `src/main/resources/icons/vehicles/<faction>/<id>.png` | 32x32 | Forward-facing; loaded at runtime |
| `src/main/resources/icons/vehicles/<faction>/<id>_r.png` | 32x32 | Backward variant — the vehicle **rotated 180°**, not mirrored |
| `icons/vehicles/<faction>/<id>@2x.png` | 64x64 | Repo root — used in LIST_OF_VEHICLES.md |
| `icons/vehicles/<faction>/<id>_r@2x.png` | 64x64 | Repo root, rotated variant at double size |

- Only the middle vertical **16 px** of a 32x32 icon (middle 32 px of 64x64) is visible — the progress bar clips everything beyond that.
- All PNGs are Git LFS-tracked (`*.png filter=lfs`) — never commit them as regular binaries; verify with `git lfs ls-files` before pushing.
- `IconResourceTests` enforces exactly these paths in both locations. If the icons for a new entity have not been provided yet, that test will fail until they are — expected, and not something to fix from this skill.

## 4. Translations — every bundle file

Add the same key to **every** bundle file under `src/main/resources/messages/`: the fallback `StarWarsBundle.properties` plus one per language defined in `Language` (`configuration/Language.kt`) — files are named `StarWarsBundle_<languageCode>.properties`.

```properties
vehicles.<factionId>.<id>=Display Name
```

- Key format is `<prefix>.<factionId>.<id>` — the faction id prefix is required (it comes from `entityId`, not just `id`).
- Escape apostrophes as `''` (e.g. `Adi Gallia''s Delta-7b-Aethersprite Starfighter`).
- Tests enforce: no empty values, fallback contains every key of the language bundles, and each language bundle contains every fallback key except `name`.

## 5. LIST_OF_VEHICLES.md — generate it

Run from the repo root:

```powershell
python .agents/skills/add-vehicle/scripts/generate_vehicle_list.py
```

The script reads `factions.json` (faction order + vehicle ids) and the fallback bundle (`StarWarsBundle.properties`) for display names, then rewrites the file between the `<!-- Included vehicles -->` / `<!-- Included vehicles end -->` markers. Each entry looks like:

```markdown
* ![Display Name](icons/vehicles/<faction>/<id>@2x.png) Display Name ![Display Name](icons/vehicles/<faction>/<id>_r@2x.png)
```

- Sections follow the faction order in `factions.json`; entries are sorted case-insensitively by display name within each section. The empty-id placeholder faction (holding the `missing` fallback vehicle) is skipped.
- Image paths are relative to the repo root (they point at the @2x icons).
- Display names match what the runtime shows: apostrophes appear as a single `'` even though they are stored doubled in the bundles.
- The script fails with a clear error if a vehicle or faction is missing its bundle key — add the translations first (section 4), then re-run.

## 6. Changelog

Add an entry under `### Added` in the `[Unreleased]` section of `CHANGELOG.md`:

```markdown
- <vehicle name> to <faction name>
```

- `<vehicle name>` is the vehicle's display name from the **fallback (English) bundle** (`StarWarsBundle.properties`) — apostrophes appear as a single `'`.
- `<faction name>` is the faction `id` from `factions.json`, exactly as written there (e.g. `mandalorians`).
- Example: `- Gauntlet starfighter to mandalorians`

If an entry for the same faction already exists under `[Unreleased]` → `Added`, append the vehicle name to that existing line instead of adding a new one — keep the names sorted, comma-separated with `and` before the last:

```markdown
- <vehicle 1> and <vehicle 2> to <faction name>
- <vehicle 1>, <vehicle 2> and <vehicle 3> to <faction name>
```

## 7. Verify

Run from the repo root:

```powershell
.\gradlew.bat test
```

Relevant gates: `IconResourceTests` (icon existence **and** no orphan PNGs — any PNG on disk that is not referenced by a factions.json entry fails CI unless whitelisted in the test) and `StarWarsResourceBundleTests` (translation parity). Then run `.\gradlew.bat check` for the full CI gate.

Also confirm `git diff LIST_OF_VEHICLES.md` shows only the expected change — the generated file should not drift from what the script produces.

## Renaming / removing an entity

- Rename: update the id everywhere above; icon files are renamed separately — never touch them. Orphaned PNGs fail `IconResourceTests` unless added to its `iconWhitelist`. If the vehicle is still unreleased, also fix the `[Unreleased]` changelog entry (section 6).
- Remove: delete the factions.json entry and the bundle keys from all files; icon pairs are deleted separately — never touch them. If the vehicle is still unreleased, remove its `[Unreleased]` changelog entry (section 6); for already-released vehicles add a `### Removed` entry instead.
- After renaming or removing, re-run the generator (section 5) so `LIST_OF_VEHICLES.md` stays in sync.
