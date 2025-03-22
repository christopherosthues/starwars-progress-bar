package messages

import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.util.*
import java.util.stream.Stream
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

internal const val BUNDLE_IDENTIFIER = "StarWarsBundle"
internal const val BUNDLE_EXTENSION = ".properties"

// TODO add unit test to detect superfluous translations
class StarWarsResourceBundleTests {
    //region Tests

    @ParameterizedTest()
    @MethodSource("bundleFileValues")
    @DisplayName("Check if all bundles contain no empty translations")
    fun `bundles should not contain empty entries`(bundleFile: File) {
        // Arrange
        val bundle = Properties()
        bundle.load(bundleFile.inputStream())
        val propertyNames = bundle.stringPropertyNames()

        // Act and Assert
        assertTrue(
            bundle.values.all { it != null && it.toString().trim().isNotEmpty() },
            "Found empty translations for keys: ${
                propertyNames.filter {
                    bundle[it] == null || bundle[it].toString().trim().isEmpty()
                }
            }",
        )
    }

    @ParameterizedTest
    @MethodSource("languageSpecificBundleFileValues")
    @DisplayName("Check if for every translations there is a fallback specified")
    fun `fallback bundle should contain all translations of language specific bundles`(bundleFile: File) {
        // Arrange
        val bundle = Properties()
        bundle.load(bundleFile.inputStream())

        // Act
        val fallbackBundle = Properties()
        fallbackBundle.load(getFallbackBundleFile().inputStream())

        // Assert
        assertTrue(
            fallbackBundle.keys.containsAll(bundle.keys),
            "Found missing translations in fallback bundle which are contained in language specific bundle ${bundleFile.name}: ${
                bundle.keys.filter {
                    !fallbackBundle.containsKey(it)
                }
            }",
        )
    }

    @ParameterizedTest
    @MethodSource("languageSpecificBundleFileValues")
    @DisplayName("Check if all language specific bundles contain all translations declared in the fallback bundle")
    fun `language specific bundles should contain all translations except plugin name`(bundleFile: File) {
        // Arrange
        val fallbackBundle = Properties()
        fallbackBundle.load(getFallbackBundleFile().inputStream())

        // Act
        val bundle = Properties()
        bundle.load(bundleFile.inputStream())

        // Assert
        val fallbackBundleKeys = fallbackBundle.keys.filter { !whitelistedKeys.contains(it) }
        assertTrue(
            bundle.keys.containsAll(fallbackBundleKeys),
            "Found missing translations in language specific bundle which are contained in fallback bundle ${bundleFile.name}: ${
                fallbackBundleKeys.filter {
                    !bundle.containsKey(it)
                }
            }",
        )
    }

    @ParameterizedTest
    @MethodSource("bundleFileValues")
    @DisplayName("Check if all used bundle keys have a translation")
    fun `bundles should contain all bundle keys`(bundleFile: File) {
        // Arrange
        val bundle = Properties()
        bundle.load(bundleFile.inputStream())
        var bundleConstantMembers = BundleConstants::class.memberProperties.filter {
            it.visibility != KVisibility.PRIVATE && !bundleConstantMembersToIgnore.contains(it.name)
        }
        if (!bundleFile.nameWithoutExtension.endsWith(BUNDLE_IDENTIFIER)) {
            bundleConstantMembers = bundleConstantMembers.filter { it.name != "PLUGIN_NAME" }
        }

        // Act
        val bundleConstantsLocalized = bundleConstantMembers.map {
            {
                assertNotNull(
                    bundle[it.getter.call()],
                    "Bundle constant ${it.name} has no translation",
                )
            }
        }.stream()

        // Assert
        assertAll(bundleConstantsLocalized)
    }

    @ParameterizedTest
    @MethodSource("bundleFileValues")
    @DisplayName("Check if all vehicle factions have a translation")
    fun `bundles should contain translations for all vehicle factions`(bundleFile: File) {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.vehicleFactions
        val bundle = Properties()
        bundle.load(bundleFile.inputStream())

        // Act
        val factionsLocalized = starWarsFactions.filter { it.id.isNotEmpty() }.map {
            {
                assertNotNull(
                    bundle[BundleConstants.VEHICLES_FACTION + it.id],
                    "Faction ${it.id} with localization key ${BundleConstants.VEHICLES_FACTION}${it.id} has no translation",
                )
            }
        }

        // Assert
        assertAll(factionsLocalized)
    }

    @ParameterizedTest
    @MethodSource("bundleFileValues")
    @DisplayName("Check if all lightsaber factions have a translation")
    fun `bundles should contain translations for all lightsaber factions`(bundleFile: File) {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.lightsabersFactions
        val bundle = Properties()
        bundle.load(bundleFile.inputStream())

        // Act
        val factionsLocalized = starWarsFactions.filter { it.id.isNotEmpty() }.map {
            {
                assertNotNull(
                    bundle[BundleConstants.LIGHTSABERS_FACTION + it.id],
                    "Faction ${it.id} with localization key ${BundleConstants.LIGHTSABERS_FACTION}${it.id} has no translation",
                )
            }
        }

        // Assert
        assertAll(factionsLocalized)
    }

    @ParameterizedTest
    @MethodSource("bundleFileValues")
    @DisplayName("Check if all vehicles have a translation")
    fun `bundles should contain translations for all vehicles`(bundleFile: File) {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.vehicleFactions
        val bundle = Properties()
        bundle.load(bundleFile.inputStream())

        // Act
        val vehiclesLocalized: Stream<() -> Unit> = starWarsFactions.parallelStream().map {
            it.data.map { vehicle ->
                {
                    assertNotNull(
                        bundle[vehicle.localizationKey],
                        "Vehicle ${vehicle.entityId} with localization key ${vehicle.localizationKey} has no translation",
                    )
                }
            }
        }
            .flatMap { it.stream() }

        // Assert
        assertAll(vehiclesLocalized)
    }

    @ParameterizedTest
    @MethodSource("bundleFileValues")
    @DisplayName("Check if all lightsabers have a translation")
    fun `bundles should contain translations for all lightsabers`(bundleFile: File) {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.lightsabersFactions
        val bundle = Properties()
        bundle.load(bundleFile.inputStream())

        // Act
        val lightsabersLocalized: Stream<() -> Unit> = starWarsFactions.parallelStream().map {
            it.data.map { lightsaber ->
                {
                    assertNotNull(
                        bundle[lightsaber.localizationKey],
                        "Vehicle ${lightsaber.entityId} with localization key ${lightsaber.localizationKey} has no translation",
                    )
                }
            }
        }
            .flatMap { it.stream() }

        // Assert
        assertAll(lightsabersLocalized)
    }

    //endregion

    //region Helper methods

    private fun getFallbackBundleFile(): File = File(".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources${File.separatorChar}messages${File.separatorChar}").list { _, name ->
        name != null && name.equals(BUNDLE_IDENTIFIER + BUNDLE_EXTENSION)
    }
        .map { fileName -> File(".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources${File.separatorChar}messages${File.separatorChar}$fileName") }
        .first()

    //endregion

    //region Test data

    private val whitelistedKeys = listOf(BundleConstants.PLUGIN_NAME)
    private val bundleConstantMembersToIgnore =
        listOf("VEHICLES_FACTION", "VEHICLES", "LIGHTSABERS_FACTION", "LIGHTSABERS")

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun bundleFileValues(): List<File> = File(".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources${File.separatorChar}messages${File.separatorChar}").list { _, name ->
            name != null && name.startsWith(BUNDLE_IDENTIFIER) && name.endsWith(BUNDLE_EXTENSION)
        }
            .map { fileName -> File(".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources${File.separatorChar}messages${File.separatorChar}$fileName") }

        @JvmStatic
        fun languageSpecificBundleFileValues(): List<File> = File(".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources${File.separatorChar}messages${File.separatorChar}").list { _, name ->
            name != null &&
                name.startsWith(BUNDLE_IDENTIFIER) &&
                name.endsWith(BUNDLE_EXTENSION) &&
                !name.equals(
                    BUNDLE_IDENTIFIER + BUNDLE_EXTENSION,
                )
        }
            .map { fileName -> File(".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources${File.separatorChar}messages${File.separatorChar}$fileName") }
    }

    //endregion
}
