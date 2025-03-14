package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.models.vehicles.StarWarsVehicle
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.awt.color.ColorSpace

class CreateStarWarsProgressConfigurationComponentTests {
    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(StarWarsPersistentStateComponent)
        mockkObject(StarWarsFactionHolder)
        mockkStatic(StarWarsResourceLoader::class)

        val starWarsPersistentStateComponentMock = mockk<StarWarsPersistentStateComponent>(relaxed = true)
        every { StarWarsPersistentStateComponent.instance } returns starWarsPersistentStateComponentMock
        every { starWarsPersistentStateComponentMock.state } returns null
        every { StarWarsFactionHolder.missingVehicle } returns StarWarsVehicle("missing", "green", 0, 0, 0f)

        every { StarWarsResourceLoader.getFactionLogo(any(), any()) } returns mockk {
            every { height } returns 10
            every { width } returns 10
            every { colorModel } returns mockk {
                every { isCompatibleRaster(any()) } returns true
                every { isAlphaPremultiplied } returns true
                every { hasAlpha() } returns true
                every { colorSpace } returns mockk {
                    every { type } returns ColorSpace.TYPE_RGB
                }
            }
            every { copyData(any()) } returns mockk {
                every { numBands } returns 0
                every { sampleModel } returns mockk()
            }
        }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

//    @Test
//    fun `createStarWarsProgressConfigurationComponent should return new StarWarsProgressConfigurationComponent`() {
//        // Arrange
//
//        // Act
//        val sut = createStarWarsProgressConfigurationComponent()
//
//        // Assert
//        assertAll(
//            { assertNotNull(sut) },
//            { assertTrue(sut is StarWarsProgressConfigurationComponent) }
//        )
//    }

    //endregion
}
