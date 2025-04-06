package com.christopherosthues.starwarsprogressbar.models

import com.intellij.idea.TestFor
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@TestFor(classes = [StarWarsFactionHolder::class])
class StarWarsFactionHolderTests {
    // TODO: Add tests for lightsabers
    //region Fields

    private val missingVehicle = StarWarsVehicle("missing", "missing", 0, 1, 2f)

    //endregion

    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(StarWarsVehicle)

        every { StarWarsVehicle.missingVehicle } returns missingVehicle
    }

    @AfterEach
    fun tearDown() {
        StarWarsFactionHolder.updateFactions(StarWarsFactions(listOf(), listOf()))

        unmockkAll()
    }

    //endregion

    //region Constructor tests

    @Test
    fun `constructor should set factions to empty list`() {
        // Arrange

        // Act and Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.vehicleFactions.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.lightsabersFactions.isEmpty()) },
        )
    }

    @Test
    fun `constructor should set default factions to empty list`() {
        // Arrange

        // Act and Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabersFactions.isEmpty()) },
        )
    }

    @Test
    fun `constructor should set default vehicles and lightsabers to empty list`() {
        // Arrange

        // Act and Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabers.isEmpty()) },
        )
    }

    @Test
    fun `constructor should set missing vehicle to default missing vehicle`() {
        // Arrange

        // Act and Assert
        assertDefaultMissingVehicle()
    }

    //endregion

    //region factions tests

    @Test
    fun `factions should return empty list`() {
        // Arrange

        // Act
        updateFactions(StarWarsFactions(listOf(), listOf()))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.vehicleFactions.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.lightsabersFactions.isEmpty()) },
        )
    }

    @Test
    fun `factions should return correct factions`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsVehicles()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsLightsabers()

        // Act
        StarWarsFactionHolder.updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertEquals(factionsVehicles, StarWarsFactionHolder.vehicleFactions) },
            { assertEquals(factionsLightsabers, StarWarsFactionHolder.lightsabersFactions) },
        )
    }

    //endregion

    //region defaultFactions tests

    @Test
    fun `default factions should return empty list if factions are empty`() {
        // Arrange

        // Act
        updateFactions(StarWarsFactions(listOf(), listOf()))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabersFactions.isEmpty()) },
        )
    }

    @Test
    fun `default factions should return empty list if the ids of all factions are empty and factions contain only one element`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdAndNoVehicles()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdAndNoLightsabers()

        // Act
        StarWarsFactionHolder.updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabersFactions.isEmpty()) },
        )
    }

    @Test
    fun `default factions should return empty list if the ids of all factions are empty and factions contain more than one element`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndNoVehicles()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndNoLightsabers()

        // Act
        StarWarsFactionHolder.updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabersFactions.isEmpty()) },
        )
    }

    @Test
    fun `default factions should return empty list if the ids of all factions are empty and factions contain more than one element and first faction contains no entities`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndFirstHasNoVehicles()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndFirstHasNoLightsabers()

        // Act
        StarWarsFactionHolder.updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabersFactions.isEmpty()) },
        )
    }

    @Test
    fun `default factions should return empty list if the ids of all factions are empty and factions contain more than one element and all factions contain entities`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsVehiclesWithEmptyIds()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsLightsabersWithEmptyIds()

        // Act
        StarWarsFactionHolder.updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabersFactions.isEmpty()) },
        )
    }

    @Test
    fun `default factions should return all factions with non-empty ids`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdContainingVehicles()
        val expectedFactionsVehicles = factionsVehicles.filter { it.id.isNotEmpty() }
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdContainingLightsabers()
        val expectedFactionsLightsabers = factionsLightsabers.filter { it.id.isNotEmpty() }

        // Act
        StarWarsFactionHolder.updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        val resultVehicles = StarWarsFactionHolder.defaultVehicleFactions
        val resultLightsabers = StarWarsFactionHolder.defaultLightsabersFactions
        assertAll(
            { assertTrue(resultVehicles.isNotEmpty()) },
            { assertEquals(2, resultVehicles.size) },
            { assertEquals(expectedFactionsVehicles, resultVehicles) },
            { assertTrue(resultLightsabers.isNotEmpty()) },
            { assertEquals(2, resultLightsabers.size) },
            { assertEquals(expectedFactionsLightsabers, resultLightsabers) },
        )
    }

    @Test
    fun `default factions should return all factions with non-empty ids if factions contain more than one faction with an empty id`() {
        // Arrange
        val factionsVehicles =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingVehicles()
        val expectedFactionsVehicles = factionsVehicles.filter { it.id.isNotEmpty() }
        val factionsLightsabers =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingLightsabers()
        val expectedFactionsLightsabers = factionsLightsabers.filter { it.id.isNotEmpty() }

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        val resultVehicles = StarWarsFactionHolder.defaultVehicleFactions
        val resultLightsabers = StarWarsFactionHolder.defaultLightsabersFactions
        assertAll(
            { assertTrue(resultVehicles.isNotEmpty()) },
            { assertEquals(2, resultVehicles.size) },
            { assertEquals(expectedFactionsVehicles, resultVehicles) },
            { assertTrue(resultLightsabers.isNotEmpty()) },
            { assertEquals(2, resultLightsabers.size) },
            { assertEquals(expectedFactionsLightsabers, resultLightsabers) },
        )
    }

    @Test
    fun `default factions should return all factions if no faction id is empty`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsVehicles()
        val expectedFactionsVehicles = factionsVehicles.filter { it.id.isNotEmpty() }
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsLightsabers()
        val expectedFactionsLightsabers = factionsLightsabers.filter { it.id.isNotEmpty() }

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        val resultVehicles = StarWarsFactionHolder.defaultVehicleFactions
        val resultLightsabers = StarWarsFactionHolder.defaultLightsabersFactions
        assertAll(
            { assertTrue(resultVehicles.isNotEmpty()) },
            { assertEquals(3, resultVehicles.size) },
            { assertEquals(expectedFactionsVehicles, resultVehicles) },
            { assertTrue(resultLightsabers.isNotEmpty()) },
            { assertEquals(3, resultLightsabers.size) },
            { assertEquals(expectedFactionsLightsabers, resultLightsabers) },
        )
    }

    //endregion

    //region missingVehicle tests

    @Test
    fun `missing vehicle should throw NoSuchElementException if factions are empty`() {
        // Arrange

        // Act
        updateFactions(StarWarsFactions(listOf(), listOf()))

        // Assert
        assertDefaultMissingVehicle()
    }

    @Test
    fun `missing vehicle should throw NoSuchElementException if all factions with empty id have no vehicles`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndNoVehicles()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndNoLightsabers()

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertDefaultMissingVehicle()
    }

    @Test
    fun `missing vehicle should throw NoSuchElementException if first factions with empty id has no vehicles`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndFirstHasNoVehicles()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndFirstHasNoLightsabers()

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertDefaultMissingVehicle()
    }

    @Test
    fun `missing vehicle should return first vehicle if first faction with empty id has one vehicle`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsVehiclesWithEmptyIds()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsLightsabersWithEmptyIds()
        val expectedVehicle = factionsVehicles.first().data.first()

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertDoesNotThrow { StarWarsFactionHolder.missingVehicle } },
            { assertEquals(expectedVehicle, StarWarsFactionHolder.missingVehicle) },
        )
    }

    @Test
    fun `missing vehicle should return first vehicle if first faction with empty id has more than one vehicle`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdContainingVehicles()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdContainingLightsabers()
        val expectedVehicle = factionsVehicles[1].data.first()

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertDoesNotThrow { StarWarsFactionHolder.missingVehicle } },
            { assertEquals(expectedVehicle, StarWarsFactionHolder.missingVehicle) },
        )
    }

    @Test
    fun `missing vehicle should return first vehicle if first faction with empty id has more than one vehicle and there are more than one factions with empty ids`() {
        // Arrange
        val factionsVehicles =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingVehicles()
        val factionsLightsabers =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingLightsabers()
        val expectedVehicle = factionsVehicles[0].data.first()

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertDoesNotThrow { StarWarsFactionHolder.missingVehicle } },
            { assertEquals(expectedVehicle, StarWarsFactionHolder.missingVehicle) },
        )
    }

    //endregion

    //region defaultVehicles tests

    @Test
    fun `default vehicles and lightsabers should return empty list if factions are empty`() {
        // Arrange

        // Act
        updateFactions(StarWarsFactions(listOf(), listOf()))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabers.isEmpty()) },
        )
    }

    @Test
    fun `default vehicles and lightsabers should return empty list if faction id is empty and faction contains no entities`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdAndNoVehicles()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdAndNoLightsabers()

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabers.isEmpty()) },
        )
    }

    @Test
    fun `default vehicles and lightsabers should return empty list if all faction ids are empty and faction contain no entities`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndNoVehicles()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndNoLightsabers()

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabers.isEmpty()) },
        )
    }

    @Test
    fun `default vehicles and lightsabers should return empty list if all faction ids are empty and one faction contains no entities`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndFirstHasNoVehicles()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndFirstHasNoLightsabers()

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabers.isEmpty()) },
        )
    }

    @Test
    fun `default vehicles and lightsabers should return empty list if all faction ids are empty and all factions contain entities`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsVehiclesWithEmptyIds()
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsLightsabersWithEmptyIds()

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty()) },
            { assertTrue(StarWarsFactionHolder.defaultLightsabers.isEmpty()) },
        )
    }

    @Test
    fun `default vehicles and lightsabers should return correct entities if one faction id is empty and all factions contain entities`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsWithEmptyIdContainingVehicles()
        val expectedVehicles = mutableListOf<StarWarsVehicle>()
        expectedVehicles.addAll(factionsVehicles.first().data)
        expectedVehicles.addAll(factionsVehicles[2].data)
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsWithEmptyIdContainingLightsabers()
        val expectedLightsabers = mutableListOf<Lightsabers>()
        expectedLightsabers.addAll(factionsLightsabers.first().data)
        expectedLightsabers.addAll(factionsLightsabers[2].data)

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertEquals(expectedVehicles, StarWarsFactionHolder.defaultVehicles) },
            { assertEquals(expectedLightsabers, StarWarsFactionHolder.defaultLightsabers) },
        )
    }

    @Test
    fun `default vehicles and lightsabers should return correct entities if more than one faction ids are empty and all factions contain entities`() {
        // Arrange
        val factionsVehicles =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingVehicles()
        val expectedVehicles = mutableListOf<StarWarsVehicle>()
        expectedVehicles.addAll(factionsVehicles[1].data)
        expectedVehicles.addAll(factionsVehicles[2].data)
        val factionsLightsabers =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingLightsabers()
        val expectedLightsabers = mutableListOf<Lightsabers>()
        expectedLightsabers.addAll(factionsLightsabers[1].data)
        expectedLightsabers.addAll(factionsLightsabers[2].data)

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertEquals(expectedVehicles, StarWarsFactionHolder.defaultVehicles) },
            { assertEquals(expectedLightsabers, StarWarsFactionHolder.defaultLightsabers) },
        )
    }

    @Test
    fun `default vehicles and lightsabers should return correct entities if more than one faction ids are empty and not all factions contain entities`() {
        // Arrange
        val factionsVehicles =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndOneFactionContainNoVehicles()
        val expectedVehicles = mutableListOf<StarWarsVehicle>()
        expectedVehicles.addAll(factionsVehicles[1].data)
        expectedVehicles.addAll(factionsVehicles[2].data)
        val factionsLightsabers =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndOneFactionContainNoLightsabers()
        val expectedLightsabers = mutableListOf<Lightsabers>()
        expectedLightsabers.addAll(factionsLightsabers[1].data)
        expectedLightsabers.addAll(factionsLightsabers[2].data)

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertEquals(expectedVehicles, StarWarsFactionHolder.defaultVehicles) },
            {
                expectedVehicles.addAll(factionsVehicles[4].data)
                assertEquals(expectedVehicles, StarWarsFactionHolder.defaultVehicles)
            },
            { assertEquals(expectedLightsabers, StarWarsFactionHolder.defaultLightsabers) },
            {
                expectedLightsabers.addAll(factionsLightsabers[4].data)
                assertEquals(expectedLightsabers, StarWarsFactionHolder.defaultLightsabers)
            },
        )
    }

    @Test
    fun `default vehicles and lightsabers should return all entities if all factions contain entities and all faction ids are not empty`() {
        // Arrange
        val factionsVehicles = FactionCreationHelper.createStarWarsFactionsVehicles()
        val expectedVehicles = factionsVehicles.fold(mutableListOf<StarWarsVehicle>()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.data)
            acc
        }
        val factionsLightsabers = FactionCreationHelper.createStarWarsFactionsLightsabers()
        val expectedLightsabers = factionsLightsabers.fold(mutableListOf<Lightsabers>()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.data)
            acc
        }

        // Act
        updateFactions(StarWarsFactions(factionsLightsabers, factionsVehicles))

        // Assert
        assertAll(
            { assertEquals(expectedVehicles, StarWarsFactionHolder.defaultVehicles) },
            { assertEquals(expectedLightsabers, StarWarsFactionHolder.defaultLightsabers) },
        )
    }

    //endregion

    //region Helper methods

    private fun updateFactions(factions: StarWarsFactions) {
        StarWarsFactionHolder.updateFactions(factions)
    }

    private fun assertDefaultMissingVehicle() {
        val missingVehicle = StarWarsFactionHolder.missingVehicle

        assertAll(
            { assertNotNull(missingVehicle) },
            { assertEquals(this.missingVehicle, missingVehicle) },
        )
    }

    //endregion
}
