package com.christopherosthues.starwarsprogressbar.configuration

import com.christopherosthues.starwarsprogressbar.StarWarsBundle
import com.christopherosthues.starwarsprogressbar.constants.BundleConstants
import com.christopherosthues.starwarsprogressbar.constants.PluginConstants
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactionHolder
import com.christopherosthues.starwarsprogressbar.selectors.SelectionType
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
        mockkObject(StarWarsFactionHolder)
        mockkObject(StarWarsPersistentStateComponent)
        mockkStatic(::createStarWarsProgressConfigurationComponent)

        every { StarWarsFactionHolder.defaultVehicles } returns listOf()
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
        enabledVehicles: MutableMap<String, Boolean>,
        enabledLightsabers: MutableMap<String, Boolean> = mutableMapOf(),
        showVehicle: Boolean,
        showVehicleNames: Boolean,
        showToolTips: Boolean,
        showFactionCrests: Boolean,
        sameVehicleVelocity: Boolean,
        enableNewVehicles: Boolean,
        solidProgressBarColor: Boolean,
        drawSilhouettes: Boolean,
        changeVehicleAfterPass: Boolean = false,
        numberOfPassesUntilVehicleChange: Int = 2,
        vehicleSelector: SelectionType = SelectionType.RANDOM_ALL,
    ) {
        // Arrange
        setupStarWarsState(
            enabledVehicles,
            enabledLightsabers,
            showVehicle,
            showVehicleNames,
            showToolTips,
            showFactionCrests,
            sameVehicleVelocity,
            enableNewVehicles,
            solidProgressBarColor,
            drawSilhouettes,
            changeVehicleAfterPass,
            numberOfPassesUntilVehicleChange,
            vehicleSelector,
        )
        setupComponentState(
            enabledVehicles,
            enabledLightsabers,
            showVehicle,
            showVehicleNames,
            showToolTips,
            showFactionCrests,
            sameVehicleVelocity,
            enableNewVehicles,
            solidProgressBarColor,
            drawSilhouettes,
            changeVehicleAfterPass,
            numberOfPassesUntilVehicleChange,
            vehicleSelector,
        )
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()

        // Act
        val result = sut.isModified

        // Assert
        assertFalse(result)
    }

    @Test
    fun `isModified should return false if all properties are all equal except number of passes and change vehicle after pass is false`() {
        // Arrange
        setupStarWarsState()
        setupComponentState(
            changeVehicleAfterPass = false,
            numberOfPassesUntilVehicleChange = 3,
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
        componentStateData: IsModifiedData,
    ) {
        // Arrange
        setupStarWarsState(
            starWarsStateData.enabledVehicles,
            starWarsStateData.enabledLightsabers,
            starWarsStateData.showVehicle,
            starWarsStateData.showVehicleNames,
            starWarsStateData.showToolTips,
            starWarsStateData.showFactionCrests,
            starWarsStateData.sameVehicleVelocity,
            starWarsStateData.enableNewVehicles,
            starWarsStateData.solidProgressBarColor,
            starWarsStateData.drawSilhouettes,
            starWarsStateData.changeVehicleAfterPass,
            starWarsStateData.numberOfPassesUntilVehicleChange,
            starWarsStateData.vehicleSelector,
            starWarsStateData.language,
        )
        setupComponentState(
            componentStateData.enabledVehicles,
            componentStateData.enabledLightsabers,
            componentStateData.showVehicle,
            componentStateData.showVehicleNames,
            componentStateData.showToolTips,
            componentStateData.showFactionCrests,
            componentStateData.sameVehicleVelocity,
            componentStateData.enableNewVehicles,
            componentStateData.solidProgressBarColor,
            componentStateData.drawSilhouettes,
            componentStateData.changeVehicleAfterPass,
            componentStateData.numberOfPassesUntilVehicleChange,
            componentStateData.vehicleSelector,
            componentStateData.language,
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
        val enabledVehicles = mutableMapOf("1" to true, "2" to false)
        starWarsStateMock.vehiclesEnabled = enabledVehicles
        val enabledLightsabers = mutableMapOf("1" to true, "2" to false)
        starWarsStateMock.lightsabersEnabled = enabledLightsabers
        val sut = StarWarsProgressConfigurable()

        // Act
        sut.apply()

        // Assert
        assertEquals(enabledVehicles, starWarsStateMock.vehiclesEnabled)
        assertEquals(enabledLightsabers, starWarsStateMock.lightsabersEnabled)
        verify(exactly = 0) { starWarsStateMock.showIcon }
        verify(exactly = 0) { starWarsStateMock.showNames }
        verify(exactly = 0) { starWarsStateMock.showToolTips }
        verify(exactly = 0) { starWarsStateMock.showFactionCrests }
        verify(exactly = 0) { starWarsStateMock.sameVelocity }
        verify(exactly = 0) { starWarsStateMock.enableNew }
        verify(exactly = 0) { starWarsStateMock.solidProgressBarColor }
        verify(exactly = 0) { starWarsStateMock.drawSilhouettes }
        verify(exactly = 0) { starWarsStateMock.changeAfterPass }
        verify(exactly = 0) { starWarsStateMock.numberOfPassesUntilChange }
        verify(exactly = 0) { starWarsStateMock.selector }
        verify(exactly = 0) { starWarsStateMock.language }
    }

    @ParameterizedTest
    @MethodSource("isNotModifiedValues")
    fun `apply should update star wars state`(
        enabledVehicles: MutableMap<String, Boolean> = mutableMapOf(),
        enabledLightsabers: MutableMap<String, Boolean> = mutableMapOf(),
        showVehicle: Boolean = false,
        showVehicleNames: Boolean = false,
        showToolTips: Boolean = false,
        showFactionCrests: Boolean = false,
        sameVehicleVelocity: Boolean = false,
        enableNewVehicles: Boolean = false,
        solidProgressBarColor: Boolean = false,
        drawSilhouettes: Boolean = false,
        changeVehicleAfterPass: Boolean = false,
        numberOfPassesUntilVehicleChange: Int = 2,
        vehicleSelector: SelectionType = SelectionType.RANDOM_ALL,
        language: Language = Language.ENGLISH,
    ) {
        // Arrange
        val starWarsStateMock = setupStarWarsPersistentStateComponentMock().state!!
        setupComponentState(
            enabledVehicles,
            enabledLightsabers,
            showVehicle,
            showVehicleNames,
            showToolTips,
            showFactionCrests,
            sameVehicleVelocity,
            enableNewVehicles,
            solidProgressBarColor,
            drawSilhouettes,
            changeVehicleAfterPass,
            numberOfPassesUntilVehicleChange,
            vehicleSelector,
            language,
        )
        val sut = StarWarsProgressConfigurable()
        sut.createComponent()

        // Act
        sut.apply()

        // Assert
        val numberOfPassesSet = if (changeVehicleAfterPass) 1 else 0
        verify(exactly = 1) { starWarsStateMock.showIcon = showVehicle }
        verify(exactly = 1) { starWarsStateMock.showNames = showVehicleNames }
        verify(exactly = 1) { starWarsStateMock.showToolTips = showToolTips }
        verify(exactly = 1) { starWarsStateMock.showFactionCrests = showFactionCrests }
        verify(exactly = 1) { starWarsStateMock.sameVelocity = sameVehicleVelocity }
        verify(exactly = 1) { starWarsStateMock.enableNew = enableNewVehicles }
        verify(exactly = 1) { starWarsStateMock.solidProgressBarColor = solidProgressBarColor }
        verify(exactly = 1) { starWarsStateMock.drawSilhouettes = drawSilhouettes }
        verify(exactly = 1) { starWarsStateMock.changeAfterPass = changeVehicleAfterPass }
        verify(exactly = 1) { starWarsStateMock.selector = vehicleSelector }
        verify(exactly = 1) { starWarsStateMock.language = language }
        verify(exactly = numberOfPassesSet) { starWarsStateMock.numberOfPassesUntilChange = numberOfPassesUntilVehicleChange }
        assertEquals(enabledVehicles, starWarsStateMock.vehiclesEnabled, StarWarsState::vehiclesEnabled.name)
        assertEquals(enabledLightsabers, starWarsStateMock.lightsabersEnabled, StarWarsState::lightsabersEnabled.name)
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
        assertEquals(PluginConstants.PLUGIN_SEARCH_ID, result)
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
        initializeStarWarsState: Boolean = true,
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
        enabledVehicles: MutableMap<String, Boolean> = mutableMapOf(),
        enabledLightsabers: MutableMap<String, Boolean> = mutableMapOf(),
        showVehicle: Boolean = false,
        showVehicleNames: Boolean = false,
        showToolTips: Boolean = false,
        showFactionCrests: Boolean = false,
        sameVehicleVelocity: Boolean = false,
        enableNewVehicles: Boolean = false,
        solidProgressBarColor: Boolean = false,
        drawSilhouettes: Boolean = false,
        changeVehicleAfterPass: Boolean = false,
        numberOfPassesUntilVehicleChange: Int = 2,
        vehicleSelector: SelectionType = SelectionType.RANDOM_ALL,
        language: Language = Language.ENGLISH,
    ): StarWarsState {
        val starWarsStateMock = mockk<StarWarsState>()
        starWarsStateMock.vehiclesEnabled = enabledVehicles
        starWarsStateMock.lightsabersEnabled = enabledLightsabers
        every { starWarsStateMock.showIcon } returns showVehicle
        every { starWarsStateMock.showNames } returns showVehicleNames
        every { starWarsStateMock.showToolTips } returns showToolTips
        every { starWarsStateMock.showFactionCrests } returns showFactionCrests
        every { starWarsStateMock.sameVelocity } returns sameVehicleVelocity
        every { starWarsStateMock.enableNew } returns enableNewVehicles
        every { starWarsStateMock.solidProgressBarColor } returns solidProgressBarColor
        every { starWarsStateMock.drawSilhouettes } returns drawSilhouettes
        every { starWarsStateMock.changeAfterPass } returns changeVehicleAfterPass
        every { starWarsStateMock.numberOfPassesUntilChange } returns numberOfPassesUntilVehicleChange
        every { starWarsStateMock.selector } returns vehicleSelector
        every { starWarsStateMock.version } returns ""
        every { starWarsStateMock.language } returns language
        val starWarsPersistentStateComponentMock = mockk<StarWarsPersistentStateComponent>(relaxed = true)
        every { StarWarsPersistentStateComponent.instance } returns starWarsPersistentStateComponentMock
        every { starWarsPersistentStateComponentMock.state } returns starWarsStateMock

        return starWarsStateMock
    }

    private fun setupComponentState(
        enabledVehicles: MutableMap<String, Boolean> = mutableMapOf(),
        enabledLightsabers: MutableMap<String, Boolean> = mutableMapOf(),
        showVehicle: Boolean = false,
        showVehicleNames: Boolean = false,
        showToolTips: Boolean = false,
        showFactionCrests: Boolean = false,
        sameVehicleVelocity: Boolean = false,
        enableNewVehicles: Boolean = false,
        solidProgressBarColor: Boolean = false,
        drawSilhouettes: Boolean = false,
        changeVehicleAfterPass: Boolean = false,
        numberOfPassesUntilVehicleChange: Int = 2,
        vehicleSelector: SelectionType = SelectionType.RANDOM_ALL,
        language: Language = Language.ENGLISH,
    ) {
        val starWarsState = StarWarsState()
        starWarsState.vehiclesEnabled = enabledVehicles
        starWarsState.lightsabersEnabled = enabledLightsabers
        starWarsState.showIcon = showVehicle
        starWarsState.showNames = showVehicleNames
        starWarsState.showToolTips = showToolTips
        starWarsState.showFactionCrests = showFactionCrests
        starWarsState.sameVelocity = sameVehicleVelocity
        starWarsState.enableNew = enableNewVehicles
        starWarsState.solidProgressBarColor = solidProgressBarColor
        starWarsState.drawSilhouettes = drawSilhouettes
        starWarsState.changeAfterPass = changeVehicleAfterPass
        starWarsState.numberOfPassesUntilChange = numberOfPassesUntilVehicleChange
        starWarsState.selector = vehicleSelector
        starWarsState.language = language
        every { starWarsProgressConfigurationComponentMock.starWarsState } returns starWarsState
    }

    //endregion

    //region Test case data

    data class IsModifiedData(
        val enabledVehicles: MutableMap<String, Boolean> = mutableMapOf(),
        val enabledLightsabers: MutableMap<String, Boolean> = mutableMapOf(),
        val showVehicle: Boolean = false,
        val showVehicleNames: Boolean = false,
        val showToolTips: Boolean = false,
        val showFactionCrests: Boolean = false,
        val sameVehicleVelocity: Boolean = false,
        val enableNewVehicles: Boolean = false,
        val solidProgressBarColor: Boolean = false,
        val drawSilhouettes: Boolean = false,
        val changeVehicleAfterPass: Boolean = false,
        val numberOfPassesUntilVehicleChange: Int = 2,
        val vehicleSelector: SelectionType = SelectionType.RANDOM_ALL,
        val language: Language = Language.ENGLISH,
    )

    companion object {
        @JvmStatic
        fun isNotModifiedValues(): Stream<Arguments> = Stream.of(
            Arguments.of(mapOf<String, Boolean>(), mapOf<String, Boolean>(), true, false, true, false, true, false, true, false, false, 2, SelectionType.RANDOM_ALL, Language.ENGLISH),
            Arguments.of(
                mapOf("1" to true, "2" to false),
                mapOf("1" to true, "2" to false),
                false,
                true,
                false,
                true,
                false,
                true,
                false,
                true,
                true,
                2,
                SelectionType.RANDOM_ALL,
                Language.ENGLISH,
            ),
            Arguments.of(
                mapOf("1" to true, "2" to false),
                mapOf("1" to true, "2" to false),
                false,
                true,
                false,
                true,
                false,
                true,
                false,
                true,
                true,
                4,
                SelectionType.RANDOM_NOT_DISPLAYED,
                Language.GERMAN,
            ),
            Arguments.of(
                mapOf("1" to true, "2" to false),
                mapOf("1" to true, "2" to false),
                false,
                true,
                false,
                true,
                false,
                true,
                false,
                true,
                false,
                4,
                SelectionType.RANDOM_NOT_DISPLAYED,
                Language.GERMAN,
            ),
        )

        @JvmStatic
        fun isModifiedValues(): Stream<Arguments> = Stream.of(
            Arguments.of(IsModifiedData(), IsModifiedData(mutableMapOf("1" to true, "2" to false))),
            Arguments.of(IsModifiedData(mutableMapOf("1" to true, "2" to false)), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(enabledLightsabers = mutableMapOf("1" to true, "2" to false))),
            Arguments.of(IsModifiedData(enabledLightsabers = mutableMapOf("1" to true, "2" to false)), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(showVehicle = true)),
            Arguments.of(IsModifiedData(showVehicle = true), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(mutableMapOf(), showVehicle = true)),
            Arguments.of(IsModifiedData(showVehicle = true), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(mutableMapOf(), showToolTips = true)),
            Arguments.of(IsModifiedData(showToolTips = true), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(mutableMapOf(), showFactionCrests = true)),
            Arguments.of(IsModifiedData(showFactionCrests = true), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(mutableMapOf(), sameVehicleVelocity = true)),
            Arguments.of(IsModifiedData(sameVehicleVelocity = true), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(mutableMapOf(), enableNewVehicles = true)),
            Arguments.of(IsModifiedData(enableNewVehicles = true), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(mutableMapOf(), solidProgressBarColor = true)),
            Arguments.of(IsModifiedData(solidProgressBarColor = true), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(mutableMapOf(), drawSilhouettes = true)),
            Arguments.of(IsModifiedData(drawSilhouettes = true), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(mutableMapOf(), vehicleSelector = SelectionType.INORDER_NAME)),
            Arguments.of(IsModifiedData(vehicleSelector = SelectionType.INORDER_NAME), IsModifiedData()),
            Arguments.of(IsModifiedData(changeVehicleAfterPass = true), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(changeVehicleAfterPass = true)),
            Arguments.of(IsModifiedData(changeVehicleAfterPass = true, numberOfPassesUntilVehicleChange = 4), IsModifiedData()),
            Arguments.of(IsModifiedData(), IsModifiedData(changeVehicleAfterPass = true, numberOfPassesUntilVehicleChange = 4)),
            Arguments.of(IsModifiedData(changeVehicleAfterPass = true), IsModifiedData(numberOfPassesUntilVehicleChange = 4)),
            Arguments.of(IsModifiedData(numberOfPassesUntilVehicleChange = 4), IsModifiedData(changeVehicleAfterPass = true)),
            Arguments.of(IsModifiedData(), IsModifiedData(language = Language.GERMAN)),
            Arguments.of(IsModifiedData(language = Language.GERMAN), IsModifiedData()),
        )
    }

    //endregion
}
