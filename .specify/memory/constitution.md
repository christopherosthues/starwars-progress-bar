<!-- Sync Impact Report — temporary scratch for human review; remove before commit.
Version change: unversioned template → 1.0.0 (initial adoption)
Modified principles: none renamed; all five placeholder slots filled in
  - [PRINCIPLE_1_NAME] → I. Platform Portability (NON-NEGOTIABLE)
  - [PRINCIPLE_2_NAME] → II. Content Integrity (Single Source of Truth)
  - [PRINCIPLE_3_NAME] → III. Quality Gates Are Non-Negotiable
  - [PRINCIPLE_4_NAME] → IV. Localization Completeness
  - [PRINCIPLE_5_NAME] → V. Simplicity & EDT Performance
Added sections: Compatibility & Versioning, Development Workflow (replacing placeholder section names); Governance content filled in
Removed sections: none
Deferred TODOs: none
-->

# Star Wars Progress Bar Constitution

## Core Principles

### I. Platform Portability (NON-NEGOTIABLE)

- The plugin MUST depend only on `com.intellij.modules.platform`; IDE-specific module APIs (`java`, `python`, etc.) are forbidden.
- Every API used MUST exist since the build number declared as `pluginSinceBuild` in `gradle.properties`; introducing newer platform APIs requires raising that value and re-verifying compatibility.
- Compatibility is defined solely by `pluginSinceBuild` (minimum supported IDE build) and `platformVersion` (the compilation artifact).

Rationale: The plugin targets all JetBrains IDEs — IntelliJ IDEA, Android Studio, PyCharm, WebStorm, Rider, CLion, GoLand, RubyMine, RustRover, PhpStorm, Kotlin/Scala plugins — not just IntelliJ IDEA. Compiling against the IDEA artifact is a build convenience only; at runtime in every JetBrains IDE, only common-platform APIs are available.

### II. Content Integrity (Single Source of Truth)

- `src/main/resources/json/factions.json` is the single source of truth for all vehicles, lightsabers, and factions.
- Every entity MUST have its complete icon set present in both required locations (LFS-tracked), translations under every supported bundle file, and faction folders MUST include `logo.png`/`logo@2x.png`.
- Content changes MUST be made through the documented skill workflows (`add-vehicle`, `add-lightsaber`, `add-faction`) and MUST keep `LIST_OF_VEHICLES.md` in sync for vehicles.
- Consistency is enforced by `IconResourceTests` and `StarWarsResourceBundleTests`; these tests MUST pass before content ships.

Rationale: With 150+ vehicles, 20+ lightsabers, multiple languages, and LFS-tracked icons, partial updates silently break the plugin — missing logos fall back to empty icons, and missing translations surface raw keys in the UI. The resource tests are the only reliable consistency check across this matrix.

### III. Quality Gates Are Non-Negotiable

- CI MUST run `buildPlugin` → `check` → `verifyPlugin` on every push; a change is not complete until all three pass locally or in CI.
- Kotlin code style is IntelliJ IDEA (ktlint), configured via `.editorconfig`: 4-space indent, LF line endings, UTF-8, max line length 120.
- The ktlint and detekt baselines MUST NOT grow: new code MUST NOT add violations; prefer fixing over extending baselines.
- Tests use JUnit 5 (Jupiter) with MockK and mirror the main package structure under `src/test/kotlin/`; the JUnit 4 runtime dependency is required by the IntelliJ Platform test framework and MUST NOT be removed.

Rationale: The gates are the only thing standing between contributors and a broken plugin across every supported IDE; baselines prevent gradual quality decay, and mirrored tests keep coverage discoverable next to the code they verify.

### IV. Localization Completeness

- Every user-facing string MUST have a bundle key defined in `messages/StarWarsBundle.properties` (default) and in every locale file (`_en`, `_de`, `_es`).
- The set of supported languages is defined by `configuration/Language.kt` and MUST match the locale bundle files; adding a language requires both.
- Entity names use prefixed keys (e.g., `vehicles.<name>`) that end with the icon file name, per the established convention.

Rationale: The plugin ships to international users on the JetBrains Marketplace; missing translations surface raw resource keys in settings panels and tooltips, breaking the experience for every non-default locale.

### V. Simplicity & EDT Performance

- Progress bar drawing runs on Swing's Event Dispatch Thread; drawing code MUST stay lightweight — no I/O, no allocation-heavy work per frame.
- Icons MUST be loaded through `StarWarsResourceLoader`'s cache (Guava); ad-hoc per-frame loading is forbidden.
- Start simple: prefer the smallest change that satisfies the requirement (YAGNI), and complexity must be justified in review.

Rationale: The progress bar redraws continuously during builds, indexing, and other long-running operations; heavy drawing code degrades every such operation in every JetBrains IDE, not just this plugin's features.

## Compatibility & Versioning

- Plugin version follows SemVer in `gradle.properties`; pre-release labels (e.g., `3.1.0-alpha`) publish to custom channels automatically.
- `CHANGELOG.md` follows Keep a Changelog with `Added`, `Changed`, `Removed`, and `Fixed` sections; breaking changes are marked with a leading `BREAKING:`; entries go under `[Unreleased]`.
- The marketplace description is extracted from the section of `README.md` between `<!-- Plugin description -->` and `<!-- Plugin description end -->`; the build fails if the markers are missing or malformed.
- Publishing requires the release secrets (`PUBLISH_TOKEN`, `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD`).

## Development Workflow

- Use the Gradle wrapper; Java 21+ is required. Key tasks: `buildPlugin` (distributable ZIP), `check` (tests + ktlint + detekt — the CI gate), `test`, `runPlugin` (sandbox IDE for manual testing), `verifyPlugin`.
- The `build` task installs a pre-commit hook that runs `ktlintFormat`, deletes stale test results, runs tests, and does `git add .`; keep commits clean so this works.
- Content and settings changes MUST follow the skill workflows under `.agents/skills/` (`add-vehicle`, `add-lightsaber`, `add-faction`, `add-setting`) — each lists every artifact that must be updated together.

## Governance

This constitution supersedes all other project practices; where `AGENTS.md` or other guidance conflicts with it, this document wins and the conflict MUST be resolved by amending one of them. Amendments require a version bump per SemVer (MAJOR for backward-incompatible principle removals or redefinitions, MINOR for new principles/sections or materially expanded guidance, PATCH for clarifications, wording, and typo fixes) and an entry in the Sync Impact Report describing what changed. Compliance review: every PR MUST keep CI green (`buildPlugin`, `check`, `verifyPlugin`); reviewers MUST verify that content changes satisfy Principle II (icons + translations + docs together) and that no new lint/detekt violations are introduced. Use `AGENTS.md` for day-to-day development guidance; this constitution defines the non-negotiable rules it must not violate.

**Version**: 1.0.0 | **Ratified**: 2026-11-09 | **Last Amended**: 2026-11-09
