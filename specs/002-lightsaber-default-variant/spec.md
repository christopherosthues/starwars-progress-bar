# Feature Specification: Lightsaber Default Variant (Unified Variant Rendering)

**Feature Branch**: `002-lightsaber-default-variant`

**Created**: 2026-11-20

**Status**: Draft

**Input**: User description: "I want for each lightsabers object a default variant that is used by the calculation to unify the computation. Take it as a fact that every lightsabers object has at least one variant in the list. The default index is 0 for the variant, it should also be possible to set it in the factions.json (don't read it, don't edit it) as a property (index of the variants list). The double-blade behavior still has to be adopted to display it in the middle when both blades of one saber are selected by a variant. maul_second is a single-blade; the correct variants will be added to factions.json manually."

## Clarifications

### Session 2026-11-20

- Q: Unified path — should `paintProgressBar` always use the variant layout with the selected index (random when toggle ON, `defaultVariant` when OFF), and the old plain `compute()` be deleted? → A: Yes — one code path for all entities; plain `compute()` is removed since every entity has at least one variant.
- Q: When the "Enable lightsaber variants" toggle is OFF, should the selected index always be the entity's `defaultVariant` (no random selection)? → A: Yes — OFF = deterministic per-entity default; ON = random from all declared variants.
- Q: What should happen if `defaultVariant` is invalid (negative or >= `variants.size`)? → A: Fall back to variant 0, AND add a test that parses the bundled factions.json and asserts every entity's `defaultVariant` is in range — run locally and in CI so this case can never occur.
- Q: For a double-bladed saber rendered with only ONE blade selected by a variant, where does the hilt go? → A: By the selected blade's direction (`isForward`: forward → left edge, backward → right edge).
- Q: When the icon is hidden or the bar is too narrow for a hilt, what should be rendered? → A: Keep the variant geometry (both blades for a full double-blade variant), hide only the hilt icon.
- Q: What should the JSON property and model field be named? → A: `defaultVariant` — `"defaultVariant": N` in factions.json; model field `defaultVariant: Int = 0`.
- Q: Jar-kai entities can have double-bladed lightsabers. How are variants that select both blades of a jar-kai saber rendered? → A: Any saber whose variant entries select both blades renders as a normal centered double-bladed lightsaber (case 3), per-saber — when every saber in the variant is full, all hilts sit centered with all blades extending outward. factions.json declares only these shapes: single-saber jar-kai (single blade or full double-blade), multi-saber jar-kai with one blade per saber, or all sabers full; mixed combinations do not exist.

## Display Cases (normative)

All rendering goes through a single variant-based layout computation using the selected variant index. The cases below define how each shape of selected variant is displayed.

| # | Case | Rendering |
|---|------|-----------|
| 1 | Single-blade saber, forward blade (`isForward = true`) | Hilt at left edge; blade extends right from the hilt emitter |
| 2 | Single-blade saber, backward blade (`isForward = false`) | Hilt at right edge; blade extends left from the hilt emitter |
| 3 | Variant selects **both blades of a saber** (bladeIds {0, 1} for that saber) — applies per-saber, including jar-kai sabers | Saber rendered as a normal double-bladed lightsaber: hilt centered in the progress bar; both blades extend outward from the center |
| 4 | Jar-kai variant with multiple sabers active, one blade each | Forward-saber hilt at left edge + backward-saber hilt at right edge; each blade stops at the other hilt's emitter (space shared between hilts) |
| 5 | Single-saber jar-kai variant | Rendered as a standalone saber: full available width minus its own hilt length, one hilt icon only (cases 1/2/3) |
| 6 | Jar-kai variant where **every** selected saber has both blades | Each saber rendered as a normal double-bladed lightsaber (case 3): all hilts centered, all blades extend outward from their centers |
| 7 | Icon hidden (`showIcon` off) or bar too narrow for a hilt | No hilt icon; blades still rendered per the selected variant's geometry (full double-blade keeps both blades) |

Notes:

- Case 3 centering is determined by **variant content** (both bladeIds of that saber present), not by the `isDoubleBladed` flag. It applies per-saber, so jar-kai sabers can be centered too.
- A single-blade variant of a double-bladed saber uses case 1 or 2 based on that blade's own `isForward`.
- Mixed variants (one saber full + another saber with a single blade) do not exist in factions.json; if they ever did, each saber would still be rendered independently per these rules.
- Only sabers referenced by the selected variant are rendered — including their hilt icons (a single-saber jar-kai variant draws exactly one hilt icon).

## Selection Rules

- Toggle **ON**: index chosen randomly from the entity's declared `variants` list; persists until the next entity selection (existing behavior, unchanged).
- Toggle **OFF**: index = entity's `defaultVariant`. No randomization.
- Entity with exactly one variant: that variant is rendered regardless of toggle state.
- Invalid `defaultVariant` (negative or >= `variants.size`): fall back to variant 0.

