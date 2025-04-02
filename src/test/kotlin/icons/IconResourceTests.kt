package icons

import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
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
        StarWarsFactionHolder.updateFactions(StarWarsResourceLoader.loadFactions())
    }

    //endregion

    //region Tests

    @Test
    fun `all vehicle icon resources should exist`() {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.vehicleFactions

        // Act
        val iconPaths = starWarsFactions.map {
            it.data.map { vehicle ->
                val iconBasePath =
                    ".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}" +
                        "resources${File.separatorChar}icons${File.separatorChar}${vehicle.fileName}"
                val icon2xBasePath = ".${File.separatorChar}icons${File.separatorChar}${vehicle.fileName}"
                listOf(
                    { assertTrue(File("$iconBasePath.png").exists(), "Icon $iconBasePath.png does not exist.") },
                    {
                        assertTrue(
                            File("${iconBasePath}_r.png").exists(),
                            "Icon ${iconBasePath}_r.png does not exist.",
                        )
                    },
                    {
                        assertTrue(
                            File("$icon2xBasePath@2x.png").exists(),
                            "Icon $icon2xBasePath@2x.png does not exist.",
                        )
                    },
                    {
                        assertTrue(
                            File("${icon2xBasePath}_r@2x.png").exists(),
                            "Icon ${icon2xBasePath}_r@2x.png does not exist.",
                        )
                    },
                )
            }.stream().flatMap { e -> e.stream() }.collect(toList())
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        assertAll(iconPaths)
    }

    @Test
    fun `all vehicle logo resources should exist`() {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.defaultVehicleFactions

        // Act
        val iconPaths = starWarsFactions.map {
            val iconBasePath =
                ".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources" +
                    "${File.separatorChar}icons${File.separatorChar}vehicles${File.separatorChar}" +
                    "${it.id}${File.separatorChar}logo"
            listOf(
                { assertTrue(File("$iconBasePath.png").exists(), "Icon $iconBasePath.png does not exist.") },
                { assertTrue(File("$iconBasePath@2x.png").exists(), "Icon $iconBasePath@2x.png does not exist.") },
            )
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        assertAll(iconPaths)
    }

    @Test
    fun `all vehicle icon resources should be used`() {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.vehicleFactions
        val iconBasePath =
            "${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources${File.separatorChar}icons"

        val matcher = FileSystems.getDefault().getPathMatcher("glob:**/vehicles/**/*\\.png")
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
            it.data.map { vehicle ->
                val iconFilePath = vehicle.fileName.replace('/', File.separatorChar)
                listOf("$iconFilePath.png", "${iconFilePath}_r.png")
            }.stream().flatMap { e -> e.stream() }.collect(toList())
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        val imagesNotReferences = imageFiles.filter {
            !iconPaths.contains(it) &&
                !iconWhitelist.contains(it) &&
                !it.endsWith("logo.png") &&
                !it.endsWith("logo@2x.png")
        }
        assertTrue(
            imagesNotReferences.isEmpty(),
            getFormattedImagesNotReferencedErrorMessage(imagesNotReferences),
        )
    }

    @Test
    fun `all vehicle 2x icon resources should be used`() {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.vehicleFactions
        val iconBasePath = "${File.separatorChar}icons"

        val matcher = FileSystems.getDefault().getPathMatcher("glob:**/vehicles/**/*\\.png")
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
            it.data.map { vehicle ->
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
            getFormattedImagesNotReferencedErrorMessage(imagesNotReferences),
        )
    }

    @Test
    fun `all lightsaber icon resources should exist`() {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.lightsabersFactions

        // Act
        val iconPaths = starWarsFactions.map {
            it.data.map { lightsaber ->
                val iconBasePath =
                    ".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}" +
                        "resources${File.separatorChar}icons${File.separatorChar}${lightsaber.fileName}"
                val icon2xBasePath = ".${File.separatorChar}icons${File.separatorChar}${lightsaber.fileName}"
                if (lightsaber.isJarKai) {
                    List(lightsaber.lightsabers.size) { index: Int ->
                        listOf(
                            { assertTrue(File("${iconBasePath}_${lightsaber.lightsabers[index].id}.png").exists(),
                                "Icon ${iconBasePath}_${lightsaber.lightsabers[index].id}.png does not exist.")
                            },
                            {
                                assertTrue(
                                    File("${icon2xBasePath}_${lightsaber.lightsabers[index].id}@2x.png").exists(),
                                    "Icon ${icon2xBasePath}_${lightsaber.lightsabers[index].id}@2x.png does not exist.",
                                )
                            },
                        )
                    }.flatten()
                } else {
                    listOf(
                        { assertTrue(File("$iconBasePath.png").exists(), "Icon $iconBasePath.png does not exist.") },
                        {
                            assertTrue(
                                File("$icon2xBasePath@2x.png").exists(),
                                "Icon $icon2xBasePath@2x.png does not exist.",
                            )
                        },
                    )
                }
            }.stream().flatMap { e -> e.stream() }.collect(toList())
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        assertAll(iconPaths)
    }

    @Test
    fun `all lightsaber logo resources should exist`() {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.defaultLightsabersFactions

        // Act
        val iconPaths = starWarsFactions.map {
            val iconBasePath =
                ".${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources" +
                    "${File.separatorChar}icons${File.separatorChar}lightsabers${File.separatorChar}" +
                    "${it.id}${File.separatorChar}logo"
            listOf(
                { assertTrue(File("$iconBasePath.png").exists(), "Icon $iconBasePath.png does not exist.") },
                { assertTrue(File("$iconBasePath@2x.png").exists(), "Icon $iconBasePath@2x.png does not exist.") },
            )
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        assertAll(iconPaths)
    }

    @Test
    fun `all lightsaber icon resources should be used`() {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.lightsabersFactions
        val iconBasePath =
            "${File.separatorChar}src${File.separatorChar}main${File.separatorChar}resources" +
                "${File.separatorChar}icons"

        val matcher = FileSystems.getDefault().getPathMatcher("glob:**/lightsabers/**/*\\.png")
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
            it.data.map { lightsaber ->
                val iconFilePath = lightsaber.fileName.replace('/', File.separatorChar)

                if (lightsaber.isJarKai) {
                    List(lightsaber.lightsabers.size) { index: Int ->
                        listOf(
                            "${iconFilePath}_${lightsaber.lightsabers[index].id}.png",
                        )
                    }.flatten()
                } else {
                    listOf(
                        "$iconFilePath.png"
                    )
                }
            }.stream().flatMap { e -> e.stream() }.collect(toList())
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        val imagesNotReferences = imageFiles.filter {
            !iconPaths.contains(it) &&
                !iconWhitelist.contains(it) &&
                !it.endsWith("logo.png") &&
                !it.endsWith("logo@2x.png")
        }
        assertTrue(
            imagesNotReferences.isEmpty(),
            getFormattedImagesNotReferencedErrorMessage(imagesNotReferences),
        )
    }

    @Test
    fun `all lightsaber 2x icon resources should be used`() {
        // Arrange
        val starWarsFactions = StarWarsFactionHolder.lightsabersFactions
        val iconBasePath = "${File.separatorChar}icons"

        val matcher = FileSystems.getDefault().getPathMatcher("glob:**/lightsabers/**/*\\.png")
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
            it.data.map { lightsaber ->
                val iconFilePath = lightsaber.fileName.replace('/', File.separatorChar)
                if (lightsaber.isJarKai) {
                    List(lightsaber.lightsabers.size) { index: Int ->
                        listOf(
                            "${iconBasePath}_${lightsaber.lightsabers[index].id}.png",
                            "${iconFilePath}_${lightsaber.lightsabers[index].id}@2x.png"
                        )
                    }.flatten()
                } else {
                    listOf(
                        "$iconBasePath.png",
                        "$iconFilePath@2x.png"
                    )
                }
            }.stream().flatMap { e -> e.stream() }.collect(toList())
        }.stream().flatMap { it.stream() }.collect(toList())

        // Assert
        val imagesNotReferences = imageFiles.filter {
            !iconPaths.contains(it) && !iconWhitelist.contains(it)
        }
        assertTrue(
            imagesNotReferences.isEmpty(),
            getFormattedImagesNotReferencedErrorMessage(imagesNotReferences),
        )
    }

    //endregion

    //region Helper methods

    private val iconWhitelist = listOf(
        "vehicles${File.separatorChar}scoundrels${File.separatorChar}lady_luck@2x.png",
        "vehicles${File.separatorChar}scoundrels${File.separatorChar}outrider@2x.png",
        "vehicles${File.separatorChar}scoundrels${File.separatorChar}outrider_r@2x.png",
        "vehicles${File.separatorChar}bounty_hunters${File.separatorChar}shadow_caster@2x.png",
        "vehicles${File.separatorChar}bounty_hunters${File.separatorChar}hounds_tooth_r@2x.png",
        "vehicles${File.separatorChar}mandalorians${File.separatorChar}fang_class_starfighter_r@2x.png",
        "vehicles${File.separatorChar}mandalorians${File.separatorChar}gauntlet_r@2x.png",
        "vehicles${File.separatorChar}scoundrels${File.separatorChar}nightbrother_r@2x.png",
        "vehicles${File.separatorChar}sith_empire${File.separatorChar}harrower_class_dreadnought_r_raw@2x.png",
        "vehicles${File.separatorChar}galactic_empire${File.separatorChar}at_pt_r@2x.png",
        "vehicles${File.separatorChar}galactic_empire${File.separatorChar}atpt.png",
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
