# Implementation Plan: Lightsaber Blade Variants

**Branch**: `001-lightsaber-blade-variants` | **Date**: 2026-11-14 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `/specs/001-lightsaber-blade-variants/spec.md`

## Summary

Extend the Star Wars Progress Bar plugin to display individual blade variants from multi-bladed lightsabers (double-bladed, jar-kai) via random selection. *V1 scope*: The feature adds a global toggle setting — when enabled, the system randomly selects from each entity's declared `variants` list in factions.json. When disabled, the first/default variant renders (current behavior preserved). Hilt positioning adjusts for single-blade variants (left edge for right blade only, right edge for left blade only), and jar-kai individual blades use full available width minus hilt length like single-blade sabers. Content authors control what display modes look good by declaring allowed variants per entity. Per-lightsaber multiselect UI deferred to V2.</parameter>
</function>
</tool_call>
<tool_call>
<function=edit>
<parameter=path>
D:\home\git\starwars-progress-bar\specs\001-lightsaber-blade-variants\plan.md

## Technical Context

**Language/Version**: Kotlin with Java 21+ runtime; IntelliJ Platform API since build 262

**Primary Dependencies**: 
- `com.intellij.modules.platform` (common-platform APIs only — no IDE-specific modules)
- kotlinx.serialization for JSON parsing (`factions.json`)
- Guava cache via `StarWarsResourceLoader` for icon caching
- Swing UI components (`JCheckBox`, `ThreeStateCheckBox`, `GridBagLayout`)

**Storage**: IntelliJ Platform's `PersistentStateComponent` mechanism; settings stored in `StarWarsState` as XML-persisted fields; entity selections use `MutableMap<String, Boolean>` keyed by `entityId`

**Testing**: JUnit 5 (Jupiter) with MockK; tests mirror main package structure under `src/test/kotlin/`; existing test infrastructure includes `IconResourceTests` and `StarWarsResourceBundleTests` for content consistency

**Target Platform**: All JetBrains IDEs (IntelliJ IDEA, Android Studio, PyCharm, WebStorm, Rider, CLion, GoLand, RubyMine, RustRover, PhpStorm) — pluginSinceBuild = 262

**Project Type**: IntelliJ Platform plugin (desktop-app); Swing-based progress bar UI running on EDT

**Performance Goals**: Lightweight drawing code on EDT; no I/O or allocation-heavy work per frame; icons cached through `StarWarsResourceLoader` via Guava cache

**Constraints**: 
- Platform portability: only common-platform APIs available at runtime
- API compatibility: every used API must exist since build 262
- ktlint (IntelliJ IDEA style): 4-space indent, LF line endings, UTF-8, max line length 120
- detekt static analysis; baselines must not grow

**Scale/Scope**: 
- 150+ vehicles, 20+ lightsabers in factions.json
- Max 4 lightsabers per entity (jar-kai configurations)
- Max 2 blades per individual lightsaber weapon (double-bladed sabers are the most complex case)
- Max 8 total blades across all hilts in a single Lightsabers entity

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Evidence |
|-----------|--------|----------|
| **I. Platform Portability** | ✅ Pass | Feature uses only common-platform APIs (Swing UI, serialization); no IDE-specific modules introduced |
| **II. Content Integrity** | ✅ Pass | New blade variant fields added to `factions.json` with default values; icon resources follow existing naming convention; translations added to all 4 bundle files (`_en`, `_de`, `_es`, default) |
| **III. Quality Gates** | ✅ Pass | New code follows ktlint style (IntelliJ IDEA); JUnit 5 tests mirror package structure; no new lint/detekt violations expected |
| **IV. Localization Completeness** | ✅ Pass | All variant labels use bundle keys (`lightsaber.variant.*`) added to every supported locale file per `configuration/Language.kt` |
| **V. Simplicity & EDT Performance** | ✅ Pass | Variant selection logic runs once per entity selection (not per frame); rendering uses cached icons; no ad-hoc per-frame loading |

## Project Structure

### Documentation (this feature)

