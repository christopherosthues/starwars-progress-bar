package com.christopherosthues.starwarsprogressbar.constants

import com.christopherosthues.starwarsprogressbar.configuration.borders.BASE_MARGIN
import com.christopherosthues.starwarsprogressbar.configuration.borders.BORDER_LINE_MARGIN
import com.christopherosthues.starwarsprogressbar.configuration.borders.BORDER_MARGIN
import com.christopherosthues.starwarsprogressbar.configuration.borders.ICON_TEXT_SPACING
import com.christopherosthues.starwarsprogressbar.configuration.borders.LEFT_RIGHT_BORDER_MARGIN
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class BorderConstantsTests {
    @Test
    fun `border constants should return correct values`() {
        // Arrange

        // Act and Assert
        assertAll(
            { assertEquals(2, BASE_MARGIN) },
            { assertEquals(5, ICON_TEXT_SPACING) },
            { assertEquals(6, BORDER_MARGIN) },
            { assertEquals(7, LEFT_RIGHT_BORDER_MARGIN) },
            { assertEquals(3, BORDER_LINE_MARGIN) }
        )
    }
}
