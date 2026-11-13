# AGENTS.md

Guidance for AI agents (and contributors) working on this repository.

## Project Overview

**Star Wars Progress Bar** is an IntelliJ Platform plugin that replaces the IDE's progress bar with Star Wars-themed visuals: 150+ vehicles and 20+ lightsabers from canon and legends, animated as they cross the progress bar during builds, indexing, and other long-running operations.

The plugin targets **all JetBrains IDEs** (IntelliJ IDEA, Android Studio, PyCharm, WebStorm, Rider, CLion, GoLand, RubyMine, RustRover, PhpStorm, Kotlin/Scala plugins, etc.), not just IntelliJ IDEA:

- It depends only on `com.intellij.modules.platform` (see `src/main/resources/META-INF/plugin.xml`).
- The Gradle build resolves the platform from `intellijIdea(providers.gradleProperty("platformVersion"))`, but that is merely the artifact used for compilation.
- **Never use IDE-specific APIs** (e.g., anything from `com.intellij.modules.platform`'s siblings like `java`, `python`, etc.). Only common-platform APIs are available at runtime in every JetBrains IDE.

Current compatibility is defined by `pluginSinceBuild` (minimum supported IDE build) and `platformVersion` (the IntelliJ Platform artifact the plugin compiles against). **Read both from `gradle.properties`** — do not assume specific values.

## Repository Layout

```
build.gradle.kts              # Build config (Kotlin DSL)
settings.gradle.kts           # Repositories + plugin settings
gradle/libs.versions.toml     # Version catalog: all dependency/plugin versions
gradle.properties             # Plugin version, platform version, since-build
CHANGELOG.md                  # Keep a Changelog format, managed by gradle-changelog-plugin
LIST_OF_VEHICLES.md           # Manually maintained list of all vehicles
src/
  main/java/.../listeners/    # StarWarsProgressListener.java (Java: LaF change, plugin enable/disable, app activation)
  main/java/.../ui/           # StarWarsProgressBarFactory.java (Swing UI factory entry point)
  main/kotlin/com/christopherosthues/starwarsprogressbar/
    configuration/            # Settings UI panels + persistent state (StarWarsState, StarWarsPersistentStateComponent)
    constants/                # Default values and bundle key constants
    models/                   # Data classes: StarWarsVehicle, Lightsabers, StarWarsFaction, ...
    notification/             # Plugin-updated notification activity
    selectors/                # Entity selection strategies (random, in-order per faction/name, reverse, rolling)
    ui/                       # Progress bar decorators, shapes, colors
    util/                     # Resource loading/caching (StarWarsResourceLoader), JSON parsing
  main/resources/
    META-INF/plugin.xml       # Plugin descriptor: configurable, services, listeners, notification group
    json/factions.json        # SINGLE SOURCE OF TRUTH for all vehicles and lightsabers
    icons/{vehicles,lightsabers}/<faction>/...   # PNG icons (Git LFS)
    messages/StarWarsBundle*.properties           # Translations: default + _en, _de, _es
  test/kotlin/                # JUnit 5 tests mirroring the main package structure
scripts/pre-commit            # Git hook installed by the `build` task
.github/workflows/build.yml   # CI: buildPlugin, check, verifyPlugin, release draft
.github/workflows/release.yml # Publish to JetBrains Marketplace on GitHub release
```

## Build & Test Commands

Use the Gradle wrapper (`./gradlew` on Unix/macOS, `.\gradlew.bat` on Windows). Java 21+ is required.

| Task | Purpose |
|---|---|
| `buildPlugin` | Builds the distributable plugin ZIP into `build/distributions/` |
| `check` | Runs tests + ktlint + detekt (the CI gate) |
| `test` | Runs JUnit 5 tests only |
| `runPlugin` | Launches a sandbox IDE with the plugin installed for manual testing |
| `verifyPlugin` | Structural validation + IntelliJ Plugin Verifier against `pluginVerifierIdeVersions` |
| `ktlintCheck` / `ktlintFormat` | Lint / auto-format Kotlin code (style: `intellij_idea`) |
| `detekt` | Static analysis; config in `config/detekt/detekt.yml`, baseline in `config/detekt/baseline.xml` |
| `publishPlugin` | Publish to JetBrains Marketplace (requires env vars, see Release Process) |
| `getChangelog` / `patchChangelog` | Read/patch changelog entries (gradle-changelog-plugin) |

Notes:

- The `build` task installs the pre-commit hook (`scripts/pre-commit`) into `.git/hooks/`. The hook runs `ktlintFormat`, deletes stale test results, runs tests, and does `git add .` — keep commits clean so this works.
- Tests use JUnit 5 (Jupiter) with MockK; JUnit 4 is present at runtime because the IntelliJ Platform test framework requires it. Do not remove the `junit4` dependency.
- Coverage reports are produced by Kover (`build/reports/kover`).

## Code Style & Quality Gates

- Kotlin code style: **IntelliJ IDEA** (enforced via ktlint, see `.editorconfig`: 4-space indent, LF line endings, UTF-8, max line length 120).
- Both ktlint and detekt have baselines (`config/ktlint/baseline.xml`, `config/detekt/baseline.xml`). New code must not add violations; prefer fixing over extending baselines.
- CI (`.github/workflows/build.yml`) runs on every push: `buildPlugin` → `check` → `verifyPlugin`. A draft GitHub release is created for pushes to `main`, `release/*`, or `hotfix/*` branches.

## Architecture Notes

### Plugin registration (`plugin.xml`)

- `applicationConfigurable` under the `appearance` parent — settings live at *Settings/Preferences > Appearance & Behavior > Star Wars Progress Bar*.
- `StarWarsPersistentStateComponent` is an application service implementing `PersistentStateComponent<StarWarsState>`; all user settings are stored in `StarWarsState`.
- `PluginUpdatedActivity` (post-startup) shows a notification when the plugin version changed.
- `StarWarsProgressListener` (Java) listens to `LafManager`, `DynamicPlugin`, and `ApplicationActivation` topics to install/uninstall the progress bar UI.

### Adding new settings

Use the **`add-setting`** skill (`.agents/skills/add-setting/SKILL.md`) — it covers every piece that must be added together: the default constant, the label bundle key (+ every bundle file), the `StarWarsState` field including `copy()` and `isModified(...)`, the explicit copy in `apply()`, and the UI panel wiring.

### Entity selection

- `selectors/` contains strategies implementing `IStarWarsSelector`: random, rolling-random, in-order/reverse per faction or name, for determinate and indeterminate progress bars separately (`SelectionType`, `EntitySelectionType`).
- `factions.json` is parsed with kotlinx.serialization into `StarWarsFactions` by `util/StarWarsResourceLoader.kt`.

### Adding content (vehicles, lightsabers, factions)

The primary content workflow is split into skills under `.agents/skills/`:

- **`add-vehicle`** — add/rename/remove a vehicle: `factions.json` entry, icon set in both locations, translations in every bundle file, and the `LIST_OF_VEHICLES.md` update.
- **`add-lightsaber`** — same for lightsabers (including jar-kai blade definitions); no `LIST_OF_VEHICLES.md` entry.
- **`add-faction`** — new faction: `factions.json` entry, LFS-tracked logos (`logo.png`/`logo@2x.png`), and faction translation keys in every bundle file.

Each skill covers the naming rules and the test gates (`IconResourceTests`, `StarWarsResourceBundleTests`) that enforce them.

### Localization

- Bundle: `messages.StarWarsBundle` (`AbstractBundle` wrapper in `StarWarsBundle.kt`).
- Supported languages are defined by `configuration/Language.kt` (English, German, Spanish) and must match the `_en`, `_de`, `_es` bundle files. Adding a language requires both.

## Commit Messages

Use the **`semantic-commits`** skill (`.agents/skills/semantic-commits/SKILL.md`) for all hand-written commits and PR titles: `<type>: <subject>` with `feat`, `fix`, `docs`, `refactor`, `test`, `chore`, `ci`, or `build`; breaking changes get a `!` marker plus a leading-`BREAKING:` entry under `[Unreleased]` in CHANGELOG.md.

## README & Changelog Conventions

- **The plugin description is extracted from `README.md`.** The build fails if the markers are missing or malformed. Keep the section between `<!-- Plugin description -->` and `<!-- Plugin description end -->` valid; it becomes the marketplace listing (converted to HTML).
- `CHANGELOG.md` follows [Keep a Changelog](https://keepachangelog.com) with sections `Added`, `Changed`, `Removed`, `Fixed`. Mark breaking changes with a leading `BREAKING:`. The `[Unreleased]` section is consumed by release tooling — add entries there, not under released versions.
- Version in `gradle.properties` follows SemVer; pre-release labels (e.g., `3.1.0-alpha`) publish to custom channels automatically.

## CI/CD & Release Process

- **Build workflow** (`build.yml`): runs on every push and via manual dispatch. Creates a draft GitHub release for `main`, `release/*`, and `hotfix/*` branches.
- **Release workflow** (`release.yml`): triggered when a GitHub release is published (prereleased or released). It patches the changelog from the release notes, runs `publishPlugin`, uploads the ZIP as a release asset, and opens a PR updating `CHANGELOG.md`.
- Publishing requires these secrets: `PUBLISH_TOKEN`, `CERTIFICATE_CHAIN`, `PRIVATE_KEY`, `PRIVATE_KEY_PASSWORD` (signing config is commented out in `build.gradle.kts`; env vars are read by the plugin's publishing extension).

## Gotchas

- The progress bar UI is Swing-based and runs on the EDT; keep drawing code lightweight (icons are cached in `StarWarsResourceLoader` via Guava cache).
- Any API used must exist since the build number in `pluginSinceBuild` (see `gradle.properties`) — do not introduce newer platform APIs without raising that value.
- The plugin icon and faction logos (`logo.png`, `logo@2x.png`) are loaded per-faction from the same icon directories; a missing logo falls back to an empty icon, so keep faction folders complete.
- Tests mirror main package names under `src/test/kotlin/`; shared test utilities live in `test/helpers/`.
