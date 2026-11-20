# Data Model: Lightsaber Blade Variants

**Date**: 2026-11-14  
**Branch**: `001-lightsaber-blade-variants`

## Entity Definitions

### CombinedId (New data class)

A structured reference to a specific lightsaber and blade combination, eliminating string parsing:

```kotlin
@Serializable
internal data class CombinedId(
    val lightsaberId: Int,   // Index into Lightsabers.lightsabers list (1-based per existing convention)
    val bladeId: Int,       // Index into Lightsaber.blades list (0-based)
)
```

**Fields**:
- `lightsaberId`: 1-based index identifying which lightsaber hilt to render (matches existing `Lightsaber.id` field)
- `bladeId`: 0-based index identifying which blade from that lightsaber to display

**Validation Rules**:
- `lightsaberId` must be positive integer; maps to a valid entry in the parent entity's `lightsabers` list
- `bladeId` must be non-negative integer; maps to a valid entry in the referenced lightsaber's `blades` list
- Example: `CombinedId(1, 0)` references lightsaber #1, blade index 0 (first/left blade)

### Variant List (Extension to existing Lightsabers class)

The existing `Lightsabers` data class is extended with a structured variant declaration — each variant is an explicit list of CombinedIds:

```kotlin
@Serializable
@SerialName("Lightsabers")
internal data class Lightsabers(
    override val id: String,
    override val velocity: Float,
    val isJarKai: Boolean,
    val lightsabers: List<Lightsaber>,
    val variants: List<List<CombinedId>> = listOf(listOf(CombinedId(1, 0))),  // NEW: default single-blade for backward compatibility
) : StarWarsEntity {

    override var factionId: String = ""

    override val fileName: String
        get() = "lightsabers/" + if (factionId.isEmpty()) id else "$factionId/$id"

    override val localizationKey: String
        get() = "${BundleConstants.LIGHTSABERS}$entityId"
}
```

**Fields**:
- `variants`: List of display mode definitions; each inner list contains the CombinedIds that render together for that variant. Default value renders a single lightsaber/blade combination (backward-compatible with existing single-blade sabers). Multi-blade entities declare all desired variants explicitly.

**Example — Darth Maul's double-bladed saber**:
```json
{
    "type": "Lightsabers",
    "id": "darth_maul_first",
    "velocity": 3,
    "isJarKai": false,
    "variants": [
        [[1, 0], [1, 1]],   // Variant 1: full weapon — lightsaber #1, blades 0 and 1 (both blades)
        [[1, 0]],           // Variant 2: right blade only — lightsaber #1, blade 0
        [[1, 1]]            // Variant 3: left blade only — lightsaber #1, blade 1
    ],
    "lightsabers": [
        {
            "id": 1,
            "isDoubleBladed": true,
            "yShift": 5,
            "blades": [
                {"bladeColor": "red", "isShoto": false, "bladeSize": 8, "xBlade": 0, "yBlade": 1},
                {"bladeColor": "red", "isShoto": false, "bladeSize": 8, "xBlade": 0, "yBlade": 1}
            ]
        }
    ]
}
```

**Example — Ahsoka Tano's jar-kai sabers**:
```json
{
    "type": "Lightsabers",
    "id": "ahsoka_tano_first",
    "velocity": 3,
    "isJarKai": true,
    "variants": [
        [[1, 0], [2, 0]],   // Variant 1: full weapon — both hilts with their blades
        [[1, 0]],           // Variant 2: first hilt only
        [[2, 0]]            // Variant 3: second hilt only
    ],
    "lightsabers": [
        {"id": 1, "isDoubleBladed": false, "yShift": 0, "blades": [{"bladeColor": "green", ...}]},
        {"id": 2, "isDoubleBladed": false, "yShift": 0, "blades": [{"bladeColor": "yellow", ...}]}
    ]
}
```

**Validation Rules**:
- `variants` must contain at least one entry (default renders single lightsaber/blade)
- Each inner list represents one display mode; contains CombinedIds that render together
- Content authors declare which variants look good for each entity — prevents awkward combinations by design
- Default value ensures backward-compatible deserialization of existing factions.json entries

### Blade Class (Unchanged)

The `Blade` data class remains **unchanged** — no `variantId` field needed since variants are declared at the entity level with explicit CombinedId references:

```kotlin
@Serializable
internal data class Blade(
    val bladeColor: String,
    val isShoto: Boolean,
    val bladeSize: Int,
    val xBlade: Int,
    val yBlade: Int,
) {
    val color: JBColor
        get() = LightsaberColor.colors[bladeColor] ?: LightsaberColor.Cyan
}
```

### Variant Selection State (Extension to StarWarsState)

New field added to `StarWarsState`:

```kotlin
// Global toggle for blade variant feature — V1: single checkbox only
var enableBladeVariants: Boolean = DEFAULT_ENABLE_BLADE_VARIANTS  // default true
```

