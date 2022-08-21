package com.christopherosthues.starwarsprogressbar.notification

import com.intellij.idea.TestFor
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

@TestFor(classes = [GotItService::class])
class GotItServiceTests {
    //region Test lifecycle

    @AfterEach
    fun tearDown() {
        GotItService.reset()
    }

    //endregion

    //region Tests

    @Test
    fun `showGotItTooltips should show all added tooltips`() {
        // Arrange
        val gotIt1Mock = mockk<GotIt>()
        justRun { gotIt1Mock.show() }
        GotItService.addGotItMessage(gotIt1Mock)

        // Act
        GotItService.showGotItTooltips()

        // Assert
        verify(exactly = 1) { gotIt1Mock.show() }

        // Arrange
        val gotIt2Mock = mockk<GotIt>()
        val gotIt3Mock = mockk<GotIt>()
        justRun { gotIt2Mock.show() }
        justRun { gotIt3Mock.show() }
        GotItService.addGotItMessage(gotIt2Mock)
        GotItService.addGotItMessage(gotIt3Mock)

        // Act
        GotItService.showGotItTooltips()

        // Assert
        verify(exactly = 2) { gotIt1Mock.show() }
        verify(exactly = 1) { gotIt2Mock.show() }
        verify(exactly = 1) { gotIt3Mock.show() }
    }

    @Test
    fun `reset should remove all tooltips`() {
        // Arrange
        val gotIt1Mock = mockk<GotIt>()
        justRun { gotIt1Mock.show() }
        GotItService.addGotItMessage(gotIt1Mock)
        GotItService.showGotItTooltips()

        verify(exactly = 1) { gotIt1Mock.show() }

        // Act
        GotItService.reset()
        GotItService.showGotItTooltips()

        // Assert
        verify(exactly = 1) { gotIt1Mock.show() }
    }

    //endregion
}