## Requirements

### Functional Requirements

- **FR-001**: The `Lightsabers` model MUST expose a serializable property `defaultVariant: Int = 0`, deserialized from `"defaultVariant"` in factions.json as an index into the entity's `variants` list; absent values default to 0 (backward-compatible parsing).
- **FR-002**: Rendering MUST always use the variant-based layout computation with the selected variant index. The legacy plain `compute()` path and its branching condition (`enableVariants && variants.size > 1`) in the decorator MUST be removed — there is exactly one rendering code path.
- **FR-003**: When the global "Enable lightsaber variants" toggle is ON, the selected variant index MUST be randomly chosen from the entity's declared variants and persist until the next entity selection (existing behavior).
- **FR-004**: When the toggle is OFF, the selected variant index MUST be the entity's `defaultVariant` — deterministic per-entity rendering with no randomization.
- **FR-005**: An invalid `defaultVariant` (negative or >= `variants.size`) MUST fall back to variant 0 at selection time. A test that parses the bundled factions.json and asserts every lightsaber entity's `defaultVariant` is within range of its `variants` list MUST exist and run in CI so this case can never occur in practice.
- **FR-006**: The display cases defined above (cases 1–7) MUST be honored for every selected variant, including: centered hilt for any saber whose variant selects both blades (cases 3/6), edge placement by blade direction for single-blade sabers (cases 1/2/4/5), shared-space placement for multi-saber jar-kai variants (case 4), and hilt omission with preserved variant geometry when the icon is hidden or the bar is too narrow (case 7).
- **FR-007**: Hilt icon selection per rendered saber MUST continue to use `CombinedId.lightsaberId`, so each rendered saber draws its own icon (jar-kai `_id`-suffixed icons; single icon otherwise).

### Key Entities

- **Default Variant**: A per-entity index into the entity's declared `variants` list, stored as `defaultVariant: Int = 0` on `Lightsabers`. It is the variant rendered when the global variants toggle is OFF and serves as the canonical "canonical look" of the entity.
- **Selected Variant Index**: The variant index used for a rendering pass — random (toggle ON) or `defaultVariant` (toggle OFF), always clamped/fallback-resolved to a valid index before layout computation.

## Success Criteria

### Measurable Outcomes

- **SC-001**: With the toggle OFF, every lightsaber entity renders deterministically via its declared default variant — no entity renders all of its sabers anymore (e.g., jar-kai entities show only their default variant).
- **SC-002**: Exactly one layout code path exists: `LightsaberLayoutCalculator.compute()` is deleted and the decorator contains no branching on `variants.size`.
- **SC-003**: The bundled factions.json passes the in-range `defaultVariant` validation test locally and in CI.
- **SC-004**: Visual parity with current variant rendering is preserved: full double-blade sabers remain centered (including jar-kai, case 3/6), jar-kai multi-saber variants keep edge placement with blades stopping at the other hilt's emitter (existing layout tests continue to pass).

## Assumptions

- Every `Lightsabers` object in factions.json declares at least one variant. The model keeps its defensive default (`[[CombinedId(1, 0)]]`) for parsing robustness only.
- Content corrections and per-entity `defaultVariant` values (including the `darth_maul_second` single-blade correction and correct variant lists) are added to factions.json manually by the user; this feature does not read or edit factions.json.
- Layout decisions derive from the selected variant's `CombinedId`s and each blade's own properties (`isForward`, `isShoto`, ...). The `isDoubleBladed` flag no longer drives layout (centering is determined by variant content per case 3); it remains in the model for data fidelity.
- Jar-kai entities can have double-bladed lightsabers. factions.json declares only these jar-kai shapes: single-saber (single blade or full double-blade), multi-saber with one blade per saber, or all sabers full — mixed combinations do not exist.
- The global "Enable lightsaber variants" toggle, its state field, and settings UI are unchanged; only the OFF-state behavior changes from "render all sabers via plain path" to "render the default variant".

## References

- [Lightsaber Blade Variants (001)](../001-lightsaber-blade-variants/spec.md) — original variant feature, entity-declared variants model, and V1 scope decisions
- `src/main/kotlin/com/christopherosthues/starwarsprogressbar/models/Lightsabers.kt` — model to extend with `defaultVariant`
- `src/main/kotlin/com/christopherosthues/starwarsprogressbar/selectors/LightsaberVariantSelector.kt` — selection rule change (OFF → default variant)
- `src/main/kotlin/com/christopherosthues/starwarsprogressbar/ui/LightsaberLayoutCalculator.kt` — unified layout computation (`compute()` removed)
- `src/main/kotlin/com/christopherosthues/starwarsprogressbar/ui/LightsaberProgressBarDecorator.kt` — single rendering path