```text
specs/001-lightsaber-blade-variants/
├── plan.md              # This file (/speckit.plan command output)
├── research.md          # Phase 0 output (/speckit.plan command)
├── data-model.md        # Phase 1 output (/speckit.plan command)
├── quickstart.md        # Phase 1 output (/speckit.plan command)
├── contracts/           # Phase 1 output (/speckit.plan command)
└── tasks.md             # Phase 2 output (/speckit.tasks command - NOT created by /speckit.plan)
```

### Source Code (repository root)

```text
src/main/kotlin/com/christopherosthues/starwarsprogressbar/
├── configuration/
│   ├── components/
│   │   └── LightsaberFactionPanel.kt      # Modified: add global toggle checkbox *(V1)*
│   ├── StarWarsState.kt                   # Modified: add enableBladeVariants field only — no per-entity selection map needed (variants declared in factions.json)
│   └── StarWarsPersistentStateComponent.kt # Modified: persist new state fields
├── constants/
│   ├── ConfigurationConstants.kt          # Modified: add DEFAULT_ENABLE_BLADE_VARIANTS
│   └── BundleConstants.kt                # Modified: add variant bundle keys (for future V2 labels)
├── models/
│   ├── Lightsabers.kt                     # Modified: add variants field — declares allowed display modes per entity *(NEW design)*
│   ├── LightsaberVariant.kt               # New: data class or enum for variant type definitions
│   └── BladeSelectionType.kt              # New: enum for variant selection modes
├── selectors/
│   └── BladeVariantSelector.kt            # New: selects random variant from entity's declared variants list *(reads from Lightsabers.variants)*
├── ui/
│   ├── LightsaberLayoutCalculator.kt      # Modified: add single-blade variant layout methods
│   └── LightsaberLayout.kt                # Modified: support variant-specific geometry
└── util/
    └── StarWarsResourceLoader.kt          # Modified (if needed): load individual blade icons

src/main/resources/
├── json/factions.json                     # Modified: add variants field to Lightsabers entries — content authors declare allowed display modes
├── messages/
│   ├── StarWarsBundle.properties          # Modified: add variant keys *(V2 labels)*
│   ├── StarWarsBundle_en.properties       # Modified: add English translations *(V2 labels)*
│   ├── StarWarsBundle_de.properties       # Modified: add German translations *(V2 labels)*
│   └── StarWarsBundle_es.properties       # Modified: add Spanish translations *(V2 labels)*
└── icons/                                 # New blade variant icons (LFS-tracked)

src/test/kotlin/com/christopherosthues/starwarsprogressbar/
├── configuration/
│   └── StarWarsStateTest.kt               # New: test variant state persistence
├── selectors/
│   └── BladeVariantSelectorTest.kt        # New: test random variant selection logic *(V1)*
├── ui/
│   └── LightsaberLayoutCalculatorTest.kt  # Modified: add single-blade variant tests
└── util/
    └── StarWarsResourceLoaderTest.kt      # Modified (if needed): blade icon loading tests
```

**Structure Decision**: The feature extends existing packages following the established patterns:
- *V1 scope*: Global toggle checkbox added to existing settings panel; no separate BladeVariantPanel component required
- **Entity-declared variants**: `Lightsabers.variants` field in factions.json declares allowed display modes per entity — content authors control what looks good (NEW design)
- New model classes in `models/` for variant definitions *(system-managed, no user-facing selection in V1)*
- New selector in `selectors/` reads from entity's declared variants list; random selection limited to declared options only
- Layout calculation extended in `ui/LightsaberLayoutCalculator.kt` with new methods for single-blade variants
- State fields simplified — only `enableBladeVariants: Boolean` needed (no per-entity selection map); follows the `add-setting` skill workflow</parameter>
</function>
</tool_call>
<tool_call>
<function=edit>
<parameter=path>
D:\home\git\starwars-progress-bar\specs\001-lightsaber-blade-variants\plan.md

## Complexity Tracking

> **Fill ONLY if Constitution Check has violations that must be justified**

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| *(None — all principles satisfied)* | | |
