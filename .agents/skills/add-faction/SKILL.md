---
name: Add Faction
description: Adds a new faction to the Star Wars Progress Bar plugin. Use when introducing a faction not present in factions.json — requires a factions.json entry, LFS-tracked faction logos (logo.png/logo@2x.png), and faction translation keys in every bundle file.
---

# Add a faction

A faction is an `id` + `data` array under the top-level `"vehicles"` or `"lightsabers"` list in `src/main/resources/json/factions.json`. The two lists are independent: e.g. `jedi` appears in both, while vehicles' `sith_empire` and lightsabers' `sith` are separate ids for unrelated sith factions. Add a faction to the list provided by the user.

## 1. factions.json entry

```json
{
    "id": "my_faction_id",
    "data": []
}
```

- Faction `id`: lowercase snake_case, **no spaces or dashes** — underscores only. It must match the icon folder name created in step 2.
- Do not touch the special `"id": ""` faction at the end of the vehicles list — it holds the `missing` vehicle fallback placeholder.

## 2. Faction logos (Git LFS PNGs)

For each list the faction is added to, add the logo **only** under resources (repo-root `icons/` contains no logos):

| File | Size |
|---|---|
| `src/main/resources/icons/{vehicles,lightsabers}/<faction>/logo.png` | 16x16 |
| `src/main/resources/icons/{vehicles,lightsabers}/<faction>/logo@2x.png` | 32x32 |

- All PNGs are Git LFS-tracked (`*.png filter=lfs`). Never commit them as regular binaries; verify with `git lfs ls-files` before pushing.
- A missing logo falls back to an empty icon at runtime — keep faction folders complete.

## 3. Translations — every bundle file

Add the same key to **every** bundle file under `src/main/resources/messages/` (the fallback `StarWarsBundle.properties` plus one per language defined in `Language` (`configuration/Language.kt`) — files are named `StarWarsBundle_<languageCode>.properties`). Only add the key for the list(s) the faction was added to:

```properties
vehicles.faction.<factionId>=Display Name
lightsabers.faction.<factionId>=Display Name
```

- Escape apostrophes as `''`.
- Tests enforce: no empty values, fallback contains every key of the language bundles, and each language bundle contains every fallback key except `name`.

## 4. Verify

Run from the repo root:

```powershell
.\gradlew.bat test
```

Relevant gates: `IconResourceTests` (logo existence for every non-empty faction id) and `StarWarsResourceBundleTests` (faction translations). Then run `.\gradlew.bat check` for the full CI gate.

## Notes

- No `LIST_OF_VEHICLES.md` entry is needed until vehicles are added — when adding the first vehicle, create its `### Display Name` section (see **add-vehicle** skill).
- Removing a faction: delete the factions.json entry, both logo files, and the faction key from all four bundles; orphaned PNGs fail `IconResourceTests` unless whitelisted.
