# Quickstart Validation Guide: Lightsaber Blade Variants

**Date**: 2026-11-14  
**Branch**: `001-lightsaber-blade-variants`

## Prerequisites

1. **Feature branch checked out**: `git checkout 001-lightsaber-blade-variants`
2. **Java 21+ installed**: Verified by Gradle build
3. **Gradle wrapper available**: `./gradlew` (Unix/macOS) or `.\gradlew.bat` (Windows)
4. **Dependencies resolved**: Run `./gradlew dependencies` to verify platform SDK and kotlinx.serialization are available

## Setup Commands

### 1. Build the plugin

```bash
./gradlew buildPlugin
```

**Expected outcome**: Plugin ZIP created at `build/distributions/starwars-progress-bar-<version>.zip`; no compilation errors related to new blade variant classes.

### 2. Run tests

```bash
./gradlew check
```

**Expected outcome**: All existing tests pass (backward compatibility verified); new tests for blade variant selection, layout calculation, and state persistence also pass.

## Validation Scenarios

### Scenario 1: Global Toggle Setting *(V1)*

**Setup**: 
1. Launch sandbox IDE with `./gradlew runPlugin`
2. Navigate to **Settings/Preferences > Appearance & Behavior > Star Wars Progress Bar**

**Validation Steps**:
1. Verify new "Enable blade variants" checkbox appears in UI Options section
2. Checkbox is checked by default (per FR-001)
3. When unchecked, progress bar displays full weapon for multi-blade lightsabers (current behavior)
4. When checked, random variant selection activates automatically — no per-lightsaber subcheckboxes visible *(V1 scope)*

**Expected Outcome**: Global toggle controls blade variant feature; default state is enabled with system-managed random selection active.

### Scenario 2: Double-Bladed Lightsaber Random Variant Display *(V1)*

