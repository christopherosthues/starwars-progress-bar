# Rendering Contract: Blade Variant Display

**Date**: 2026-11-14  
**Branch**: `001-lightsaber-blade-variants`

## Interface Definition

### Layout Calculation API Extension

**Component**: `LightsaberLayoutCalculator` (existing object in `ui/`)

**Variant Selection Source**: *V1: Selected variant INDEX is determined by system-managed random selection from the entity's declared `variants` list in factions.json; when toggle disabled or single variant, first/default variant (index 0) renders*

**New Methods Required**:

```kotlin
// Compute layout for a selected variant using explicit CombinedId references
fun computeVariantLayout(
    lightsabers: Lightsabers,     // Parent entity with variants list of List<CombinedId>
    selectedVariantIndex: Int,    // Index into lightsabers.variants list — system-selected randomly or default (0)
    iconWidths: List<Int>,
    width: Int,
    amountFull: Int,
    showIcon: Boolean,
): List<LightsaberLayout>         // Returns layout for all CombinedIds in the selected variant
```

**Return Type**: `List<LightsaberLayout>` — existing data class containing `List<BladeGeometry>` and optional `HiltGeometry` per lightsaber hilt in the variant

**Key Change from Previous Design**: Variants are now explicit lists of `CombinedId(lightsaberId, bladeId)` references rather than string labels. The rendering code uses CombinedIds directly — **no string parsing required**. Each variant index maps to a list of CombinedIds that render together (e.g., full weapon = all blades; individual variant = single blade).

### Rendering Flow

