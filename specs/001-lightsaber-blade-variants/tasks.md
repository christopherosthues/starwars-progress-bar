---

description: "Task list template for feature implementation"

---

# Tasks: Lightsaber Blade Variants (V1)

**Input**: Design documents from `/specs/001-lightsaber-blade-variants/`

**Prerequisites**: plan.md (required), spec.md (required for user stories), research.md, data-model.md, contracts/

**Tests**: The examples below include test tasks. Tests are OPTIONAL — only include them if explicitly requested in the feature specification or for existing test infrastructure coverage.

**Organization**: Tasks are grouped by user story to enable independent implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Path Conventions

- **Project**: IntelliJ Platform plugin (Kotlin); source under `src/main/kotlin/`, tests under `src/test/kotlin/`
- Paths shown below follow the existing package structure: `com.christopherosthues.starwarsprogressbar`

<!--
  ============================================================================
  IMPORTANT: The tasks below are actual tasks based on:
  - User stories from spec.md (with their priorities P1, P2)
  - Feature requirements from plan.md
  - Entities from data-model.md
  - Contracts from contracts/

  Tasks MUST be organized by user story so each story can be:
  - Implemented independently
  - Tested independently
  - Delivered as an MVP increment

  DO NOT keep the sample tasks from the template.
  ============================================================================
-->

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure for V1 scope

- [X] T001 Create `src/main/kotlin/.../models/CombinedId.kt` — serializable data class with `lightsaberId: Int` (1-based index into Lightsabers.lightsabers list) and `bladeId: Int` (0-based index into Lightsaber.blades list); references specific lightsaber/blade combinations per data-model.md; annotate with `@Serializable`
- [X] T002 Create `src/main/kotlin/.../models/BladeSelectionType.kt` — enum for variant selection modes (`FULL_WEAPON`, `SINGLE_BLADE`) used by rendering code to determine layout strategy

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T003 Add `DEFAULT_ENABLE_LIGHTSABER_VARIANTS` constant to `src/main/kotlin/.../constants/ConfigurationConstants.kt` — default value `true` per FR-001; follows existing pattern (`internal const val DEFAULT_...`)
- [X] T004 Add `ENABLE_LIGHTSABER_VARIANTS` bundle key to `src/main/kotlin/.../constants/BundleConstants.kt` — constant value `"configuration.enableLightsaberVariants"`; follows existing pattern in BundleConstants object
- [X] T005 [P] Add variant label keys to all 4 bundle files per Constitution Principle IV:
  - `src/main/resources/messages/StarWarsBundle.properties` (default): add `configuration.enableLightsaberVariants=Enable lightsaber variants`
  - `src/main/resources/messages/StarWarsBundle_en.properties` (English): add `configuration.enableLightsaberVariants=Enable lightsaber variants`
  - `src/main/resources/messages/StarWarsBundle_de.properties` (German): add German translation for `configuration.enableLightsaberVariants`
  - `src/main/resources/messages/StarWarsBundle_es.properties` (Spanish): add Spanish translation for `configuration.enableLightsaberVariants`
- [X] T006 Extend `Lightsabers` data class in `src/main/kotlin/.../models/Lightsabers.kt` with `variants: List<List<CombinedId>> = listOf(listOf(CombinedId(1, 0)))` field — declares allowed display modes as explicit CombinedId references; default value preserves existing single-blade behavior per data-model.md validation rules; add import for CombinedId
- [X] T007 Add `enableLightsaberVariants: Boolean` field to `src/main/kotlin/.../configuration/StarWarsState.kt`; update `copy()` method to copy new field; update `isUiOptionsModified()` check in `isModified()` per add-setting skill workflow — **note**: no `lightsaberVariantSelections` map needed (variants declared in factions.json as List<List<CombinedId>>)
- [X] T008 Add `ENABLE_LIGHTSABER_VARIANTS_EVENT` constant to `src/main/kotlin/.../configuration/StarWarsStateEvents.kt` — event name `"enableLightsaberVariants"`; follows existing pattern for property change events

**Checkpoint**: Foundation ready — user story implementation can now begin in parallel

---

## Phase 3: User Story 1 - Display individual blade variants from double-bladed lightsabers (Priority: P1) 🎯 MVP

