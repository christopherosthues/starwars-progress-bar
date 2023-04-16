package com.christopherosthues.starwarsprogressbar

import com.intellij.BundleBase
import com.intellij.idea.TestFor
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@TestFor(classes = [StarWarsBundle::class])
internal class StarWarsBundleTests {

    @BeforeEach
    fun setup() {
        mockkStatic(BundleBase::messageOrDefault)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(BundleBase::messageOrDefault)
    }

    @Test
    fun `message with no parameters`() {
        // Arrange
        val key = "no_params_key"
        val expectedMessage = "May the force be with you"
        every { BundleBase.messageOrDefault(any(), key, any()) } returns expectedMessage

        // Act
        val result = StarWarsBundle.message(key)

        // Assert
        assertEquals(expectedMessage, result)
        verify(exactly = 1) { BundleBase.messageOrDefault(any(), key, any()) }
    }

    @Test
    fun `message with parameters`() {
        // Arrange
        val key = "params_key"
        val expectedMessage = "Hello, Luke Skywalker"
        val name = "Luke"
        val surname = "Skywalker"
        every { BundleBase.messageOrDefault(any(), key, any(), name, surname) } returns expectedMessage

        // Act
        val result = StarWarsBundle.message(key, name, surname)

        // Assert
        assertEquals(expectedMessage, result)
        verify(exactly = 1) { BundleBase.messageOrDefault(any(), key, any(), name, surname) }
    }

    @Test
    fun `message with empty key`() {
        // Arrange
        val key = ""
        val expectedMessage = ""
        every { BundleBase.messageOrDefault(any(), key, any()) } returns expectedMessage

        // Act
        val result = StarWarsBundle.message(key)

        // Assert
        assertEquals(expectedMessage, result)
        verify() { BundleBase.messageOrDefault(any(), key, any()) }
    }

    @Test
    fun `message with invalid key`() {
        // Arrange
        val key = "invalid_key"
        val expectedMessage = "!invalid_key!"
        every { BundleBase.messageOrDefault(any(), key, any()) } returns expectedMessage

        // Act
        val result = StarWarsBundle.message(key)

        // Assert
        assertEquals(expectedMessage, result)
        verify(exactly = 1) { BundleBase.messageOrDefault(any(), key, any()) }
    }

    @Test
    fun `message with non-string parameter`() {
        // Arrange
        val key = "params_key"
        val expectedMessage = "Hello, 5 Skywalker"
        val name = 5
        val surname = "Skywalker"
        every { BundleBase.messageOrDefault(any(), key, any(), name, surname) } returns expectedMessage

        // Act
        val result = StarWarsBundle.message(key, name, surname)

        // Assert
        assertEquals(expectedMessage, result)
        verify(exactly = 1) { BundleBase.messageOrDefault(any(), key, any(), name, surname) }
    }
}