1. `BladeVariantSelector` randomly selects a variant INDEX from `lightsabers.variants.size()` when toggle enabled
2. Selected variant's `List<CombinedId>` is passed to layout calculator
3. For each CombinedId in the list:
   - Look up lightsaber by `lightsaberId` (1-based index into `lightsabers.lightsabers`)
   - Look up blade by `bladeId` (0-based index into that lightsaber's `blades`)
   - Compute layout for that specific blade with correct hilt positioning

**Example — Darth Maul double-bladed, variant 2 (right blade only)**:
- Selected variant: `[[1, 0]]` (one CombinedId)
- Rendering: Look up lightsaber #1 → look up blade index 0 → compute single-blade layout with hilt at left edge per FR-003</parameter>
</function>
</tool_call>
<tool_call>
<function=edit>
<parameter=path>
D:\home\git\starwars-progress-bar\specs\001-lightsaber-blade-variants\contracts\rendering-contract.md

### Hilt Positioning Rules

#### Double-Bladed Single Blade Variant (FR-003, FR-004)

When a variant contains only one CombinedId from a double-bladed lightsaber:

| CombinedId | Hilt X Position | Blade Extension Direction |
|------------|-----------------|---------------------------|
| `[lightsaberId, 0]` (first/left blade) | Right edge of progress bar (`x = width - iconWidth`) | Extends leftward from hilt (backward) |
| `[lightsaberId, 1]` (second/right blade) | Left edge of progress bar (`x = 0`) | Extends rightward from hilt (forward) |

**Visual Result**: 
- First/left blade variant: Hilt at end, blade extends to the left — behaves like an even-numbered jar-kai blade
- Second/right blade variant: Hilt at start, blade extends to the right — behaves like an odd-numbered jar-kai or single-blade saber

**Note**: Blade index determines positioning — `bladeId = 0` → hilt at right edge (backward); `bladeId = 1` → hilt at left edge (forward). The xBlade shift is applied relative to the hilt emitter: left edge of the hilt for backward blades, right edge for forward blades. No string parsing needed; rendering code reads CombinedId directly.

#### Jar-Kai Single Blade (FR-005)

When a variant contains only one CombinedId from a jar-kai configuration:

**Space Allocation**: Full available width minus only the selected blade's hilt length — matching single-blade behavior exactly.

**Formula**:
```
bladeWidth = progressAmount * (width - hiltIconWidth) / width
hiltX = if (lightsaber.id.isOdd()) 0 else width - hiltIconWidth
```

**Comparison to Current Jar-Kai Rendering**:
- **Current (full variant)**: Width shared between multiple hilts; each blade gets reduced space
- **New (single CombinedId variant)**: Full width minus one hilt; selected blade uses complete available space

**Note**: The rendering code reads the CombinedId directly — `lightsaberId` identifies which hilt to render, `bladeId` identifies which blade from that hilt. No string parsing needed.

### Variant Selection at Runtime

**Selector Integration**: `BladeVariantSelector` (new class in `selectors/`)

**Selection Logic**:
1. When entity selector chooses a lightsaber, read the entity's declared variants from `Lightsabers.variants: List<List<CombinedId>>`
2. If `enableBladeVariants = false` OR entity has only one variant → render first/default variant (index 0) — current behavior preserved
3. If `enableBladeVariants = true` AND entity has multiple variants → randomly select one **variant index** from the declared list
4. Selected variant index persists until next entity is chosen by selector

**Random Selection Rules**:
- Applies to all selector strategies (random, rolling, in-order, reverse) — per spec clarification
- Variant selected once per lightsaber entity selection event
- No explicit frequency setting required — variant changes only when new entity is selected
- **Content author control**: Each entity's `variants` list determines what display modes are available; prevents awkward combinations by design (e.g., jar-kai with one double-blade showing both blades and another showing only one)
- **No string parsing**: Selected variant index maps directly to a `List<CombinedId>` — rendering code uses CombinedIds for lightsaber/blade lookup</parameter>
</function>
</tool_call>
<tool_call>
<function=edit>
<parameter=path>
D:\home\git\starwars-progress-bar\specs\001-lightsaber-blade-variants\contracts\rendering-contract.md

### Edge Case Rendering Behavior

#### Multi-Color Blades (Edge Cases)

**Rule**: Selected blade retains its original color from `Blade.bladeColor`

**Example**: Double-bladed saber with red left blade and blue right blade:
- Variant with CombinedId `[1, 0]` → displays only the first blade (red) with correct geometry
- Variant with CombinedId `[1, 1]` → displays only the second blade (blue) with correct geometry</parameter>
</function>
</tool_call>
<tool_call>
<function=edit>
<parameter=path>
D:\home\git\starwars-progress-bar\specs\001-lightsaber-blade-variants\contracts\rendering-contract.md

#### Narrow Progress Bar Width (Edge Cases)

**Threshold**: When `width - hiltIconWidth < minimumBladeLength` (approximately 50px)

**Behavior**: Hide hilt icon, display only blade geometry to maximize visible blade length

**Implementation**: 
```kotlin
if (showIcon && width >= iconWidth + MINIMUM_BLADE_LENGTH) {
    // Normal rendering with hilt and blade
} else {
    // Blade-only rendering (hilt hidden)
    return LightsaberLayout(listOf(computeNoHiltBlade(...)), null)
}
```

#### `showIcon` Disabled (Edge Cases)

**Rule**: When `StarWarsState.showIcon = false`, jar-kai individual blade variants display only blade geometry (no hilt), consistent with current behavior for all lightsaber types.

**Implementation**: Existing `computeNoHiltBlade()` method handles this case; no new logic required.

#### Missing Icon Resources (Edge Cases)

**Rule**: Display blade geometry without hilt icon on missing resource — graceful degradation maintains visual continuity

**Fallback**: 
```kotlin
try {
    val hiltIcon = StarWarsResourceLoader.getIcon(fileName)
    // Render with hilt
} catch (e: Exception) {
    // Render blade only, hilt = null
    return LightsaberLayout(listOf(bladeGeometry), null)
}
```

## Performance Constraints

Per Constitution Principle V (Simplicity & EDT Performance):

1. **Variant selection**: O(1) random choice from Set — runs once per entity selection event, not per frame
2. **Layout calculation**: New methods reuse existing slot computation logic; no additional I/O or allocation-heavy work
3. **Icon loading**: Uses cached resources via `StarWarsResourceLoader`; ad-hoc per-frame loading forbidden

## Visual Consistency Requirements

| Scenario | Expected Visual Result | Tolerance |
|----------|------------------------|-----------|
| Single CombinedId variant from double-bladed saber (blade index 0, backward) | Matches even-numbered jar-kai blade rendering | Hilt position within 2px of right edge |
| Single CombinedId variant from double-bladed saber (blade index 1, forward) | Matches odd-numbered jar-kai / single-blade saber rendering | Hilt position within 2px of left edge |
| Jar-kai single CombinedId variant | Identical space allocation to any single-blade saber | Width difference ≤ 5px (SC-003) |
| Narrow width fallback | Blade visible, hilt hidden | No clipping or overflow at widths < 100px |

## References

- [Data Model](../data-model.md) — entity definitions and CombinedId format
- [Settings UI Contract](./settings-ui-contract.md) — user selection interface behavior
- Feature Specification FR-003 through FR-009 — hilt positioning and rendering requirements
