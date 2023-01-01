package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.christopherosthues.starwarsprogressbar.models.FactionHolder
import com.intellij.idea.TestFor
import com.intellij.openapi.options.ConfigurationException
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import javax.swing.JPanel

@TestFor(classes = [StarWarsProgressConfigurable::class])
class StarWarsProgressConfigurableTests {
    //region Fields

    private lateinit var starWarsProgressConfigurationComponentMock: StarWarsProgressConfigurationComponent

    //endregion

    //region Test lifecycle

    @BeforeEach
    fun setup() {
        mockkObject(FactionHolder)
        mockkObject(StarWarsPersistentStateComponent)
        mockkStatic(::createStarWarsProgressConfigurationComponent)

        every { FactionHolder.defaultVehicles } returns listOf()
        starWarsProgressConfigurationComponentMock = mockk(relaxed = true)
        every { createStarWarsProgressConfigurationComponent() } returns starWarsProgressConfigurationComponentMock
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    //region createComponent tests

    @Test
    fun `createComponent should create component using factory`() {
        // Arrange
        val sut = StarWarsProgressConfigurable()

        // Act
        sut.createComponent()

        // Assert
        verify(exactly = 1) { createStarWarsProgressConfigurationComponent() }
    }

    @Test
    fun `createComponent should return panel of created component`() {
        // Arrange
        val sut = StarWarsProgressConfigurable()
        val panelMock = mockk<JPanel>(relaxed = true)
        every { starWarsProgressConfigurationComponentMock.panel } returns panelMock

        // Act
        val result = sut.createComponent()

        // Assert
        assertAll({ assertEquals(panelMock, result) }, { assertSame(panelMock, result) })
    }

    //endregion

    //region isModified tests

    @Test
    fun `isModified should return false if component is null`() {
        // Arrange
        setupStarWarsPersistentStateComponentMock()
        val sut = StarWarsProgressConfigurable()

        // Act
        val result = sut.isModified

        // Assert
        assertFalse(result)
    }

    @Test
    fun `isModified should return false if star wars state is null`() {
        // Arrange
        setupStarWarsPersistentStateComponentMock(false)
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()

        // Act
        val result = sut.isModified

        // Assert
        assertFalse(result)
    }

    @ParameterizedTest
    @MethodSource("isNotModifiedValues")
    fun `isModified should return false if star wars state and component properties are all equal`(
        enabledVehicles: Map<String, Boolean>,
        showVehicle: Boolean,
        showVehicleNames: Boolean,
        showToolTips: Boolean,
        showFactionCrests: Boolean,
        sameVehicleVelocity: Boolean,
        enableNewVehicles: Boolean,
        solidProgressBarColor: Boolean
    ) {
        // Arrange
        setupStarWarsState(
            enabledVehicles,
            showVehicle,
            showVehicleNames,
            showToolTips,
            showFactionCrests,
            sameVehicleVelocity,
            enableNewVehicles,
            solidProgressBarColor
        )
        setupComponentState(
            enabledVehicles,
            showVehicle,
            showVehicleNames,
            showToolTips,
            showFactionCrests,
            sameVehicleVelocity,
            enableNewVehicles,
            solidProgressBarColor
        )
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()

        // Act
        val result = sut.isModified

        // Assert
        assertFalse(result)
    }

    @ParameterizedTest
    @MethodSource("isModifiedValues")
    fun `isModified should return true if star wars state and component properties are not all equal`(
        starWarsStateData: IsModifiedData,
        componentStateData: IsModifiedData
    ) {
        // Arrange
        setupStarWarsState(
            starWarsStateData.enabledVehicles,
            starWarsStateData.showVehicle,
            starWarsStateData.showVehicleNames,
            starWarsStateData.showToolTips,
            starWarsStateData.showFactionCrests,
            starWarsStateData.sameVehicleVelocity,
            starWarsStateData.enableNewVehicles,
            starWarsStateData.solidProgressBarColor
        )
        setupComponentState(
            componentStateData.enabledVehicles,
            componentStateData.showVehicle,
            componentStateData.showVehicleNames,
            componentStateData.showToolTips,
            componentStateData.showFactionCrests,
            componentStateData.sameVehicleVelocity,
            componentStateData.enableNewVehicles,
            componentStateData.solidProgressBarColor
        )
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()

        // Act
        val result = sut.isModified

        // Assert
        assertTrue(result)
    }

    //endregion

    //region apply tests

    @Test
    fun `apply should throw configuration exception if star wars state is null`() {
        // Arrange
        setupStarWarsPersistentStateComponentMock(false)
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()

        // Act and Assert
        val thrownException = assertThrows(ConfigurationException::class.java) { sut.apply() }
        assertEquals("The configuration state cannot be null!", thrownException.message)
    }

    @Test
    fun `apply should not update star wars state if component is null`() {
        // Arrange
        val starWarsStateMock = setupStarWarsPersistentStateComponentMock().state!!
        val enabledVehicles = mapOf("1" to true, "2" to false)
        starWarsStateMock.vehiclesEnabled = enabledVehicles
        val sut = StarWarsProgressConfigurable()

        // Act
        sut.apply()

        // Assert
        assertEquals(enabledVehicles, starWarsStateMock.vehiclesEnabled)
        verify(exactly = 0) { starWarsStateMock.showVehicle }
        verify(exactly = 0) { starWarsStateMock.showVehicleNames }
        verify(exactly = 0) { starWarsStateMock.showToolTips }
        verify(exactly = 0) { starWarsStateMock.showFactionCrests }
        verify(exactly = 0) { starWarsStateMock.sameVehicleVelocity }
        verify(exactly = 0) { starWarsStateMock.enableNewVehicles }
        verify(exactly = 0) { starWarsStateMock.solidProgressBarColor }
    }

    @ParameterizedTest
    @MethodSource("isNotModifiedValues")
    fun `apply should update star wars state`(
        enabledVehicles: Map<String, Boolean> = mapOf(),
        showVehicle: Boolean = false,
        showVehicleNames: Boolean = false,
        showToolTips: Boolean = false,
        showFactionCrests: Boolean = false,
        sameVehicleVelocity: Boolean = false,
        enableNewVehicles: Boolean = false,
        solidProgressBarColor: Boolean = false
    ) {
        // Arrange
        val starWarsStateMock = setupStarWarsPersistentStateComponentMock().state!!
        setupComponentState(
            enabledVehicles,
            showVehicle,
            showVehicleNames,
            showToolTips,
            showFactionCrests,
            sameVehicleVelocity,
            enableNewVehicles,
            solidProgressBarColor
        )
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()

        // Act
        sut.apply()

        // Assert
        verify(exactly = 1) { starWarsStateMock.showVehicle = showVehicle }
        verify(exactly = 1) { starWarsStateMock.showVehicleNames = showVehicleNames }
        verify(exactly = 1) { starWarsStateMock.showToolTips = showToolTips }
        verify(exactly = 1) { starWarsStateMock.showFactionCrests = showFactionCrests }
        verify(exactly = 1) { starWarsStateMock.sameVehicleVelocity = sameVehicleVelocity }
        verify(exactly = 1) { starWarsStateMock.enableNewVehicles = enableNewVehicles }
        verify(exactly = 1) { starWarsStateMock.solidProgressBarColor = solidProgressBarColor }
        assertEquals(enabledVehicles, starWarsStateMock.vehiclesEnabled, StarWarsState::vehiclesEnabled.name)
    }

    //endregion

    //region getDisplayName tests

    @Test
    fun `getDisplayName should return correct plugin name`() {
        // Arrange
        mockkStatic(StarWarsBundle::class)
        val expectedDisplayName = "Star Wars Progress Bar"
        every { StarWarsBundle.message(BundleConstants.PLUGIN_NAME) } returns expectedDisplayName
        val sut = StarWarsProgressConfigurable()

        // Act
        val result = sut.displayName

        // Assert
        assertEquals(expectedDisplayName, result)
        verify(exactly = 1) { StarWarsBundle.message(BundleConstants.PLUGIN_NAME) }
    }

    //endregion

    //region getId tests

    @Test
    fun `getId should return correct plugin search id`() {
        // Arrange
        val sut = StarWarsProgressConfigurable()

        // Act
        val result = sut.id

        // Assert
        assertEquals(PluginConstants.PluginSearchId, result)
    }

    //endregion

    //region reset tests

    @Test
    fun `reset should retrieve star wars state`() {
        // Arrange
        val starWarsPersistentStateComponentMock = setupStarWarsPersistentStateComponentMock()
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()

        // Act
        sut.reset()

        // Assert
        verify(exactly = 1) { starWarsPersistentStateComponentMock.state }
    }

    @Test
    fun `reset should update ui of component`() {
        // Arrange
        val starWarsStateMock = setupStarWarsPersistentStateComponentMock().state
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()

        // Act
        sut.reset()

        // Assert
        verify(exactly = 1) { starWarsProgressConfigurationComponentMock.updateUI(starWarsStateMock) }
    }

    @Test
    fun `reset should update ui of component if star wars state is null`() {
        // Arrange
        setupStarWarsPersistentStateComponentMock(false)
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()

        // Act
        sut.reset()

        // Assert
        verify(exactly = 1) { starWarsProgressConfigurationComponentMock.updateUI(any()) }
    }

    @Test
    fun `reset should not update ui of component if component is null`() {
        // Arrange
        val starWarsPersistentStateComponentMock = setupStarWarsPersistentStateComponentMock()
        val sut = StarWarsProgressConfigurable()

        // Act
        sut.reset()

        // Assert
        verify(exactly = 1) { starWarsPersistentStateComponentMock.state }
        verify(exactly = 0) { starWarsProgressConfigurationComponentMock.updateUI(any()) }
    }

    //endregion

    //region disposeUIResources tests

    @Test
    fun `disposeUIResources should set component to null`() {
        // Arrange
        setupStarWarsPersistentStateComponentMock()
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()
        sut.reset()

        verify(exactly = 1) { starWarsProgressConfigurationComponentMock.updateUI(any()) }

        // Act
        sut.disposeUIResources()

        // Assert
        sut.reset()
        verify(exactly = 1) { starWarsProgressConfigurationComponentMock.updateUI(any()) }
    }

    //endregion

    //endregion

    //region Helper methods

    private fun setupStarWarsPersistentStateComponentMock(
        initializeStarWarsState: Boolean = true
    ): StarWarsPersistentStateComponent {
        val starWarsStateMock = mockk<StarWarsState>(relaxed = true)
        val starWarsPersistentStateComponentMock = mockk<StarWarsPersistentStateComponent>(relaxed = true)
        every { StarWarsPersistentStateComponent.instance } returns starWarsPersistentStateComponentMock
        every {
            starWarsPersistentStateComponentMock.state
        } returns if (initializeStarWarsState) starWarsStateMock else null

        return starWarsPersistentStateComponentMock
    }

    private fun setupStarWarsState(
        enabledVehicles: Map<String, Boolean> = mapOf(),
        showVehicle: Boolean = false,
        showVehicleNames: Boolean = false,
        showToolTips: Boolean = false,
        showFactionCrests: Boolean = false,
        sameVehicleVelocity: Boolean = false,
        enableNewVehicles: Boolean = false,
        solidProgressBarColor: Boolean = false
    ): StarWarsState {
        val starWarsStateMock = mockk<StarWarsState>()
        starWarsStateMock.vehiclesEnabled = enabledVehicles
        every { starWarsStateMock.showVehicle } returns showVehicle
        every { starWarsStateMock.showVehicleNames } returns showVehicleNames
        every { starWarsStateMock.showToolTips } returns showToolTips
        every { starWarsStateMock.showFactionCrests } returns showFactionCrests
        every { starWarsStateMock.sameVehicleVelocity } returns sameVehicleVelocity
        every { starWarsStateMock.enableNewVehicles } returns enableNewVehicles
        every { starWarsStateMock.solidProgressBarColor } returns solidProgressBarColor
        every { starWarsStateMock.version } returns ""
        val starWarsPersistentStateComponentMock = mockk<StarWarsPersistentStateComponent>(relaxed = true)
        every { StarWarsPersistentStateComponent.instance } returns starWarsPersistentStateComponentMock
        every { starWarsPersistentStateComponentMock.state } returns starWarsStateMock

        return starWarsStateMock
    }

    private fun setupComponentState(
        enabledVehicles: Map<String, Boolean> = mapOf(),
        showVehicle: Boolean = false,
        showVehicleNames: Boolean = false,
        showToolTips: Boolean = false,
        showFactionCrests: Boolean = false,
        sameVehicleVelocity: Boolean = false,
        enableNewVehicles: Boolean = false,
        solidProgressBarColor: Boolean = false
    ) {
        every { starWarsProgressConfigurationComponentMock.enabledVehicles } returns enabledVehicles
        every { starWarsProgressConfigurationComponentMock.showVehicle } returns showVehicle
        every { starWarsProgressConfigurationComponentMock.showVehicleNames } returns showVehicleNames
        every { starWarsProgressConfigurationComponentMock.showToolTips } returns showToolTips
        every { starWarsProgressConfigurationComponentMock.showFactionCrests } returns showFactionCrests
        every { starWarsProgressConfigurationComponentMock.sameVehicleVelocity } returns sameVehicleVelocity
        every { starWarsProgressConfigurationComponentMock.enableNewVehicles } returns enableNewVehicles
        every { starWarsProgressConfigurationComponentMock.solidProgressBarColor } returns solidProgressBarColor
    }

    //endregion

    //region Test case data

    data class IsModifiedData(
        val enabledVehicles: Map<String, Boolean> = mapOf(),
        val showVehicle: Boolean = false,
        val showVehicleNames: Boolean = false,
        val showToolTips: Boolean = false,
        val showFactionCrests: Boolean = false,
        val sameVehicleVelocity: Boolean = false,
        val enableNewVehicles: Boolean = false,
        val solidProgressBarColor: Boolean = false
    )

    companion object {
        @JvmStatic
        fun isNotModifiedValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(mapOf<String, Boolean>(), false, false, false, false, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, false, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, false, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, false, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, false, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, false, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, false, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, false, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, true, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, true, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, true, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, true, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, true, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, true, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, true, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, false, true, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, false, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, false, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, false, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, false, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, false, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, false, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, false, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, false, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, true, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, true, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, true, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, true, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, true, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, true, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, true, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, false, true, true, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, false, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, false, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, false, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, false, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, false, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, false, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, false, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, false, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, true, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, true, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, true, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, true, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, true, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, true, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, true, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, false, true, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, false, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, false, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, false, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, false, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, false, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, false, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, false, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, false, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, true, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, true, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, true, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, true, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, true, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, true, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, true, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), false, true, true, true, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, false, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, false, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, false, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, false, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, false, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, false, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, false, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, false, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, true, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, true, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, true, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, true, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, true, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, true, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, true, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, false, true, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, false, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, false, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, false, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, false, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, false, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, false, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, false, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, false, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, true, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, true, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, true, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, true, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, true, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, true, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, true, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, false, true, true, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, false, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, false, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, false, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, false, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, false, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, false, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, false, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, false, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, true, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, true, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, true, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, true, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, true, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, true, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, true, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, false, true, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, false, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, false, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, false, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, false, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, false, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, false, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, false, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, false, true, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, true, false, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, true, false, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, true, false, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, true, false, true, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, true, true, false, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, true, true, false, true),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, true, true, true, false),
                Arguments.of(mapOf<String, Boolean>(), true, true, true, true, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, false, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, false, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, false, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, false, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, false, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, false, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, false, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, false, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, true, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, true, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, true, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, true, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, true, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, true, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, true, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, false, true, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, false, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, false, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, false, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, false, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, false, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, false, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, false, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, false, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, true, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, true, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, true, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, true, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, true, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, true, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, true, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, false, true, true, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, false, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, false, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, false, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, false, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, false, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, false, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, false, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, false, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, true, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, true, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, true, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, true, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, true, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, true, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, true, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, false, true, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, false, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, false, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, false, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, false, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, false, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, false, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, false, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, false, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, true, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, true, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, true, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, true, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, true, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, true, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, true, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), false, true, true, true, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, false, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, false, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, false, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, false, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, false, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, false, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, false, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, false, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, true, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, true, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, true, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, true, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, true, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, true, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, true, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, false, true, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, false, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, false, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, false, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, false, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, false, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, false, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, false, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, false, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, true, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, true, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, true, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, true, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, true, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, true, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, true, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, false, true, true, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, false, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, false, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, false, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, false, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, false, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, false, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, false, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, false, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, true, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, true, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, true, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, true, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, true, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, true, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, true, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, false, true, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, false, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, false, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, false, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, false, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, false, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, false, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, false, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, false, true, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, true, false, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, true, false, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, true, false, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, true, false, true, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, true, true, false, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, true, true, false, true),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, true, true, true, false),
                Arguments.of(mapOf("1" to true, "2" to false), true, true, true, true, true, true, true)
            )
        }

        @JvmStatic
        fun isModifiedValues(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false),
                    IsModifiedData(mapOf("1" to true, "2" to false), false, false, false, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf("1" to true, "2" to false), false, false, false, false, false, false, false),
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false),
                    IsModifiedData(mapOf(), true, false, false, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), true, false, false, false, false, false, false),
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false),
                    IsModifiedData(mapOf(), false, true, false, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, true, false, false, false, false, false),
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false),
                    IsModifiedData(mapOf(), false, false, true, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, true, false, false, false, false),
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false),
                    IsModifiedData(mapOf(), false, false, false, true, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, true, false, false, false),
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false),
                    IsModifiedData(mapOf(), false, false, false, false, true, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, true, false, false),
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false),
                    IsModifiedData(mapOf(), false, false, false, false, false, true, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, false, true, false),
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false),
                    IsModifiedData(mapOf(), false, false, false, false, false, false, true)
                ),
                Arguments.of(
                    IsModifiedData(mapOf(), false, false, false, false, false, false, true),
                    IsModifiedData(mapOf(), false, false, false, false, false, false, false)
                )
            )
        }
    }

    //endregion
}
