package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class CreateStarWarsProgressConfigurationComponentTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(StarWarsPersistentStateComponent)
        mockkObject(FactionHolder)

        val starWarsPersistentStateComponentMock = mockk<StarWarsPersistentStateComponent>(relaxed = true)
        every { StarWarsPersistentStateComponent.instance } returns starWarsPersistentStateComponentMock
        every { starWarsPersistentStateComponentMock.state } returns null
        every { FactionHolder.missingVehicle } returns StarWarsVehicle("missing", "green", 0, 0, 0f)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @Test
    fun `createStarWarsProgressConfigurationComponent should return new StarWarsProgressConfigurationComponent`() {
        // Arrange

        // Act
        val sut = createStarWarsProgressConfigurationComponent()

        // Assert
        assertAll(
            { assertNotNull(sut) },
            { assertTrue(sut is StarWarsProgressConfigurationComponent) }
        )
    }

    //endregion
}
