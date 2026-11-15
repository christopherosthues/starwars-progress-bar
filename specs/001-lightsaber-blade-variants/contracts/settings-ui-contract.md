# Settings UI Contract: Blade Variant Selection

**Date**: 2026-11-14  
**Branch**: `001-lightsaber-blade-variants`

## Interface Definition

### Global Toggle Control *(V1)*

**Location**: Settings/Preferences > Appearance & Behavior > Star Wars Progress Bar > UI Options panel

**Control Type**: Standard checkbox (`JCheckBox`)

**Label**: Localized string from bundle key `configuration.enableBladeVariants`

**Default State**: Checked (enabled) — per FR-001 and spec clarification

**Behavior**:
- When **checked**: System-managed random variant selection activates automatically for all multi-blade lightsabers; the progress bar randomly displays either the full weapon OR an individual blade variant *(V1: no user-facing subcheckboxes)*
- When **unchecked**: Only current lightsaber setting applies (full weapon renders as currently implemented)
- State persists across IDE sessions via `StarWarsState.enableBladeVariants`

### Multiselect Variant Panel *(Deferred to V2)*

**Location**: Nested within `LightsaberFactionPanel`, under each individual lightsaber checkbox *(V2)*

**Control Type**: Group of checkboxes (`JCheckBox`) — no icons, no repeated lightsaber names per FR-002

**Structure by Lightsaber Type**:

| Lightsaber Type | Available Options | Default Selection |
|-----------------|-------------------|-------------------|
| Single-blade | "Both/all" (only) | Checked |
| Double-bladed | "Both/all", "Left blade", "Right blade" | All checked |
| Jar-kai (N hilts) | "All", "First blade", "Second blade", ... (up to N blades) | All checked |

**Label Keys**: Bundle keys follow pattern `lightsaber.variant.{type}`:
- `lightsaber.variant.all` — "Both/all" or "All" depending on context
- `lightsaber.variant.left` — "Left blade"
- `lightsaber.variant.right` — "Right blade"
- `lightsaber.variant.first` — "First blade"
- `lightsaber.variant.second` — "Second blade"

**Behavior**:
1. **Parent lightsaber deselected → Variant UI hidden** (per FR-002b): Subcheckboxes disappear from view; selection state is preserved in memory
2. **Parent lightsaber reselected → Variant UI restored**: Previous selections are restored to checkboxes
3. **"Both/all" selected → Full weapon displays**: Current rendering behavior preserved; all blades render simultaneously
4. **Multiple individual variants selected → Random selection applies**: When the selector chooses this lightsaber, one variant is randomly picked from the user's chosen set (excluding "all")

### Selection State Persistence *(V1: default values for V2 readiness)*

**Storage Field**: `StarWarsState.lightsaberVariantSelections: MutableMap<String, Set<String>>`

**Key Format**: Lightsaber entityId (e.g., `"sith.darth_maul_first"`)

**Value Format**: Set of selected variant IDs *(V1: default value `setOf("all")`; user multiselect deferred to V2)*

**Default Value**: `setOf("all")` for all lightsaber entities — populated with default values for V2 readiness; *V1 uses system-managed random selection from full pool*

## Interaction Rules

### Click Behavior

1. **Global toggle click**: 
   - Enables/disables variant feature
   - Shows/hides subcheckbox UI immediately
   - Persists to `StarWarsState.enableBladeVariants`

2. **Parent lightsaber checkbox click**:
   - Toggles lightsaber enabled/disabled (existing behavior)
   - When deselected: hides variant subcheckboxes
   - When selected: shows variant subcheckboxes with previous selections restored

3. **Variant subcheckbox click**:
   - Adds/removes variant from selection set
   - Persists to `StarWarsState.lightsaberVariantSelections[entityId]`
   - "Both/all" can coexist with individual variants in the selection set

### State Transitions

```
Global toggle OFF  →  Variant UI hidden, selections preserved
                     ↓ (user enables)
Global toggle ON   →  Variant UI visible, defaults to all selected
                       ↓ (user modifies)
User selects subset →  Selections stored; random variant chosen at runtime
                       ↓ (parent deselected)
Parent OFF         →  Variant UI hidden, selections preserved in memory
                       ↓ (parent reselected)
Parent ON          →  Variant UI restored with previous selections
```

## Localization Requirements

Per Constitution Principle IV (Localization Completeness):

All variant labels must have translations in every supported locale file:
- `StarWarsBundle.properties` (default)
- `StarWarsBundle_en.properties` (English)
- `StarWarsBundle_de.properties` (German)
- `StarWarsBundle_es.properties` (Spanish)

**Key Pattern**: `lightsaber.variant.{variant_id}`

**Example Entries**:
```properties
# English
lightsaber.variant.all=Both/all
lightsaber.variant.left=Left blade
lightsaber.variant.right=Right blade
lightsaber.variant.first=First blade
lightsaber.variant.second=Second blade
```

## Accessibility Considerations

- Subcheckboxes use standard Swing `JCheckBox` components with keyboard navigation support (Tab, Space to toggle)
- Labels are localized and readable by screen readers
- No custom rendering — uses platform-native checkbox appearance
