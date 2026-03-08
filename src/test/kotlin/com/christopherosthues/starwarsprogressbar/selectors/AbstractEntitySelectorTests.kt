package com.christopherosthues.starwarsprogressbar.selectors

import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.intellij.idea.TestFor
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestFor(classes = [DeterminateSelector::class, IndeterminateSelector::class])
class AbstractEntitySelectorTests {
    private lateinit var sut: AbstractEntitySelector

    private lateinit var inorderFactionSelectorMock: InorderFactionSelector
    private lateinit var inorderNameSelectorMock: InorderNameSelector
    private lateinit var randomSelectorMock: RandomSelector
    private lateinit var rollingRandomSelectorMock: RollingRandomSelector
    private lateinit var reverseOrderFactionSelectorMock: ReverseOrderFactionSelector
    private lateinit var reverseOrderNameSelectorMock: ReverseOrderNameSelector

    //region Test lifecycle

    @BeforeEach
    fun setup() {
        inorderFactionSelectorMock = mockk(relaxed = true)
        inorderNameSelectorMock = mockk(relaxed = true)
        randomSelectorMock = mockk(relaxed = true)
        rollingRandomSelectorMock = mockk(relaxed = true)
        reverseOrderFactionSelectorMock = mockk(relaxed = true)
        reverseOrderNameSelectorMock = mockk(relaxed = true)

        sut = TestAbstractEntitySelector(
            inorderFactionSelectorMock,
            inorderNameSelectorMock,
            randomSelectorMock,
            rollingRandomSelectorMock,
            reverseOrderFactionSelectorMock,
            reverseOrderNameSelectorMock,
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    //endregion

    //region Tests

    @ParameterizedTest
    @MethodSource("selectorValues")
    fun `selectEntity should route to correct sub-selector based on SelectionType`(
        selectionType: SelectionType,
        factionSelector: Int,
        nameSelector: Int,
        randomSelector: Int,
        rollingRandomSelector: Int,
        reverseFactionSelector: Int,
        reverseNameSelector: Int,
    ) {
        // Arrange
        val expectedEntity = mockk<StarWarsEntity>()
        val enabledVehicles = mapOf("v1" to true)
        val enabledLightsabers = mapOf("l1" to true)
        val defaultEnabled = true

        every { inorderFactionSelectorMock.selectEntity(any(), any(), any()) } returns expectedEntity
        every { inorderNameSelectorMock.selectEntity(any(), any(), any()) } returns expectedEntity
        every { randomSelectorMock.selectEntity(any(), any(), any()) } returns expectedEntity
        every { rollingRandomSelectorMock.selectEntity(any(), any(), any()) } returns expectedEntity
        every { reverseOrderFactionSelectorMock.selectEntity(any(), any(), any()) } returns expectedEntity
        every { reverseOrderNameSelectorMock.selectEntity(any(), any(), any()) } returns expectedEntity

        // Act
        val result = sut.selectEntity(
            enabledVehicles,
            enabledLightsabers,
            defaultEnabled,
            selectionType,
            EntitySelectionType.ALL,
        )

        // Assert
        assertAll(
            { assertEquals(expectedEntity, result) },
            {
                verify(exactly = factionSelector) {
                    inorderFactionSelectorMock.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled)
                }
            },
            {
                verify(exactly = nameSelector) {
                    inorderNameSelectorMock.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled)
                }
            },
            {
                verify(exactly = randomSelector) {
                    randomSelectorMock.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled)
                }
            },
            {
                verify(exactly = rollingRandomSelector) {
                    rollingRandomSelectorMock.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled)
                }
            },
            {
                verify(exactly = reverseFactionSelector) {
                    reverseOrderFactionSelectorMock.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled)
                }
            },
            {
                verify(exactly = reverseNameSelector) {
                    reverseOrderNameSelectorMock.selectEntity(enabledVehicles, enabledLightsabers, defaultEnabled)
                }
            },
        )
    }

    @ParameterizedTest
    @MethodSource("entitySelectionTypeValues")
    fun `selectEntity should correctly filter enabled maps based on EntitySelectionType`(
        entitySelectionType: EntitySelectionType,
        expectedVehicles: Map<String, Boolean>,
        expectedLightsabers: Map<String, Boolean>,
    ) {
        // Arrange
        val enabledVehicles = mapOf("v1" to true, "v2" to false)
        val enabledLightsabers = mapOf("l1" to true, "l2" to false)
        val defaultEnabled = true

        // Act
        sut.selectEntity(
            enabledVehicles,
            enabledLightsabers,
            defaultEnabled,
            SelectionType.RANDOM_ALL,
            entitySelectionType,
        )

        // Assert
        verify {
            randomSelectorMock.selectEntity(expectedVehicles, expectedLightsabers, defaultEnabled)
        }
    }

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun selectorValues(): Stream<Arguments> = Stream.of(
            Arguments.of(SelectionType.INORDER_FACTION, 1, 0, 0, 0, 0, 0),
            Arguments.of(SelectionType.INORDER_NAME, 0, 1, 0, 0, 0, 0),
            Arguments.of(SelectionType.RANDOM_ALL, 0, 0, 1, 0, 0, 0),
            Arguments.of(SelectionType.RANDOM_NOT_DISPLAYED, 0, 0, 0, 1, 0, 0),
            Arguments.of(SelectionType.REVERSE_ORDER_FACTION, 0, 0, 0, 0, 1, 0),
            Arguments.of(SelectionType.REVERSE_ORDER_NAME, 0, 0, 0, 0, 0, 1),
        )

        @JvmStatic
        fun entitySelectionTypeValues(): Stream<Arguments> = Stream.of(
            Arguments.of(
                EntitySelectionType.ALL,
                mapOf("v1" to true, "v2" to false),
                mapOf("l1" to true, "l2" to false),
            ),
            Arguments.of(
                EntitySelectionType.VEHICLES,
                mapOf("v1" to true, "v2" to false),
                mapOf("l1" to false, "l2" to false),
            ),
            Arguments.of(
                EntitySelectionType.LIGHTSABERS,
                mapOf("v1" to false, "v2" to false),
                mapOf("l1" to true, "l2" to false),
            ),
        )
    }

    //endregion

    private class TestAbstractEntitySelector(
        inorderFactionSelector: InorderFactionSelector,
        inorderNameSelector: InorderNameSelector,
        randomSelector: RandomSelector,
        rollingRandomSelector: RollingRandomSelector,
        reverseOrderFactionSelector: ReverseOrderFactionSelector,
        reverseOrderNameSelector: ReverseOrderNameSelector,
    ) : AbstractEntitySelector(
        inorderFactionSelector,
        inorderNameSelector,
        randomSelector,
        rollingRandomSelector,
        reverseOrderFactionSelector,
        reverseOrderNameSelector,
    )
}
