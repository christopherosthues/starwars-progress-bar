---
name: Add Vehicle or Lightsaber
description: Adds a new vehicle or lightsaber to the Star Wars Progress Bar plugin. Use when adding (or renaming/removing) an entity — requires updating factions.json, the LFS icon set in two locations, all four translation bundles, and LIST_OF_VEHICLES.md together, enforced by IconResourceTests and StarWarsResourceBundleTests.
---

# Add a vehicle or lightsaber

All pieces must be added **together** — CI tests fail if any part is missing:

| Piece | Location | Enforced by |
|---|---|---|
| Entity definition | `src/main/resources/json/factions.json` (single source of truth) | JSON parsing at startup/tests |
| 32x32 runtime icons | `src/main/resources/icons/{vehicles,lightsabers}/<faction>/` | `IconResourceTests` + runtime loader |
| 64x64 @2x icons | **repo root** `icons/{vehicles,lightsabers}/<faction>/` (NOT under `src/`) | `IconResourceTests` |
| Translations | all four `src/main/resources/messages/StarWarsBundle*.properties` files | `StarWarsResourceBundleTests` |
| Documentation | `LIST_OF_VEHICLES.md` (vehicles only) | manual — keep alphabetical |

## 1. Naming rules

- Entity `id`: lowercase snake_case, **no spaces or dashes** — underscores only (e.g. `stap`, `harrower_class_dreadnought`).
- Faction folder name must exactly match the faction `id` in `factions.json`.
- Display names may contain any characters; escape apostrophes as `''` in bundle values.

## 2. factions.json entry

Add to the appropriate faction's `data` array under `"vehicles"` or `"lightsabers"`.

Vehicle:

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
- Typical values: vehicles use `velocity: 2.5`, lightsabers `velocity: 3`; vehicle `yShift` is usually `-6`.

Lightsaber:

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

- `isJarKai: true` means the entity has **one icon per blade** (see below). Copy an existing entry of the same kind as a template.
- Each blade's `bladeColor` must match one of the keys in the `colors` map defined by `LightsaberColor` (`src/main/kotlin/com/christopherosthues/starwarsprogressbar/ui/LightsaberColor.kt`) — unknown values silently fall back to cyan.

## 3. Icons (Git LFS PNGs)

All PNGs are Git LFS-tracked (`*.png filter=lfs`). Never commit them as regular binaries; verify with `git lfs ls-files` before pushing.

### Vehicle `<id>` in faction `<faction>` — four files:

| File | Size | Notes |
|---|---|---|
| `src/main/resources/icons/vehicles/<faction>/<id>.png` | 32x32 | Forward-facing; loaded at runtime |
| `src/main/resources/icons/vehicles/<faction>/<id>_r.png` | 32x32 | Backward (mirrored) variant |
| `icons/vehicles/<faction>/<id>@2x.png` | 64x64 | Repo root — used in LIST_OF_VEHICLES.md |
| `icons/vehicles/<faction>/<id>_r@2x.png` | 64x64 | Repo root, mirrored variant |

- Only the middle vertical **16 px** of a 32x32 icon (middle 32 px of 64x64) is visible — the progress bar clips everything beyond that.

### Lightsaber `<id>` in faction `<faction>`:

- Non-jar-kai (`isJarKai: false`): `<id>.png` (32x32) + `<id>@2x.png` (64x64). **No `_r` variant.**
- Jar-kai (`isJarKai: true`): one pair per blade entry — `<id>_<bladeId>.png` / `<id>_<bladeId>@2x.png`, where `<bladeId>` is each `lightsabers[].id`.

### New faction (rare)

Also add the faction logo **only** under resources: `src/main/resources/icons/{vehicles,lightsabers}/<faction>/logo.png` and `logo@2x.png`, plus a faction translation key (step 4). Root `icons/` folders contain no logos.

## 4. Translations — all four bundle files

Add the same key to **every** file: `StarWarsBundle.properties` (fallback), `StarWarsBundle_en.properties`, `StarWarsBundle_de.properties`, `StarWarsBundle_es.properties`.

```properties
vehicles.<factionId>.<id>=Display Name
lightsabers.<factionId>.<id>=Display Name
```

- Key format is `<prefix>.<factionId>.<id>` — the faction id prefix is required (it comes from `entityId`, not just `id`).
- New factions additionally need `vehicles.faction.<factionId>=...` or `lightsabers.faction.<factionId>=...`.
- Escape apostrophes as `''` (e.g. `Darth Maul''s first lightsaber`).
- Tests enforce: no empty values, fallback contains every key of the language bundles, and each language bundle contains every fallback key except `name`.

## 5. LIST_OF_VEHICLES.md (vehicles only)

Insert into the faction's `### Faction Name` section, keeping **alphabetical order** by display name:

```markdown
* ![Display Name](icons/vehicles/<faction>/<id>@2x.png) Display Name ![Display Name](icons/vehicles/<faction>/<id>_r@2x.png)
```

Image paths are relative to the repo root (they point at the @2x icons). Lightsabers do not appear in this file.

## 6. Verify

Run from the repo root:

```powershell
.\gradlew.bat test
```

Relevant gates: `IconResourceTests` (icon existence **and** no orphan PNGs — any PNG on disk that is not referenced by a factions.json entry fails CI unless whitelisted in the test) and `StarWarsResourceBundleTests` (translation parity). Then run `.\gradlew.bat check` for the full CI gate.

## Renaming / removing an entity

- Rename: update the id everywhere above **and** rename all icon files; orphaned PNGs fail `IconResourceTests` unless added to its `iconWhitelist`.
- Remove: delete the factions.json entry, both icon pairs, and the bundle keys from all four files (plus the LIST_OF_VEHICLES.md line).
