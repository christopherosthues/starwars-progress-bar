package com.christopherosthues.starwarsprogressbar.constants

import com.intellij.idea.TestFor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

@TestFor(classes = [BundleConstants::class])
class BundleConstantsTests {
    //region Tests

    @ParameterizedTest
    @MethodSource("bundleConstants")
    fun `constants should return correct values`(expectedBundleConstant: String, bundleConstant: String) {
        // Arrange

        // Act and Assert
        assertEquals(expectedBundleConstant, bundleConstant)
    }

    @Test
    fun `all public constants should be tested`() {
        // Arrange
        val bundleConstantMembers = BundleConstants::class.memberProperties.filter { it.visibility != KVisibility.PRIVATE }

        // Act and Assert
        assertEquals(bundleConstantMembers.size, bundleConstants().count().toInt())
    }

    //endregion

    //region Test case data

    companion object {
        @JvmStatic
        fun bundleConstants(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("name", BundleConstants.PLUGIN_NAME),
                Arguments.of("notification.pluginUpdated", BundleConstants.NOTIFICATION_PLUGIN_UPDATED),
                Arguments.of("notification.configure", BundleConstants.NOTIFICATION_CONFIGURE),
                Arguments.of("notification.dontShowAgain", BundleConstants.NOTIFICATION_DONT_SHOW_AGAIN),
                Arguments.of("configuration.preview", BundleConstants.PREVIEW_TITLE),
                Arguments.of("configuration.determinate", BundleConstants.DETERMINATE),
                Arguments.of("configuration.indeterminate", BundleConstants.INDETERMINATE),
                Arguments.of("configuration.uiOptions", BundleConstants.UI_OPTIONS),
                Arguments.of("configuration.showVehicleName", BundleConstants.SHOW_VEHICLE_NAME),
                Arguments.of("configuration.showToolTips", BundleConstants.SHOW_TOOL_TIPS),
                Arguments.of("configuration.showFactionCrests", BundleConstants.SHOW_FACTION_CRESTS),
                Arguments.of("configuration.sameVehicleVelocity", BundleConstants.SAME_VEHICLE_VELOCITY),
                Arguments.of("configuration.enableNewVehicles", BundleConstants.ENABLE_NEW_VEHICLES),
                Arguments.of("configuration.vehicles", BundleConstants.VEHICLES_TITLE),
                Arguments.of("configuration.selectAll", BundleConstants.SELECT_ALL),
                Arguments.of("configuration.deselectAll", BundleConstants.DESELECT_ALL),
                Arguments.of("configuration.selected", BundleConstants.SELECTED),
                Arguments.of("configuration.gotit.iconSelection", BundleConstants.GOT_IT_ICON_SELECTION),
                Arguments.of("faction.", BundleConstants.FACTION),
                Arguments.of("vehicles.", BundleConstants.VEHICLES)
            )
        }
    }

    //endregion
}
