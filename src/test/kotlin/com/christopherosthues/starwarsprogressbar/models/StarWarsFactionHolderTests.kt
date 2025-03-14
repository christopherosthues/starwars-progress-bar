package com.christopherosthues.starwarsprogressbar.models

import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicleFaction
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
        StarWarsFactionHolder.updateFactions(listOf())

        unmockkAll()
    }

    //endregion

    //region Constructor tests

    @Test
    fun `constructor should set factions to empty list`() {
        // Arrange

        // Act and Assert
        assertTrue(StarWarsFactionHolder.vehicleFactions.isEmpty())
    }

    @Test
    fun `constructor should set default factions to empty list`() {
        // Arrange

        // Act and Assert
        assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty())
    }

    @Test
    fun `constructor should set default vehicles to empty list`() {
        // Arrange

        // Act and Assert
        assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty())
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
        updateFactions(listOf())

        // Assert
        assertTrue(StarWarsFactionHolder.vehicleFactions.isEmpty())
    }

    @Test
    fun `factions should return correct factions`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactions()

        // Act
        StarWarsFactionHolder.updateFactions(factions)

        // Assert
        assertEquals(factions, StarWarsFactionHolder.vehicleFactions)
    }

    //endregion

    //region defaultFactions tests

    @Test
    fun `default factions should return empty list if factions are empty`() {
        // Arrange

        // Act
        updateFactions(listOf())

        // Assert
        assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty())
    }

    @Test
    fun `default factions should return empty list if the ids of all factions are empty and factions contain only one element`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdAndNoVehicles()

        // Act
        StarWarsFactionHolder.updateFactions(factions)

        // Assert
        assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty())
    }

    @Test
    fun `default factions should return empty list if the ids of all factions are empty and factions contain more than one element`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndNoVehicles()

        // Act
        StarWarsFactionHolder.updateFactions(factions)

        // Assert
        assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty())
    }

    @Test
    fun `default factions should return empty list if the ids of all factions are empty and factions contain more than one element and first faction contains no vehicles`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndFirstHasNoVehicles()

        // Act
        StarWarsFactionHolder.updateFactions(factions)

        // Assert
        assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty())
    }

    @Test
    fun `default factions should return empty list if the ids of all factions are empty and factions contain more than one element and all factions contain vehicles`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIds()

        // Act
        StarWarsFactionHolder.updateFactions(factions)

        // Assert
        assertTrue(StarWarsFactionHolder.defaultVehicleFactions.isEmpty())
    }

    @Test
    fun `default factions should return all factions with non-empty ids`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdContainingVehicles()
        val expectedFactions = factions.filter { it.id.isNotEmpty() }

        // Act
        StarWarsFactionHolder.updateFactions(factions)

        // Assert
        val result = StarWarsFactionHolder.defaultVehicleFactions
        assertAll(
            { assertTrue(result.isNotEmpty()) },
            { assertEquals(2, result.size) },
            { assertEquals(expectedFactions, result) },
        )
    }

    @Test
    fun `default factions should return all factions with non-empty ids if factions contain more than one faction with an empty id`() {
        // Arrange
        val factions =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingVehicles()
        val expectedFactions = factions.filter { it.id.isNotEmpty() }

        // Act
        updateFactions(factions)

        // Assert
        val result = StarWarsFactionHolder.defaultVehicleFactions
        assertAll(
            { assertTrue(result.isNotEmpty()) },
            { assertEquals(2, result.size) },
            { assertEquals(expectedFactions, result) },
        )
    }

    @Test
    fun `default factions should return all factions if no faction id is empty`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactions()
        val expectedFactions = factions.filter { it.id.isNotEmpty() }

        // Act
        updateFactions(factions)

        // Assert
        val result = StarWarsFactionHolder.defaultVehicleFactions
        assertAll(
            { assertTrue(result.isNotEmpty()) },
            { assertEquals(3, result.size) },
            { assertEquals(expectedFactions, result) },
        )
    }

    //endregion

    //region missingVehicle tests

    @Test
    fun `missing vehicle should throw NoSuchElementException if factions are empty`() {
        // Arrange

        // Act
        updateFactions(listOf())

        // Assert
        assertDefaultMissingVehicle()
    }

    @Test
    fun `missing vehicle should throw NoSuchElementException if all factions with empty id have no vehicles`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndNoVehicles()

        // Act
        updateFactions(factions)

        // Assert
        assertDefaultMissingVehicle()
    }

    @Test
    fun `missing vehicle should throw NoSuchElementException if first factions with empty id has no vehicles`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndFirstHasNoVehicles()

        // Act
        updateFactions(factions)

        // Assert
        assertDefaultMissingVehicle()
    }

    @Test
    fun `missing vehicle should return first vehicle if first faction with empty id has one vehicle`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIds()
        val expectedVehicle = factions.first().vehicles.first()

        // Act
        updateFactions(factions)

        // Assert
        assertAll(
            { assertDoesNotThrow { StarWarsFactionHolder.missingVehicle } },
            { assertEquals(expectedVehicle, StarWarsFactionHolder.missingVehicle) },
        )
    }

    @Test
    fun `missing vehicle should return first vehicle if first faction with empty id has more than one vehicle`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdContainingVehicles()
        val expectedVehicle = factions[1].vehicles.first()

        // Act
        updateFactions(factions)

        // Assert
        assertAll(
            { assertDoesNotThrow { StarWarsFactionHolder.missingVehicle } },
            { assertEquals(expectedVehicle, StarWarsFactionHolder.missingVehicle) },
        )
    }

    @Test
    fun `missing vehicle should return first vehicle if first faction with empty id has more than one vehicle and there are more than one factions with empty ids`() {
        // Arrange
        val factions =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingVehicles()
        val expectedVehicle = factions[0].vehicles.first()

        // Act
        updateFactions(factions)

        // Assert
        assertAll(
            { assertDoesNotThrow { StarWarsFactionHolder.missingVehicle } },
            { assertEquals(expectedVehicle, StarWarsFactionHolder.missingVehicle) },
        )
    }

    //endregion

    //region defaultVehicles tests

    @Test
    fun `default vehicles should return empty list if factions are empty`() {
        // Arrange

        // Act
        updateFactions(listOf())

        // Assert
        assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty())
    }

    @Test
    fun `default vehicles should return empty list if faction id is empty and faction contains no vehicles`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdAndNoVehicles()

        // Act
        updateFactions(factions)

        // Assert
        assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty())
    }

    @Test
    fun `default vehicles should return empty list if all faction ids are empty and faction contain no vehicles`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndNoVehicles()

        // Act
        updateFactions(factions)

        // Assert
        assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty())
    }

    @Test
    fun `default vehicles should return empty list if all faction ids are empty and one faction contains no vehicles`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdsAndFirstHasNoVehicles()

        // Act
        updateFactions(factions)

        // Assert
        assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty())
    }

    @Test
    fun `default vehicles should return empty list if all faction ids are empty and all factions contain vehicles`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIds()

        // Act
        updateFactions(factions)

        // Assert
        assertTrue(StarWarsFactionHolder.defaultVehicles.isEmpty())
    }

    @Test
    fun `default vehicles should return correct vehicles if one faction id is empty and all factions contain vehicles`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactionsWithEmptyIdContainingVehicles()
        val expectedVehicles = mutableListOf<StarWarsVehicle>()
        expectedVehicles.addAll(factions.first().vehicles)
        expectedVehicles.addAll(factions[2].vehicles)

        // Act
        updateFactions(factions)

        // Assert
        assertEquals(expectedVehicles, StarWarsFactionHolder.defaultVehicles)
    }

    @Test
    fun `default vehicles should return correct vehicles if more than one faction ids are empty and all factions contain vehicles`() {
        // Arrange
        val factions =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndContainingVehicles()
        val expectedVehicles = mutableListOf<StarWarsVehicle>()
        expectedVehicles.addAll(factions[1].vehicles)
        expectedVehicles.addAll(factions[2].vehicles)

        // Act
        updateFactions(factions)

        // Assert
        assertEquals(expectedVehicles, StarWarsFactionHolder.defaultVehicles)
    }

    @Test
    fun `default vehicles should return correct vehicles if more than one faction ids are empty and not all factions contain vehicles`() {
        // Arrange
        val factions =
            FactionCreationHelper.createStarWarsFactionsWithMultipleFactionsWithEmptyIdAndOneFactionContainNoVehicles()
        val expectedVehicles = mutableListOf<StarWarsVehicle>()
        expectedVehicles.addAll(factions[1].vehicles)
        expectedVehicles.addAll(factions[2].vehicles)

        // Act
        updateFactions(factions)

        // Assert
        assertAll(
            { assertEquals(expectedVehicles, StarWarsFactionHolder.defaultVehicles) },
            {
                expectedVehicles.addAll(factions[4].vehicles)
                assertEquals(expectedVehicles, StarWarsFactionHolder.defaultVehicles)
            },
        )
    }

    @Test
    fun `default vehicles should return all vehicles if all factions contain vehicles and all faction ids are not empty`() {
        // Arrange
        val factions = FactionCreationHelper.createStarWarsFactions()
        val expectedVehicles = factions.fold(mutableListOf<StarWarsVehicle>()) { acc, starWarsFaction ->
            acc.addAll(starWarsFaction.vehicles)
            acc
        }

        // Act
        updateFactions(factions)

        // Assert
        assertEquals(expectedVehicles, StarWarsFactionHolder.defaultVehicles)
    }

    //endregion

    //region Helper methods

    private fun updateFactions(factions: List<StarWarsVehicleFaction>) {
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
