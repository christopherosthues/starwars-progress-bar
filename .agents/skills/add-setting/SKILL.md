---
name: Add Setting
description: Adds a new user setting to the Star Wars Progress Bar plugin. Use when adding, renaming, or removing a configuration option — requires updating ConfigurationConstants.kt, BundleConstants.kt (+ every bundle file), StarWarsState.kt (including copy() and isModified(...)), StarWarsProgressConfigurable.apply(), and the UI panel under configuration/components/.
---

# Add a setting

All pieces must be added **together** — CI tests fail if any part is missing:

| Piece | Location | Enforced by |
|---|---|---|
| Default value constant | `constants/ConfigurationConstants.kt` | `ConfigurationConstantsTests` (convention) |
| Label bundle key | `constants/BundleConstants.kt` + every bundle file | `BundleConstantsTests` ("all public constants should be tested") |
| State field | `configuration/StarWarsState.kt` — incl. `copy()` and the relevant `isXxxModified(...)` check | `StarWarsStateTests` (convention) |
| Apply on OK | `configuration/StarWarsProgressConfigurable.apply()` | manual — setting silently not persisted |
| UI wiring | panel under `configuration/components/` | manual |

## 1. Default value constant

Add to `constants/ConfigurationConstants.kt`:

```kotlin
internal const val DEFAULT_MY_SETTING = false
```

- Naming: `DEFAULT_` + PascalCase of the state field name (e.g., `showIcon` → `DEFAULT_SHOW_ICON`).
- For enum-typed settings use `val` with `.ordinal` (see `DEFAULT_SELECTOR`).
- Add an assertion for it in `ConfigurationConstantsTests`.

## 2. Label bundle key

Settings labels come from the translation bundles, not hardcoded strings:

1. Add to `constants/BundleConstants.kt`:

```kotlin
const val MY_SETTING = CONFIGURATION + "mySetting"
```

2. Add `configuration.mySetting=My Setting` to **every** bundle file under `src/main/resources/messages/` (the fallback `StarWarsBundle.properties` plus one per language defined in `Language` (`configuration/Language.kt`) — files are named `StarWarsBundle_<languageCode>.properties`).
3. Add the entry to the test data stream in `BundleConstantsTests.bundleConstants()` — the "all public constants should be tested" check fails otherwise.

## 3. State field

Add to `configuration/StarWarsState.kt`:

```kotlin
var mySetting: Boolean = DEFAULT_MY_SETTING
```

- For enum-typed settings, follow the ordinal pattern: a persisted `<name>Ordinal: Int` plus an internal nullable property (see `determinateOrderSelector`).
- **Include it in `copy(starWarsState)`** — used when refreshing/resetting the UI.
- **Include it in the relevant `isXxxModified(other)` check** — otherwise OK stays disabled and changes are lost on close.

Add assertions to `StarWarsStateTests` (`assertDefaultValues` and `values should be updated on set`).

## 4. Apply (persist on OK)

`StarWarsProgressConfigurable.apply()` copies each field from the component's state into the persistent state explicitly — add a line there:

```kotlin
starWarsState.mySetting = component.starWarsState.mySetting
```

Persistence itself is automatic: `StarWarsPersistentStateComponent` XML-serializes `StarWarsState`, so no extra work for simple serializable fields. Guarded settings (e.g., `numberOfPassesUntilChange` only applies when `changeAfterPass`) follow the same guarded pattern in `apply()`.

## 5. UI wiring

Wire into the appropriate panel under `configuration/components/` (most options live in `UiOptionsPanel.kt`):

1. Create the Swing component initialized with the default value and bundle label:

```kotlin
private val mySettingCheckBox = JBCheckBox(StarWarsBundle.message(BundleConstants.MY_SETTING), DEFAULT_MY_SETTING)
```

2. Add a listener that fires a property change event and updates `starWarsState` (see existing checkboxes).
3. Add the component to the panel layout.
4. Update its text in the language-change block (`addPropertyChangeListener { if (it.propertyName == LANGUAGE_EVENT) ... }`).
5. Set it from state in `updateUI(starWarsState)`.

If the setting affects the progress bar preview: add an event constant to `configuration/StarWarsStateEvents.kt`, fire it via `firePropertyChange(<EVENT>, old, new)`, and register it as a repaint trigger in one of the `isXxxEvent(...)` helpers in `StarWarsProgressConfigurationComponent`.

## 6. Verify

Run from the repo root:

```powershell
.\gradlew.bat test
```

Relevant gates: `BundleConstantsTests` (every public constant must have a test entry), `StarWarsStateTests`, and `StarWarsResourceBundleTests` (translation parity). Then run `.\gradlew.bat check` for the full CI gate.

## Renaming / removing a setting

- Rename: update everything above **and** keep the old field `@Deprecated` in `StarWarsState.kt` with a migration in `StarWarsPersistentStateComponent.loadState` (see `migrationToVersion2`) so existing users' values carry over.
- Remove: delete all of the above; old persisted values are simply ignored on load.
