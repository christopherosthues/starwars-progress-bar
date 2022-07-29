package com.christopherosthues.starwarsprogressbar.util

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.RepeatedTest

class RandomIntTests {
    @RepeatedTest(20)
    fun `randomInt should return random integer between zero and provided value`() {
        // Arrange
        val until = 10

        // Act
        val randomInt = randomInt(until)

        // Assert
        assertTrue(randomInt in 0..until, "$randomInt not in range 0..$until")
    }
}
