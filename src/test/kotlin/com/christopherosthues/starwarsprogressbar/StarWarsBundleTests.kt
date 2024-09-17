package com.christopherosthues.starwarsprogressbar

import com.intellij.idea.TestFor
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.util.*

@TestFor(classes = [StarWarsBundle::class])
internal class StarWarsBundleTests {
    @Test
    @Disabled("ResourceBundle has to be mocked")
    fun `message with no parameters`() {
        // Arrange
        val key = "no_params_key"
        val expectedMessage = "May the force be with you"
        every { ResourceBundle.getBundle(any(), any<Locale>()).getString(key) } returns expectedMessage

        // Act
        val result = StarWarsBundle.message(key)

        // Assert
        assertEquals(expectedMessage, result)
        verify(exactly = 1) { ResourceBundle.getBundle(any(), any<Locale>()).getString(key) }
    }

    @Test
    @Disabled("ResourceBundle has to be mocked")
    fun `message with parameters`() {
        // Arrange
        val key = "params_key"
        val expectedMessage = "Hello, Luke Skywalker"
        val name = "Luke"
        val surname = "Skywalker"
        every { ResourceBundle.getBundle(any(), any<Locale>()).getString(key) } returns expectedMessage

        // Act
        val result = StarWarsBundle.message(key, name, surname)

        // Assert
        assertEquals(expectedMessage, result)
        verify(exactly = 1) { ResourceBundle.getBundle(any(), any<Locale>()).getString(key) }
    }

    @Test
    @Disabled("ResourceBundle has to be mocked")
    fun `message with empty key`() {
        // Arrange
        val key = ""
        val expectedMessage = ""
        every { ResourceBundle.getBundle(any(), any<Locale>()).getString(key) } returns expectedMessage

        // Act
        val result = StarWarsBundle.message(key)

        // Assert
        assertEquals(expectedMessage, result)
        verify(exactly = 1) { ResourceBundle.getBundle(any(), any<Locale>()).getString(key) }
    }

    @Test
    @Disabled("ResourceBundle has to be mocked")
    fun `message with invalid key`() {
        // Arrange
        val key = "invalid_key"
        val expectedMessage = "!invalid_key!"
        every { ResourceBundle.getBundle(any(), any<Locale>()).getString(key) } returns expectedMessage

        // Act
        val result = StarWarsBundle.message(key)

        // Assert
        assertEquals(expectedMessage, result)
        verify(exactly = 1) { ResourceBundle.getBundle(any(), any<Locale>()).getString(key) }
    }

    @Test
    @Disabled("ResourceBundle has to be mocked")
    fun `message with non-string parameter`() {
        // Arrange
        val key = "params_key"
        val expectedMessage = "Hello, 5 Skywalker"
        val name = 5
        val surname = "Skywalker"
        every { ResourceBundle.getBundle(any(), any<Locale>()).getString(key) } returns expectedMessage

        // Act
        val result = StarWarsBundle.message(key, name, surname)

        // Assert
        assertEquals(expectedMessage, result)
        verify(exactly = 1) { ResourceBundle.getBundle(any(), any<Locale>()).getString(key) }
    }
}
