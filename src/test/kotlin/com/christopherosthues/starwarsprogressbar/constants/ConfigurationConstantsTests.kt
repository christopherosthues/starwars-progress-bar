package com.christopherosthues.starwarsprogressbar.constants

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class ConfigurationConstantsTests {
    @Test
    fun `configuration constants should return correct values`() {
        // Arrange

        // Act and Assert
        assertAll(
            { Assertions.assertTrue(DEFAULT_SHOW_VEHICLE) },
            { Assertions.assertTrue(DEFAULT_ENABLE_NEW_VEHICLES) },
            { Assertions.assertTrue(DEFAULT_SHOW_TOOLTIPS) },
            { Assertions.assertFalse(DEFAULT_SHOW_VEHICLE_NAMES) },
            { Assertions.assertFalse(DEFAULT_SHOW_FACTION_CRESTS) },
            { Assertions.assertFalse(DEFAULT_SAME_VEHICLE_VELOCITY) },
            { Assertions.assertFalse(DEFAULT_SOLID_PROGRESS_BAR_COLOR) }
        )
    }
}
