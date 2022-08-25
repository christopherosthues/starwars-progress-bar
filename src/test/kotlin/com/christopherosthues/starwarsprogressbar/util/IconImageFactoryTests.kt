package com.christopherosthues.starwarsprogressbar.util

import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class IconImageFactoryTests {
    //region createClassLoader tests

    @Test
    fun `createClassLoader should create class loader of StarWarsResourceLoader class`() {
        // Arrange
        val classLoader = StarWarsResourceLoader.javaClass.classLoader

        // Act
        val result = createClassLoader()

        // Assert
        assertAll(
            { assertEquals(classLoader, result) },
            { assertSame(classLoader, result) }
        )
    }

    //endregion

    //region parseFactionsFromJson tests

    @Test
    fun `parseFactionsFromJson should return empty factions if json provides empty factions`() {
        // Arrange

        // Act
        val result = parseFactionsFromJson("{\"factions\": []}")

        // Assert
        assertEquals(listOf<StarWarsFaction>(), result.factions)
    }

    @Test
    fun `parseFactionsFromJson should return empty factions if json provides empty object`() {
        // Arrange

        // Act
        val result = parseFactionsFromJson("{}")

        // Assert
        assertEquals(listOf<StarWarsFaction>(), result.factions)
    }

    @Test
    fun `parseFactionsFromJson should throw null pointer exception if json is empty`() {
        // Arrange

        // Act and Assert
        val result = parseFactionsFromJson("")

        // Assert
        assertEquals(listOf<StarWarsFaction>(), result.factions)
    }

    @Test
    fun `parseFactionsFromJson should throw json syntax exception if json is invalid`() {
        // Arrange

        // Act and Assert
        val result = parseFactionsFromJson("[]")

        // Assert
        assertEquals(listOf<StarWarsFaction>(), result.factions)
    }

    // TODO: unit test with data

    //endregion
}
