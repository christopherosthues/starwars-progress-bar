package icons

import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.util.stream.Collectors.toList
import kotlin.io.path.absolutePathString

class IconResourceTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        FactionHolder.updateFactions(StarWarsResourceLoader.loadFactions().factions)
    }

    //endregion

    //region Tests

    @Test
    fun `all icon resources should exist`() {
        // Arrange
        val starWarsFactions = FactionHolder.factions

        // Act
        val iconPaths = starWarsFactions.map {
            it.vehicles.map { vehicle ->
                val iconBasePath =
                    ".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}" +
                        "resources${File.separatorChar}icons/${vehicle.fileName}"
                val icon2xBasePath = ".${File.separatorChar}icons/${vehicle.fileName}"
                listOf(
                    { assertTrue(File("$iconBasePath.png").exists(), "Icon $iconBasePath.png does not exist.") },
                    {
                        assertTrue(
                            File("${iconBasePath}_r.png").exists(),
                            "Icon ${iconBasePath}_r.png does not exist."
                        )
                    },
                    {
                        assertTrue(
                            File("$icon2xBasePath@2x.png").exists(),
                            "Icon $icon2xBasePath@2x.png does not exist."
                        )
                    },
                    {
                        assertTrue(
                            File("${icon2xBasePath}_r@2x.png").exists(),
                            "Icon ${icon2xBasePath}_r@2x.png does not exist."
                        )
                    }
                )
            }.stream().flatMap { e -> e.stream() }.collect(toList())
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        assertAll(iconPaths)
    }

    @Test
    fun `all logo resources should exist`() {
        // Arrange
        val starWarsFactions = FactionHolder.defaultFactions

        // Act
        val iconPaths = starWarsFactions.map {
            val iconBasePath =
                ".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources" +
                    "${File.separatorChar}icons${File.separatorChar}${it.id}${File.separatorChar}logo"
            listOf(
                { assertTrue(File("$iconBasePath.png").exists(), "Icon $iconBasePath.png does not exist.") },
                { assertTrue(File("$iconBasePath@2x.png").exists(), "Icon $iconBasePath@2x.png does not exist.") }
            )
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        assertAll(iconPaths)
    }

    @Test
    fun `all icon resources should be used`() {
        // Arrange
        val starWarsFactions = FactionHolder.factions
        val iconBasePath =
            "${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources${File.separatorChar}icons"

        val matcher = FileSystems.getDefault().getPathMatcher("glob:**/*\\.png")
        val path = File(".$iconBasePath").toPath()
        val basePath = "$iconBasePath${File.separatorChar}"
        val imageFiles = Files.walk(path)
            .filter {
                matcher.matches(it)
            }
            .collect(toList())
            .map {
                val imagePath = it.toAbsolutePath().absolutePathString()
                imagePath.drop(imagePath.indexOf(basePath) + basePath.length)
            }

        // Act
        val iconPaths = starWarsFactions.map {
            it.vehicles.map { vehicle ->
                val iconFilePath = vehicle.fileName.replace('/', File.separatorChar)
                listOf("$iconFilePath.png", "${iconFilePath}_r.png")
            }.stream().flatMap { e -> e.stream() }.collect(toList())
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        val imagesNotReferences = imageFiles.filter {
            !iconPaths.contains(it) && !iconWhitelist.contains(it) && !it.endsWith("logo.png") &&
                !it.endsWith("logo@2x.png")
        }
        assertTrue(
            imagesNotReferences.isEmpty(),
            getFormattedImagesNotReferencedErrorMessage(imagesNotReferences)
        )
    }

    @Test
    fun `all 2x icon resources should be used`() {
        // Arrange
        val starWarsFactions = FactionHolder.factions
        val iconBasePath =
            "${File.separatorChar}icons"

        val matcher = FileSystems.getDefault().getPathMatcher("glob:**/*\\.png")
        val path = File(".$iconBasePath").toPath()
        val basePath = "$iconBasePath${File.separatorChar}"
        val imageFiles = Files.walk(path)
            .filter {
                matcher.matches(it)
            }
            .collect(toList())
            .map {
                val imagePath = it.toAbsolutePath().absolutePathString()
                imagePath.drop(imagePath.indexOf(basePath) + basePath.length)
            }

        // Act
        val iconPaths = starWarsFactions.map {
            it.vehicles.map { vehicle ->
                val iconFilePath = vehicle.fileName.replace('/', File.separatorChar)
                listOf("$iconFilePath@2x.png", "${iconFilePath}_r@2x.png")
            }.stream().flatMap { e -> e.stream() }.collect(toList())
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        val imagesNotReferences = imageFiles.filter {
            !iconPaths.contains(it) && !iconWhitelist.contains(it)
        }
        assertTrue(
            imagesNotReferences.isEmpty(),
            getFormattedImagesNotReferencedErrorMessage(imagesNotReferences)
        )
    }

    //endregion

    //region Helper methods

    private val iconWhitelist = listOf(
        "galactic_republic${File.separatorChar}at_te@2x.png",
        "galactic_empire${File.separatorChar}quasar_fire_class_cruiser_carrier@2x.png",
        "rebel_alliance${File.separatorChar}phoenix_nest@2x.png",
        "scoundrels${File.separatorChar}lady_luck@2x.png",
        "scoundrels${File.separatorChar}outrider@2x.png",
        "scoundrels${File.separatorChar}outrider_r@2x.png"
    )

    private fun getFormattedImagesNotReferencedErrorMessage(imagesNotReferenced: List<String>): String {
        val errorMessage = StringBuilder("Found unused image:")
        errorMessage.appendLine()
        imagesNotReferenced.stream().sorted().forEach {
            errorMessage.appendLine(it)
        }

        return errorMessage.toString()
    }

    //endregion
}
