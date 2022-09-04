package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsPersistentStateComponent
import com.christopherosthues.starwarsprogressbar.util.VehicleSelector
import com.intellij.idea.TestFor
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.ui.JBEmptyBorder
import com.intellij.util.ui.JBEmptyBorder.JBEmptyBorderUIResource
import com.intellij.util.ui.JBUI
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JComponent

@TestFor(classes = [StarWarsProgressBarFactory::class])
class StarWarsProgressBarFactoryTests {
    //region Fields

    private lateinit var componentMock: JComponent

    //endregion

    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(VehicleSelector)
        mockkStatic(JBUI.Borders::class)
        mockkStatic(ApplicationManager::class)
        val starWarsPersistentStateComponent = mockk<StarWarsPersistentStateComponent>()

        componentMock = mockk()

        justRun { componentMock.border = any() }
        every { VehicleSelector.selectRandomVehicle() } returns mockk(relaxed = true)
        every {
            ApplicationManager.getApplication().getService(StarWarsPersistentStateComponent::class.java)
        } returns starWarsPersistentStateComponent
        every { starWarsPersistentStateComponent.state } returns mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    @Test
    fun `createUI should set border of component to empty border`() {
        // Arrange
        val borderMock = mockk<JBEmptyBorder>()
        val borderUIMock = mockk<JBEmptyBorderUIResource>()
        every { borderMock.asUIResource() } returns borderUIMock
        every { JBUI.Borders.empty() } returns borderMock

        // Act
        StarWarsProgressBarFactory.createUI(componentMock)

        // Assert
        verify(exactly = 1) { componentMock.border = borderUIMock }
        verify(exactly = 1) { JBUI.Borders.empty() }
        verify(exactly = 1) { borderMock.asUIResource() }
    }

    @Test
    fun `createUI should create star wars progress bar ui with random vehicle`() {
        // Arrange

        // Act
        val result = StarWarsProgressBarFactory.createUI(componentMock)

        // Assert
        assertTrue(result is StarWarsProgressBarUI)
        verify(exactly = 1) { VehicleSelector.selectRandomVehicle() }
    }
}