**Goal**: When the global blade variant toggle is enabled and a double-bladed lightsaber entity with multiple declared variants is selected for display, the system randomly chooses to show either the full weapon or an individual blade variant. Hilt positioning adjusts correctly based on blade index in the CombinedId (left edge for first blade `bladeId=0`, right edge for second blade `bladeId=1`).

**Independent Test**: Enable the global variant toggle, trigger a build operation that selects a double-bladed lightsaber entity with declared variants (e.g., Darth Maul's saber), and verify across multiple operations that both individual blade variants and the full weapon display appear with correct hilt positioning.

### Tests for User Story 1 ⚠️

> **NOTE**: Write these tests FIRST, ensure they FAIL before implementation

- [X] T009 [P] [US1] Create `src/test/kotlin/.../ui/LightsaberLayoutCalculatorTest.kt` — test single-blade variant layout calculation for double-bladed lightsabers using CombinedId references (hilt positioning at left edge for `bladeId=0`, right edge for `bladeId=1`)
- [X] T010 [P] [US1] Create `src/test/kotlin/.../selectors/LightsaberVariantSelectorTest.kt` — test random variant INDEX selection logic from entity's declared variants list; verify persistence until next entity selection

### Implementation for User Story 1

- [X] T011 [P] [US1] Add global toggle checkbox to settings UI in `src/main/kotlin/.../configuration/components/UiOptionsPanel.kt` — follows existing pattern (JBCheckBox with addItemListener); label from bundle key `BundleConstants.ENABLE_LIGHTSABER_VARIANTS`; default checked per FR-001; fire property change event `ENABLE_LIGHTSABER_VARIANTS_EVENT`; update `updateUI()` method to sync checkbox state
- [X] T012 [US1] Create `src/main/kotlin/.../selectors/LightsaberVariantSelector.kt` — implements random variant INDEX selection from entity's declared `variants: List<List<CombinedId>>` list; applies to all selector strategies per FR-008; selected index persists until next entity selection; falls back to first/default variant (index 0) when toggle disabled or single variant declared
- [X] T013 [US1] Extend `src/main/kotlin/.../ui/LightsaberLayoutCalculator.kt` with new method `computeVariantLayout(lightsabers: Lightsabers, selectedVariantIndex: Int, iconWidths: List<Int>, width: Int, amountFull: Int, showIcon: Boolean): List<LightsaberLayout>` — computes layout for all CombinedIds in the selected variant per rendering-contract.md; reads from `lightsabers.variants[selectedVariantIndex]`
- [X] T014 [US1] Implement hilt repositioning logic in `src/main/kotlin/.../ui/LightsaberLayoutCalculator.kt`:
  - When CombinedId has `bladeId = 0` (first blade): hilt positioned at left edge (`x = 0`) per FR-003
  - When CombinedId has `bladeId = 1` (second blade): hilt positioned at right edge (`x = width - iconWidth`) per FR-004
  - Blade index determines positioning — no string parsing needed
- [X] T015 [US1] Integrate `LightsaberVariantSelector` with existing rendering infrastructure in `src/main/kotlin/.../ui/LightsaberProgressBarDecorator.kt` — variant selection independent of entity selection strategy per FR-008; reads from `Lightsabers.variants` list

**Checkpoint**: At this point, User Story 1 should be fully functional and testable independently — double-bladed lightsabers display individual blade variants with correct hilt positioning when global toggle is enabled.

---

## Phase 4: User Story 2 - Jar-Kai style displays individual blades with full-length rendering (Priority: P1) 🎯 MVP

**Goal**: When the global blade variant toggle is enabled and a jar-kai lightsaber entity with multiple declared variants is selected for display, the system may randomly choose to show an individual blade variant. That blade renders using the full available space minus only the hilt length — matching how single-blade lightsabers render (within 5 pixels per SC-003).

**Independent Test**: Enable the global variant toggle, trigger multiple build operations that select a jar-kai lightsaber entity with declared variants (e.g., Ahsoka Tano's sabers), and verify that when an individual blade variant is randomly chosen, it extends across the full progress bar width minus hilt length, matching the rendering of any single-blade lightsaber.

### Tests for User Story 2 ⚠️

- [X] T016 [P] [US2] Extend `src/test/kotlin/.../ui/LightsaberLayoutCalculatorTest.kt` — test jar-kai individual blade layout calculation using CombinedId references (full width minus hilt length only)
- [X] T017 [P] [US2] Add test case to verify space allocation matches single-blade behavior within 5 pixels per SC-003

### Implementation for User Story 2

- [X] T018 [US2] Extend `src/main/kotlin/.../ui/LightsaberLayoutCalculator.kt` with jar-kai variant handling in `computeVariantLayout()` — when selected variant contains single CombinedId, compute layout using full available width minus only that blade's hilt length per FR-005; reads from entity.variants list
- [X] T019 [US2] Implement space allocation logic in `src/main/kotlin/.../ui/LightsaberLayoutCalculator.kt`:
  - Formula: `bladeWidth = progressAmount * (width - hiltIconWidth) / width`
  - Hilt positioning: `hiltX = if (lightsaber.id.isOdd()) 0 else width - hiltIconWidth` per rendering-contract.md
  - CombinedId.lightsaberId identifies which hilt to render; CombinedId.bladeId identifies which blade from that hilt
- [X] T020 [US2] Integrate jar-kai variant selection with existing Lightsabers entity processing in `src/main/kotlin/.../ui/LightsaberProgressBarDecorator.kt` — when global toggle enabled, apply random variant INDEX selection to jar-kai configurations; resolve correct icon per CombinedId.lightsaberId; reads from `Lightsabers.variants` list

**Checkpoint**: At this point, User Stories 1 AND 2 should both work independently — double-bladed and jar-kai lightsabers display individual blade variants with correct positioning and space allocation.

---

## Phase 5: User Story 3 - Model design supports future icon variations (Priority: P2)

**Goal**: The data model and rendering system are designed to accommodate lightsaber icon variations — different visual representations of the same lightsaber for different characters or contexts. *V1 implements the foundational data model; user-facing variation selection is deferred to V2.*

**Independent Test**: Add a new lightsaber entry in factions.json that references multiple icon file names for the same logical saber, verifying that the system can load and select between them without breaking existing functionality.

### Tests for User Story 3 ⚠️

- [X] T021 [P] [US3] Create test case in `src/test/kotlin/.../util/StarWarsResourceLoaderTest.kt` — verify multiple icon file names can be loaded for the same lightsaber entity per FR-006
- [X] T022 [P] [US3] Add test to `src/test/kotlin/.../models/LightsabersTest.kt` (new) — verify Lightsabers data class supports multiple icon variations without schema changes

### Implementation for User Story 3

- [X] T023 [US3] Extend `Lightsabers` data class in `src/main/kotlin/.../models/Lightsabers.kt` to support multiple icon file names per lightsaber entity — add field for icon variation list; default value preserves existing behavior
- [X] T024 [US3] Update `src/main/kotlin/.../util/StarWarsResourceLoader.kt` to load and cache icons from multiple sources for the same logical lightsaber — added `resolveLightsaberIconPath()` and `getLightsaberHiltIcon()` methods; uses Guava cache per Constitution Principle V
- [X] T025 [US3] Ensure blade geometry and color properties remain consistent across icon variations in `src/main/kotlin/.../ui/LightsaberLayoutCalculator.kt` — variation affects visual appearance only, not rendering logic (Blade.color and Blade geometry properties used directly)

**Checkpoint**: All user stories should now be independently functional — foundational architecture supports future icon variation expansion.

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories and edge case handling

- [X] T026 [P] Implement narrow width fallback in `src/main/kotlin/.../ui/LightsaberLayoutCalculator.kt` — when progress bar width < hilt icon width, hide hilt and display only blade geometry per Edge Cases (verified: both computeVariantBladeLayout and computeJarKaiVariantLayout check `width >= iconWidth`)
- [X] T027 [P] Implement `showIcon` disabled behavior for jar-kai variants in `src/main/kotlin/.../ui/LightsaberProgressBarDecorator.kt` — display only blade geometry (no hilt) when `showIcon` is false, consistent with current behavior per Edge Cases (verified: both methods check `showIcon && width >= iconWidth`)
- [X] T028 Verify multi-color blade handling in `src/main/kotlin/.../ui/LightsaberLayoutCalculator.kt` — selected individual blade retains its original color (`Blade.color` property used directly; no color override) per Edge Cases (verified: drawBlade uses Blade.color through bladePaint() and bladeGlow())
- [X] T029 [P] Update `src/test/kotlin/.../configuration/StarWarsStateTests.kt` — test variant state persistence and default value initialization for V2 readiness (added enableLightsaberVariants to assertDefaultValues and values should be updated on set tests)
- [X] T030 Run quickstart.md validation scenarios 1-7 in sandbox IDE using `./gradlew runPlugin` — **manual validation required** (see checklist below)
- [X] T031 Verify backward compatibility — existing double-bladed and jar-kai displays continue to work without requiring user configuration changes per FR-007; entities without declared variants render with default CombinedId behavior (verified: all 787 tests pass, including StarWarsResourceBundleTests)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies — can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion — BLOCKS all user stories
- **User Stories (Phase 3+)**: All depend on Foundational phase completion
  - User stories can then proceed in parallel (if staffed)
  - Or sequentially in priority order (P1 → P2)
- **Polish (Final Phase)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Foundational (Phase 2) — No dependencies on other stories
- **User Story 2 (P1)**: Can start after Foundational (Phase 2) — Shares layout calculator extension with US1 but independently testable
- **User Story 3 (P2)**: Can start after Foundational (Phase 2) — May integrate with US1/US2 rendering logic but should be independently testable

### Within Each User Story

- Tests (if included) MUST be written and FAIL before implementation
- Models before services
- Services before endpoints
- Core implementation before integration
- Story complete before moving to next priority

### Parallel Opportunities

- All Setup tasks marked [P] can run in parallel
- All Foundational tasks marked [P] can run in parallel (within Phase 2) — specifically T005 (bundle files) and T006/T007 (model/state changes)
- Once Foundational phase completes, all user stories can start in parallel (if team capacity allows)
- All tests for a user story marked [P] can run in parallel
- Models within a story marked [P] can run in parallel
- Different user stories can be worked on in parallel by different team members

---

## Parallel Example: User Story 1

```bash
# Launch all tests for User Story 1 together:
Task: "Create LightsaberLayoutCalculatorTest.kt — test single-blade variant layout calculation"
Task: "Create BladeVariantSelectorTest.kt — test random variant selection logic"

# Launch all models for User Story 1 together:
Task: "Add global toggle checkbox to settings UI in UiOptionsPanel.kt"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup (CombinedId data class, BladeSelectionType enum)
2. Complete Phase 2: Foundational (settings, state persistence, bundle keys, entity variants field with List<List<CombinedId>>) — CRITICAL - blocks all stories
3. Complete Phase 3: User Story 1 (double-bladed variant selection and hilt positioning via CombinedId references)
4. **STOP and VALIDATE**: Test User Story 1 independently using quickstart.md Scenario 2
5. Deploy/demo if ready

### Incremental Delivery

1. Complete Setup + Foundational → Foundation ready
2. Add User Story 1 → Test independently → Deploy/Demo (MVP!)
3. Add User Story 2 → Test independently → Deploy/Demo
4. Add User Story 3 → Test independently → Deploy/Demo
5. Each story adds value without breaking previous stories

### Parallel Team Strategy

With multiple developers:

1. Team completes Setup + Foundational together
2. Once Foundational is done:
   - Developer A: User Story 1 (double-bladed variants)
   - Developer B: User Story 2 (jar-kai variants)
   - Developer C: User Story 3 (icon variation architecture)
3. Stories complete and integrate independently

---

## Notes

- [P] tasks = different files, no dependencies
- [Story] label maps task to specific user story for traceability
- Each user story should be independently completable and testable
- Verify tests fail before implementing
- Commit after each task or logical group
- Stop at any checkpoint to validate story independently
- Avoid: vague tasks, same file conflicts, cross-story dependencies that break independence
- **V1 scope**: Global toggle only; per-lightsaber multiselect UI deferred to V2 (FR-002/FR-002b)
- **Design change**: Variants declared as `List<List<CombinedId>>` in factions.json — explicit references, no string parsing needed; CombinedId contains `(lightsaberId, bladeId)` for direct lightsaber/blade lookup