**Setup**:
1. Ensure "Enable blade variants" is checked
2. Configure a selector that will choose a double-bladed lightsaber entity with multiple declared variants (e.g., Darth Maul's saber under Sith faction — must have `variants: [[[1,0],[1,1]], [[1,0]], [[1,1]]]` in factions.json)

**Validation Steps**:
1. Trigger multiple build operations until the selector chooses the double-bladed lightsaber
2. Observe that the display randomly shows either: full weapon (both blades), first blade only, or second blade only — **only if entity has declared variants**
3. When first blade displays (CombinedId `[1,0]`), verify hilt is positioned at left edge (per FR-003)
4. When second blade displays (CombinedId `[1,1]`), verify hilt is positioned at right edge (per FR-004)

**Expected Outcome**: Random variant selection uses entity's declared variants list; hilt repositioning is visually correct per SC-002. Entities without multiple declared variants render with default CombinedId behavior only.

### Scenario 3: Jar-Kai Individual Blade Rendering *(V1)*

**Setup**:
1. Ensure "Enable blade variants" is checked
2. Configure a selector that will choose a jar-kai lightsaber entity with multiple declared variants (e.g., Ahsoka Tano's sabers under Jedi faction — must have `variants: [[[1,0],[2,0]], [[1,0]], [[2,0]]]` in factions.json)

**Validation Steps**:
1. Trigger multiple build operations until the selector chooses the jar-kai lightsaber
2. When an individual blade variant randomly displays (single CombinedId), measure rendered blade width — should equal full available width minus hilt length only (per FR-005, SC-003)
3. Compare to single-blade lightsaber rendering — space allocation should match within 5 pixels

**Expected Outcome**: Jar-kai individual blade uses full-width rendering like single-blade sabers; visual consistency confirmed via random selection from entity's declared variants list.</parameter>
</function>
</tool_call>
<tool_call>
<function=edit>
<parameter=path>
D:\home\git\starwars-progress-bar\specs\001-lightsaber-blade-variants\quickstart.md

### Scenario 4: Random Variant Selection Persistence *(V1)*

**Setup**:
1. Ensure "Enable blade variants" is checked
2. Configure selector to choose a multi-blade lightsaber entity with declared variants repeatedly

**Validation Steps**:
1. Trigger multiple build operations with the same lightsaber selected
2. Observe that once a variant INDEX is randomly chosen, it persists until the next entity is selected by the selector (per FR-008)
3. Verify that both full weapon display AND individual blade variants appear in the random pool — **only from entity's declared variants list**

**Expected Outcome**: Random variant selection works across all selector strategies; variant INDEX persists correctly until next entity selection event. Selection is limited to variant indices declared for each entity.

### Scenario 7: Backward Compatibility *(V1)*

**Setup**:
1. Ensure "Enable blade variants" is checked (or unchecked)
2. Configure a selector that will choose any lightsaber entity WITHOUT multiple declared variants (e.g., single-blade sabers, or entities where `variants` field uses default value `listOf(listOf(CombinedId(1, 0)))`)

**Validation Steps**:
1. Trigger build operations until the selector chooses such an entity
2. Verify that the display renders exactly as currently implemented — full weapon with all blades visible
3. No visual changes compared to pre-feature behavior

**Expected Outcome**: Entities without multiple declared variants render identically to current behavior; backward compatibility maintained per FR-007. Default CombinedId variant preserves existing rendering logic.</parameter>
</function>
</tool_call>
<tool_call>
<function=edit>
<parameter=path>
D:\home\git\starwars-progress-bar\specs\001-lightsaber-blade-variants\quickstart.md

### Scenario 5: Narrow Progress Bar Width Fallback

**Setup**:
1. Select individual blade variant for any lightsaber
2. Resize IDE window to make progress bar very narrow (< 100px width)

**Validation Steps**:
1. Trigger progress bar display
2. Verify hilt icon is hidden, only blade geometry displays (per Edge Cases)
3. Blade length maximizes visible space without hilt

**Expected Outcome**: Graceful degradation at narrow widths; visual continuity maintained.

### Scenario 6: `showIcon` Disabled Behavior

**Setup**:
1. Uncheck "Show icon in progress bar" setting
2. Select jar-kai individual blade variant

**Validation Steps**:
1. Trigger progress bar display
2. Verify only blade geometry renders (no hilt) — consistent with current behavior when `showIcon` is disabled (per Edge Cases)

**Expected Outcome**: Jar-kai variants respect existing `showIcon` setting; rendering matches single-blade behavior.

## Test Commands

### Unit Tests

```bash
./gradlew test --tests "com.christopherosthues.starwarsprogressbar.selectors.BladeVariantSelectorTest"
./gradlew test --tests "com.christopherosthues.starwarsprogressbar.ui.LightsaberLayoutCalculatorTest"
./gradlew test --tests "com.christopherosthues.starwarsprogressbar.configuration.StarWarsStateTest"
```

**Expected outcome**: All tests pass; coverage includes variant selection logic, layout calculation for single-blade variants, and state persistence.

### Integration Tests

```bash
./gradlew runPlugin
```

Then manually validate scenarios 1-6 above in the sandbox IDE.

## Success Criteria Verification *(V1)*

| Criterion | Validation Method | Pass Condition |
|-----------|-------------------|----------------|
| **SC-001**: Single checkbox enables/disables feature | Scenario 1, Step 1-4 | User can enable/disable blade variant selection with one mouse click; when enabled, random selection activates automatically for entities with declared variants — *no per-entity clicks required* |
| **SC-002**: Hilt positioning visually correct | Scenario 2, Step 3-4 | No visible misalignment between hilt and blade attachment points across all widths |
| **SC-003**: Jar-kai matches single-blade within 5px | Scenario 3, Step 2-3 | Measured width difference ≤ 5 pixels for progress bar widths from 100px to 2000px |
| **SC-004**: Data model supports max 4 lightsabers × max 2 blades | Code review + test | `Lightsabers.lightsabers` list accepts up to 4 items; each `Lightsaber.blades` accepts up to 2 items without schema changes |
| **FR-007**: Backward compatibility maintained | Scenario 7, Step 1-3 | Entities without multiple declared variants render identically to current behavior; default `"full"` variant preserves existing rendering logic |

## Rollback Procedure

If validation fails:

1. **Revert code changes**: `git revert HEAD` (or specific commit)
2. **Verify existing functionality**: Run `./gradlew check` to confirm all pre-existing tests pass
3. **Debug**: Use `./gradlew runPlugin` with debug attached to inspect state and rendering

## References

- [Feature Specification](./spec.md) — complete requirements and acceptance criteria
- [Implementation Plan](./plan.md) — technical architecture and project structure
- [Data Model](./data-model.md) — entity definitions and validation rules
- [Research Notes](./research.md) — technical decisions and rationale
- [Rendering Cases](./cases.md) — complete reference for all rendering paths, hilt positioning rules, xBlade anchor computation, and edge case behavior
