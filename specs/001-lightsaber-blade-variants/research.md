# Research: Lightsaber Blade Variants

**Date**: 2026-11-14  
**Branch**: `001-lightsaber-blade-variantss`

## Technical Decisions

### Decision 1: Data Model Extension Approach *(Updated)*

**Decision**: Extend existing `Lightsabers` data class with a `variants: List<String>` field that declares allowed display modes; `Blade` and `Lightsaber` classes remain **unchanged**. No per-blade variant tracking needed.

**Rationale**: 
- The current model already supports multi-blade lightsabers via `isDoubleBladed` flag and `List<Blade>` in the `Lightsaber` class
- Adding a `variants` field to `Lightsabers` gives content authors control over what display modes look good for each entity — prevents awkward combinations (e.g., jar-kai with one double-blade showing both blades and another showing only one)
- The default value `listOf("full")` preserves existing behavior where the full weapon displays as currently implemented
- No `variantId` field needed in Blade class; no `lightsaberVariantSelections` map needed in StarWarsState

**Alternatives considered**:
- Per-blade `variantId` field — derived from blade structure; less flexible than entity-declared variants (REJECTED per user feedback)
- Separate `BladeVariant` data class — adds indirection without benefit; existing Blade class is sufficient
- User selection storage map — V1 uses system-managed random selection; deferred to V2

### Decision 2: Variant Declaration Format *(Updated)*

**Decision**: Each `Lightsabers` entity in factions.json declares its allowed display modes via a `variants` list field. Valid values include `"full"` (default, current behavior), `"right_blade"`, `"left_blade"` for double-bladed sabers, and `"first_blade"`, `"second_blade"` for jar-kai configurations.

**Rationale**:
- Content authors control what looks good — explicit declaration prevents awkward combinations
- Default value `listOf("full")` ensures backward-compatible deserialization of existing factions.json entries
- Simple string list is easy to extend for future variant types (e.g., icon variations)
- Rendering system reads from entity's declared variants; no user selection storage needed in V1

**Alternatives considered**:
- Derived variants from blade structure — automatic but less flexible; content authors have no control over what combinations look good (REJECTED per user feedback)
- Enum-based variant types — more type-safe but harder to extend without code changes; string list is simpler for JSON configuration

### Decision 3: State Storage Architecture *(Updated)*

**Decision**: Add only `enableBladeVariants: Boolean` field to StarWarsState; no per-entity variant selection map needed. Variant pool comes from entity's declared `variants` list in factions.json at runtime.

**Rationale**:
- *V1 scope*: Single toggle controls feature globally; random selection reads from entity-declared variants
- Simplifies state storage — only one new field required (follows add-setting skill workflow)
- No persistent user selection needed for V1 system-managed random selection
- Per-entity multiselect storage deferred to V2 if needed

**Alternatives considered**:
- `lightsaberVariantSelections: MutableMap<String, Set<String>>` — provides per-entity user selections; adds complexity without V1 value (REJECTED per user feedback)
- Separate variant configuration file — overkill for runtime selection from factions.json data model</parameter>
</function>
</tool_call>
<tool_call>
<function=edit>
<parameter=path>
D:\home\git\starwars-progress-bar\specs\001-lightsaber-blade-variants\research.md

### Decision 4: Layout Calculation Extension

**Decision**: Add new methods to `LightsaberLayoutCalculator` for computing single-blade variant layouts, reusing existing slot computation logic.

**Rationale**:
- Current `computeDoubleBladedLayout()` and `computeSingleBladedLayout()` provide templates for hilt positioning
- New methods can extract individual blade geometry from the existing Blade data class
- Hilt repositioning (left edge for right blade, right edge for left blade) follows FR-003/FR-004

**Alternatives considered**:
- Create separate calculator class — duplicates logic; extension methods keep related code together
- Modify existing compute() method with variant parameter — increases complexity of already complex layout logic

### Decision 5: Random Variant Selection Timing *(Updated)*

**Decision**: Select random variant when the lightsaber entity is chosen by the selector, persisting until next entity selection. The random pool contains only variants declared in the entity's `variants` list from factions.json. No explicit frequency setting required per spec clarification.

**Rationale**:
- Applies to all selector strategies (random, rolling, in-order, reverse) independently of entity selection strategy
- Entity-declared variants control what display modes are available — content authors prevent awkward combinations by design
- Simple implementation: variant selected once per entity selection event from declared list
- When toggle disabled or single variant declared, first/default variant renders automatically

**Alternatives considered**:
- Per-frame random selection — too frequent; causes visual flickering
- Separate frequency setting — adds UI complexity without clear user value (per spec clarification)
- Auto-derived variants from blade structure — less flexible than entity-declared list (REJECTED per user feedback)</parameter>
</function>
</tool_call>
<tool_call>
<function=edit>
<parameter=path>
D:\home\git\starwars-progress-bar\specs\001-lightsaber-blade-variants\research.md

## Existing Patterns Identified

### Setting Pattern (add-setting skill workflow)

New settings require updates to:
1. `ConfigurationConstants.kt` — default constant definition
2. `BundleConstants.kt` — bundle key constant
3. Every bundle file (`StarWarsBundle*.properties`) — translation strings
4. `StarWarsState.kt` — field declaration, `copy()` method, `isModified()` check
5. UI panel under `configuration/components/` — checkbox/radio button wiring

### Content Pattern (add-lightsaber skill workflow)

New lightsaber content requires:
1. `factions.json` entry with complete blade definitions
2. Icon files in both required locations (LFS-tracked PNGs)
3. Translation keys in every bundle file
4. Consistency enforced by `IconResourceTests` and `StarWarsResourceBundleTests`

### Selector Pattern

Selectors implement `IStarWarsSelector.selectEntity()` returning a `StarWarsEntity`:
- Determinate selectors compute based on progress amount
- Indeterminate selectors cycle through entities continuously
- Entity selection type (vehicles only, lightsabers only, all) filters available pool

## API Compatibility Verification

**Platform Version**: 2026.2  
**Since Build**: 262  

All APIs used in the feature exist since build 262:
- `ThreeStateCheckBox` — part of IntelliJ UI utilities since early platform versions
- `GridBagLayout`, `JCheckBox` — standard Swing components available on all platforms
- `MutableMap`, `Set` — Kotlin stdlib (bundled with platform)
- kotlinx.serialization — already used for factions.json parsing

## Performance Analysis

**EDT Impact**: Variant selection runs once per entity selection event, not per frame:
- Random variant choice is O(1) set operation
- Layout calculation adds minimal overhead (new methods compute geometry from existing data)
- Icon loading uses cached resources via `StarWarsResourceLoader`

**Memory Impact**: 
- New state fields add negligible memory overhead (Map + Set structures)
- Variant selection data persists in StarWarsState XML storage (~100 bytes per lightsaber entity)