**Fields**:
- `enableBladeVariants`: Boolean toggle; when false, the first/default variant from `Lightsabers.variants` renders (current behavior preserved); *V1: single checkbox controls feature*

**State Transitions**:
1. **Global toggle enabled → System-managed random selection active**: When a lightsaber entity is selected by the selector, the system randomly chooses one variant index from that entity's declared `variants` list; *V1: no user interaction required beyond enabling toggle*
2. **Variant persists until next entity selection**: Selected variant displays consistently during long-running operations; changes only when the selector picks a new entity
3. **Random selection uses entity-declared variants only**: The random pool contains only the variant indices declared in `factions.json` for that specific lightsaber entity — content authors control what looks good
4. **Global toggle disabled → Default variant renders**: First variant from `Lightsabers.variants` (index 0) renders as currently implemented without variant options</parameter>
</function>
</tool_call>
<tool_call>
<function=edit>
<parameter=path>
D:\home\git\starwars-progress-bar\specs\001-lightsaber-blade-variants\data-model.md

## Validation Rules

### From Requirements

| Rule | Source | Implementation |
|------|--------|----------------|
| Max 4 lightsabers per entity | SC-004, Assumptions | `Lightsabers.lightsabers` list size ≤ 4 |
| Max 2 blades per individual lightsaber | SC-004, Assumptions | `Lightsaber.blades` list size ≤ 2 |
| Entity-declared variants control display modes | FR-006 | Each `Lightsabers.variants` list defines allowed rendering options using explicit CombinedId references |
| Global toggle controls variant feature (V1) | FR-001 | `enableBladeVariants` boolean; when true, random selection activates from entity's declared variants |
| Random pool uses entity-declared variants only | FR-008 | System selects randomly from `Lightsabers.variants` list indices when lightsaber is chosen by selector |
| Blade retains original color when individually selected | Edge Cases | `Blade.color` property used directly; no color override |

### Data Integrity Constraints

1. **factions.json backward compatibility**: New `variants` field added with default value that renders single-blade behavior; existing entries deserialize correctly without schema migration
2. **CombinedId validation**: Each CombinedId's `lightsaberId` must reference a valid lightsaber in the entity's list; `bladeId` must reference a valid blade in that lightsaber
3. **Content author control**: Each entity's `variants` list determines what display modes are available — prevents awkward combinations by design (e.g., jar-kai with one double-blade showing both blades and another showing only one)

## Storage Format

### Persistent State (XML)

StarWarsState fields are serialized by IntelliJ Platform's `PersistentStateComponent`:

```xml
<state version="1.0">
    <entry key="enableBladeVariants" value="true"/>
</state>
```

**Note**: No per-entity variant selection storage needed in V1 — variants are declared in factions.json, and random selection is system-managed.

### Memory Representation

- `enableBladeVariants`: Boolean field in StarWarsState instance
- Variant pool: Read from `Lightsabers.variants` at runtime; selected variant index stored temporarily for persistence until next entity selection
- **No string parsing**: CombinedId references are used directly by rendering code — lightsaber/blade lookup is O(1) list indexing

## Relationships Diagram

```
StarWarsFaction (Lightsabers)
    └── Lightsabers entity (id: "darth_maul_first", isJarKai: false)
            ├── List<Lightsaber> (max 4 items) — blade geometry unchanged
            │       └── Lightsaber (id: 1, isDoubleBladed: true)
            │               └── List<Blade> (max 2 items)
            │                       ├── Blade { bladeColor: "red" }  ← index 0
            │                       └── Blade { bladeColor: "red" }  ← index 1
            └── variants: List<List<CombinedId>> — NEW: explicit display mode declarations
                    ├── Variant 0 (full): [[1, 0], [1, 1]]   ← lightsaber #1 blades 0+1
                    ├── Variant 1 (right blade): [[1, 0]]    ← lightsaber #1 blade 0 only
                    └── Variant 2 (left blade): [[1, 1]]     ← lightsaber #1 blade 1 only

StarWarsState
    ├── lightsabersEnabled: Map<String, Boolean>  (existing)
    └── enableBladeVariants: Boolean              (new - V1 global toggle only)

BladeVariantSelector (V1: new component)
    ├── Receives: lightsaber entity + enableBladeVariants flag
    ├── Reads: entity.variants list of CombinedId lists from factions.json data model
    ├── Computes: random variant INDEX from entity's declared variants when toggle enabled
    └── Returns: selected variant index → rendering code uses CombinedIds directly (no string parsing)
```

## References

- [Rendering Contract](./contracts/rendering-contract.md) — interface definition and visual consistency requirements
- [Settings UI Contract](./contracts/settings-ui-contract.md) — user selection interface behavior
- [Rendering Cases](./cases.md) — complete reference for all rendering paths, hilt positioning rules, xBlade anchor computation, and edge case behavior
