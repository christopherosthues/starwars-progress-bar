package com.christopherosthues.starwarsprogressbar.constants

import com.christopherosthues.starwarsprogressbar.selectors.SelectionType
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class ConfigurationConstantsTests {
    @Test
    fun `configuration constants should return correct values`() {
        // Arrange

        // Act and Assert
        assertAll(
            { Assertions.assertTrue(DEFAULT_SHOW_ICON) },
            { Assertions.assertTrue(DEFAULT_ENABLE_NEW) },
            { Assertions.assertTrue(DEFAULT_SHOW_TOOLTIPS) },
            { Assertions.assertFalse(DEFAULT_SHOW_NAMES) },
            { Assertions.assertFalse(DEFAULT_SHOW_FACTION_CRESTS) },
            { Assertions.assertFalse(DEFAULT_SAME_VELOCITY) },
            { Assertions.assertFalse(DEFAULT_SOLID_PROGRESS_BAR_COLOR) },
            { Assertions.assertFalse(DEFAULT_DRAW_SILHOUETTES) },
            { Assertions.assertFalse(DEFAULT_CHANGE_AFTER_PASS) },
            { Assertions.assertEquals(SelectionType.RANDOM_ALL, DEFAULT_SELECTOR) },
            { Assertions.assertEquals(2, DEFAULT_NUMBER_OF_PASSES_UNTIL_CHANGE) },
        )
    }
}
