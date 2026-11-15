# Feature Specification: Lightsaber Blade Variants

**Feature Branch**: `001-lightsaber-blade-variants`

**Created**: 2026-11-14

**Status**: Draft

**Input**: User description: "As a user I want not only all blades of a lightsaber displayed but also variants of it like the left blade or the right blade of a double-bladed lightsaber or in jar-kai style only the first or the second blade. when painting a single bladed variant of a double-bladed lightsaber the hilt should be moved to the start when the right blade is drawn or to the end when the left blade variant is displayed. for jar-kai one blade the blade should take the complete space minus hilt length instead of width minus the length of the longest hilts on both sides, it should take the space like single blades do. In the future there will also be a variation of the icons (different icons / lightsabers for Inquisitors or Asajj Ventress) that should be taken into account when designing the models."

## Clarifications

### Session 2026-11-14

- Q: When a user selects an individual blade variant from a multi-bladed lightsaber, should the selection be presented as a named choice (e.g., "Right Blade", "Left Blade") or as a numeric index (e.g., "Blade 1", "Blade 2"), and how does this affect the settings UI? → A: Named choices with canonical English names that follow the existing translation pattern (bundle keys like `lightsaber.variant.right`, `lightsaber.variant.left`)
- Q: When a selected blade variant's icon resource is missing or fails to load, should the system fall back to displaying the full lightsaber (all blades), display only the blade geometry without hilt icon, or show an error indicator in the progress bar? → A: Display blade geometry without hilt icon — graceful degradation maintains visual continuity
- Q: Should individual blade variant selection apply to both determinate progress bars (showing completion percentage) and indeterminate progress bars (cycling animation), or only to one type? → A: Both determinate and indeterminate progress bars — consistent UX across all operations
- Q: For lightsabers with more than two blades (e.g., crossguard sabers like Kylo Ren's or triple-bladed weapons), should users be able to select individual blade variants, or is variant selection limited only to left/right choices for double-bladed sabers? → A: Individual blade variants for any blade count — flexible architecture handles future content without rework
- Q: When extending `factions.json` to support blade variants, should the JSON structure require a version number field to enable backward-compatible parsing, or can new fields be added directly with default value handling? → A: Direct field addition with default values — simpler approach, existing entries work if defaults are sensible

### Session 2026-11-14 (continued)

- Q: When the global blade variant feature is toggled on in settings, should the default selection be "all blades" (showing complete lightsaber as currently rendered) or "none selected" (requiring user to explicitly choose which blades to display)? → A: All variants selected by default — maintains familiar behavior when feature is enabled
- Q: When a user selects multiple blade variants simultaneously, should all selected blades render together in the progress bar, or should they cycle/rotate during animation? → A: Random selection from chosen set when lightsaber is selected by selector; variant persists until next entity is selected (no explicit change frequency setting)
- Q: What should be the default value for the "number of passes until variant changes" setting — should it change on every progress bar rendering cycle, after a fixed number of cycles, or only when a new build/operation starts? → A: Variant selected randomly when lightsaber is chosen by selector and kept until next entity selection
- Q: When blade variants are enabled, should the random variant selection apply to all existing selector strategies (random, rolling, in-order, reverse), or only to specific selectors like the random selector? → A: Apply to all selector strategies — variant selection independent of entity selection strategy
- Q: When a user selects "all" variants for a multi-bladed lightsaber, should the progress bar display all blades simultaneously (showing complete weapon), or randomly select one blade to display at a time? → A: "Both/all" option displays all blades simultaneously (current behavior); random single variant selection applies only when multiple individual variants are selected from subset options (e.g., "left", "right", "first", "second")

### Session 2026-11-14 (edge cases)

- Q: When a user selects an individual blade variant from a double-bladed lightsaber where each blade has a different color (e.g., one red, one blue), should the displayed single blade retain its original color, or should both blades share the same color in such cases? → A: Blade retains its original color — accurate representation of selected variant
- Q: When the progress bar width is too narrow to display both hilt and selected blade variant comfortably, should the system prioritize showing the hilt icon and scale down the blade, hide the hilt entirely and show only the blade, or clamp minimum dimensions to maintain visual integrity? → A: Hide hilt, show blade only — maximizes visible blade length at narrow widths
- Q: When the `showIcon` setting is disabled for progress bars, how should jar-kai individual blade variants render — should they display only the blade geometry (no hilt), use a simplified text indicator, or fall back to showing all blades as a single unit? → A: Display only blade geometry — consistent with current behavior when `showIcon` disabled
- Q: When the specification states "individual blade variants for any blade count," should the implementation support a specific maximum number of blades per lightsaber (e.g., 4-6 blades), or is there truly no upper bound defined? → A: No upper bound for lightsaber entities; max 2 blades per individual lightsaber weapon
- Q: When storing a user's blade variant selection in `StarWarsState`, should each variant be identified by a unique combination of lightsaber name plus variant label (e.g., "Darth Maul saber" + "right blade"), or is there a simpler identification scheme that doesn't require compound keys? → A: Each lightsaber and each blade has an ID; variant selections stored as list of combined identifiers
- Q: What is the correct maximum number of lightsabers per entity for jar-kai configurations, and what is the maximum blades per individual lightsaber? → A: Max 4 lightsabers per entity; each with max 2 blades

### Session 2026-11-14 (V1 scope simplification)

- Q: When the global blade variant toggle is enabled for V1, does "all possible combinations are possible" mean that the random selection pool includes both the full weapon display (current behavior) and all individual blade variants, or does it include only individual blade variants with the full weapon as a separate option? → A: Full weapon + all individual blades included in random pool — maximum variety, includes current behavior as one option
- Q: When the global blade variant toggle is enabled and random selection occurs, should the selected variant persist until the next entity is chosen by the selector (same as the original spec), or should it change on every new progress bar operation/build cycle? → A: Persist until next entity selection — stable display during long operations, one variant per lightsaber selection event
- Q: Given the V1 scope reduction (global toggle only, no per-lightsaber checkboxes), should User Stories 1 and 2 be updated to reflect that variant selection is now system-managed (random) rather than user-chosen, or can they remain as-is with a V1/V2 scope annotation? → A: Update stories for system-managed selection — clearer V1 scope; acceptance scenarios become testable for automatic behavior

### Session 2026-11-14 (variant declaration model change)

- Q: Should blade variants be derived automatically from the lightsaber's blade structure (e.g., double-bladed → left/right options), or should each `Lightsabers` entity in factions.json explicitly declare which display modes are allowed? → A: Entity-declared variants — each Lightsabers entry holds a list of allowed variants; gives content authors control over what looks good together (e.g., jar-kai with one double-blade showing both blades and another showing only one blade looks weird without explicit declaration)
- Q: When the global variant toggle is disabled, should the system render using the first/default variant from the entity's declared list? → A: Yes — current implementation behavior is the default/first variant; when variants are turned off, this renders automatically
- Q: Does this mean we can drop the planned `variantId` field in the Blade class and the `lightsaberVariantSelections` map from StarWarsState? → A: Yes — variants are declared at the entity level (`Lightsabers.variants: List<String>`); no per-blade variant tracking or user selection storage needed for V1

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Display individual blade variants from double-bladed lightsabers (Priority: P1) *(V1: system-managed random selection)*

When the global blade variant toggle is enabled and a double-bladed lightsaber is selected for display in the progress bar, the system randomly chooses to show either the full weapon or an individual blade variant. When displaying only the right blade, the hilt moves to the start (left side); when displaying only the left blade, the hilt moves to the end (right side). The selected variant persists until the next entity is chosen by the selector.

**Why this priority**: This is the core feature request — enabling individual blade display from multi-blade lightsabers with proper hilt positioning. It delivers immediate visual variety and realism for users who want to see varied blade configurations during builds and indexing operations.

**Independent Test**: Can be fully tested by enabling the global variant toggle, triggering a build operation that selects a double-bladed lightsaber (e.g., Darth Maul's saber), and verifying across multiple operations that both individual blade variants and the full weapon display appear with correct hilt positioning. The display should match the visual appearance of a single-blade lightsaber held in one hand when an individual variant is shown.

**Acceptance Scenarios**:

1. **Given** the global blade variant toggle is enabled and a double-bladed lightsaber is selected for display, **When** the system randomly chooses "right blade only" variant, **Then** only the right blade is rendered with the hilt positioned at the left edge of the progress bar
2. **Given** the global blade variant toggle is enabled and a double-bladed lightsaber is selected for display, **When** the system randomly chooses "left blade only" variant, **Then** only the left blade is rendered with the hilt positioned at the right edge of the progress bar
3. **Given** a single-blade variant is displayed, **When** the progress bar animates, **Then** the blade length scales proportionally to the progress amount while maintaining correct hilt position

---

### User Story 2 - Jar-Kai style displays individual blades with full-length rendering (Priority: P1) *(V1: system-managed random selection)*

When the global blade variant toggle is enabled and a jar-kai lightsaber configuration is selected for display, the system may randomly choose to show an individual blade variant. That blade renders using the full available space minus only the hilt length — matching how single-blade lightsabers render. Previously, jar-kai blades shared space between multiple hilts; when an individual variant is shown, it takes complete space like a standalone saber.

**Why this priority**: Jar-kai is already supported in the plugin (e.g., Ahsoka Tano's sabers), and users expect individual blade selection to behave consistently with single-blade rendering — using full available width for dramatic effect. Random variant display adds visual variety during long-running operations.

**Independent Test**: Can be fully tested by enabling the global variant toggle, triggering multiple build operations that select a jar-kai lightsaber (e.g., Ahsoka Tano's sabers), and verifying that when an individual blade variant is randomly chosen, it extends across the full progress bar width minus hilt length, matching the rendering of any single-blade lightsaber.

**Acceptance Scenarios**:

1. **Given** the global blade variant toggle is enabled and a jar-kai lightsaber is selected for display, **When** the system randomly chooses an individual blade variant (e.g., "first blade"), **Then** that blade renders using full available width minus only its hilt length
2. **Given** a jar-kai individual blade is displayed via random selection, **When** comparing to a single-blade lightsaber, **Then** both use identical space allocation logic (full width minus one hilt)
3. **Given** multiple jar-kai hilts exist in the icon set and an individual variant is randomly selected, **When** the progress bar renders, **Then** only that blade's corresponding hilt renders at the appropriate position

---

### User Story 3 - Model design supports future icon variations (Priority: P2) *(V1: foundational architecture)*

The data model and rendering system are designed to accommodate lightsaber icon variations — different visual representations of the same lightsaber for different characters or contexts (e.g., Grand Inquisitor's saber vs. Fifth Brother's saber, or Asajj Ventress's different weapon configurations). The architecture supports associating multiple icon sets with a single logical lightsaber entity. *V1 implements the foundational data model; user-facing variation selection is deferred to V2.*

**Why this priority**: While not immediately visible to users in V1, this enables future content expansion without architectural changes. It allows the plugin to support character-specific weapon variants while maintaining data integrity.

**Independent Test**: Can be fully tested by adding a new lightsaber entry in factions.json that references multiple icon file names for the same logical saber, verifying that the system can load and select between them without breaking existing functionality.

**Acceptance Scenarios**:

1. **Given** a lightsaber entity supports multiple icon variations, **When** a specific variation is selected, **Then** only that variation's icons are used for rendering
2. **Given** icon variations exist for the same lightsaber, **When** switching between variations, **Then** all blade geometry and color properties remain consistent while visual appearance changes
3. **Given** a new icon variation is added to factions.json, **When** the plugin loads, **Then** it appears as an available option without requiring code changes

---

### Edge Cases

- When a user selects a blade variant from a lightsaber whose icon set doesn't include that specific variant, the system displays only the blade geometry without hilt icon (graceful degradation)
- When displaying an individual blade from a double-bladed lightsaber with different colored blades, the selected blade retains its original color (e.g., selecting "right blade" shows only that blade's specific color)
- When progress bar width is too narrow to display both hilt and selected blade variant, the system hides the hilt icon and displays only the blade geometry to maximize visible blade length
- When `showIcon` setting is disabled, jar-kai individual blade variants display only the blade geometry (no hilt), consistent with current behavior for all lightsaber types

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: System MUST provide a global toggle checkbox in settings to enable/disable blade variant selection, following the established setting pattern (default constant, bundle key, state field, UI panel); when enabled, all possible blade variants are available for random selection; when disabled, only the current lightsaber display behavior is applied (full weapon renders as currently implemented) — *V1 scope: single toggle only, no per-lightsaber variant checkboxes*
- **FR-002**: *(Deferred to V2)* System MAY present blade variant selection as a multiselect checkbox interface matching the faction checkboxes pattern: parent lightsaber checkbox with nested subcheckboxes for each blade variant; subcheckboxes display without icons or repeated lightsaber names
- **FR-002b**: *(Deferred to V2)* System MUST hide the blade variant selection UI when the parent lightsaber is deselected, and restore it when reselected
- **FR-003**: System MUST reposition the hilt to the left edge when displaying only the right blade of a double-bladed lightsaber
- **FR-004**: System MUST reposition the hilt to the right edge when displaying only the left blade of a double-bladed lightsaber
- **FR-005**: System MUST render jar-kai individual blades using full available width minus only the selected blade's hilt length (matching single-blade behavior)
- **FR-006**: Data model MUST support associating multiple icon file names with a single lightsaber entity to enable future character-specific variations
- **FR-007**: System MUST maintain backward compatibility — existing double-bladed and jar-kai displays continue to work without requiring user configuration changes; individual blade variant selection applies to both determinate progress bars (completion percentage) and indeterminate progress bars (cycling animation) for consistent user experience
- **FR-008**: When the global blade variant toggle is enabled, the system MUST randomly select from a pool that includes both the full weapon display (current behavior) AND all individual blade variants when the selector chooses that lightsaber entity; the selected variant persists until the next entity is selected; this random selection applies to all existing selector strategies regardless of their determinism
- **FR-009**: System MUST validate that selected blade variants have corresponding icon resources before rendering

### Key Entities *(include if feature involves data)*

- **Lightsaber Variant**: A selectable display mode for a lightsaber entity, representing either the full weapon (all blades) or an individual blade. Includes positioning logic (hilt placement) and space allocation rules. Each variant is identified by a unique ID. *V1: System manages variant selection internally; no user-facing per-entity storage required.*
- **Blade Selection**: *(Simplified for V1)* The system's internal random choice of which blade variant to display from the full pool (full weapon + all individual blades). No persistent per-lightsaber selection stored — only the global toggle state is persisted. *V2 may add per-entity multiselect storage.*
- **Icon Variation Set**: A collection of alternative icon files for the same logical lightsaber entity, enabling character-specific visual representations while sharing geometry and color data. Each variation has a unique ID within its parent lightsaber.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: *(V1)* Users can enable/disable blade variant selection with a single checkbox in the settings UI; when enabled, random variant selection activates automatically for all multi-blade lightsabers — *no per-entity clicks required*
- **SC-002**: Hilt positioning for single-blade variants is visually correct across all progress bar widths and configurations, with no visible misalignment between hilt and blade attachment points
- **SC-003**: Jar-kai individual blade rendering matches single-blade space allocation within 5 pixels across all progress bar widths from 100px to 2000px
- **SC-004**: Data model supports up to 4 lightsabers per entity, each with up to 2 blades, without requiring schema migration or code refactoring

## Assumptions

- Users understand the difference between double-bladed, jar-kai, and single-blade lightsaber configurations from Star Wars canon/legends
- The existing `factions.json` structure can be extended by adding new fields directly with sensible default values; no version number field is required for backward-compatible parsing (existing entries continue to deserialize correctly)
- Icon resources for individual blade variants follow the same naming convention as existing icons (LFS-tracked PNG files)
- Blade color properties remain consistent across variants of the same lightsaber entity
- The feature works within the existing `StarWarsState` persistent configuration system; new settings follow the established pattern (default constant, bundle key, state field, UI panel)
- No upper bound exists for the number of lightsaber entities in factions.json; individual weapon configurations have a maximum of 4 lightsabers per entity (jar-kai), each with a maximum of 2 blades (double-bladed sabers represent the most complex case)
- **V1 scope limitation**: Only a global toggle checkbox is implemented; per-lightsaber variant multiselect UI is deferred to V2. When enabled, random selection includes both full weapon display AND all individual blade variants in the pool.
