---
name: Scale 2X Icon
description: Scales a single @2x icon down to half resolution and stores it as an RGBA PNG in the corresponding directory under src/main/resources/icons/. Use when generating the runtime icon for one vehicle, lightsaber blade, or logo — runs scripts/ScaleIcons.java (JDK only, no dependencies), which halves each dimension rounded up and overwrites existing targets.
---

# Scale a 2X Icon to Runtime Resolution

The repo keeps double-resolution (`@2x`) icons at the **repo root** `icons/{vehicles,lightsabers}/<faction>/` and runtime icons under `src/main/resources/icons/...`. This skill generates the runtime icon for **one** @2x icon.

## Rules

- Target size: each dimension halved, **rounded up** (e.g., 109 -> 55, 27 -> 14).
- Output is always an **RGBA** PNG, regardless of the source color type.
- Naming: strip `@2x` from the file name — `stap@2x.png` -> `stap.png`, `stap_r@2x.png` -> `stap_r.png`.
- Location mirrors the source path under `src/main/resources/icons/`: `icons/vehicles/galactic_empire/stap@2x.png` -> `src/main/resources/icons/vehicles/galactic_empire/stap.png`. For files already inside `src/main/resources/icons/` (e.g. faction logos), the result is written next to the source.
- Existing targets are **overwritten** — scaling an icon again replaces its runtime copy.

## Running the script

From the repo root (requires only a JDK — no dependencies):

```powershell
java .agents\skills\scale-2x-icon\scripts\ScaleIcons.java icons\vehicles\galactic_empire\stap@2x.png
```

The script takes exactly one argument: the path of the @2x icon to scale. It prints the conversion with source and target dimensions; check that the target matches expectations (e.g., vehicles: 64x64 -> 32x32).

## Verify

- Runtime PNGs are Git LFS-tracked (`*.png filter=lfs`) — verify before pushing with `git lfs ls-files`.
- `IconResourceTests` will most probably fail, no test execution needed.

## Notes

- Create or edit the @2x icon first (see **add-vehicle**, **add-lightsaber**, and **add-faction**); this skill only derives the runtime copy.
- Do not run the script speculatively over whole directories — scale exactly the icons that were added or changed.
