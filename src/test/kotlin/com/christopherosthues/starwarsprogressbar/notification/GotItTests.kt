package com.christopherosthues.starwarsprogressbar.notification

import com.intellij.idea.TestFor
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.GotItTooltip
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.awt.Component
import java.awt.Point
import javax.swing.JComponent

@TestFor(classes = [GotIt::class])
class GotItTests {
    @Test
    fun `show should display tooltip within correct component and correct position`() {
        // Arrange
        val tooltipMock = mockk<GotItTooltip>(relaxed = true)
        val componentMock = mockk<JComponent>(relaxed = true)
        val pointProviderMock = mockk<(Component, Balloon) -> Point>(relaxed = true)
        val sut = GotIt(tooltipMock, componentMock, pointProviderMock)

        // Act
        sut.show()

        // Assert
        verify(exactly = 1) { tooltipMock.show(componentMock, pointProviderMock) }
    }
}
