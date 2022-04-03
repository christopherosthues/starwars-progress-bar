package com.christopherosthues.starwarsprogressbar.ui

import com.intellij.ui.JBColor
import java.awt.Color

private const val MAX_RGB_VALUE = 255

private const val BLUE_R_VALUE = 74
private const val BLUE_G_VALUE = 228
private const val BLUE_B_VALUE = 220

private const val BROWN_R_VALUE = 117
private const val BROWN_G_VALUE = 80
private const val BROWN_B_VALUE = 62

private const val GREEN_R_VALUE = 85
private const val GREEN_B_VALUE = 36

private const val ORANGE_R_VALUE = 240
private const val ORANGE_G_VALUE = 200
private const val ORANGE_B_VALUE = 91

private const val PURPLE_G_VALUE = 73
private const val PURPLE_B_VALUE = 245

private const val RED_G_VALUE = 107
private const val RED_B_VALUE = 107

private const val YELLOW_R_VALUE = 245
private const val YELLOW_G_VALUE = 238
private const val YELLOW_B_VALUE = 60

internal object IonEngineColor {
    val BlueEngine = JBColor(
        Color(BLUE_R_VALUE, BLUE_G_VALUE, BLUE_B_VALUE),
        Color(BLUE_R_VALUE, BLUE_G_VALUE, BLUE_B_VALUE)
    )

    private val BrownEngine = JBColor(
        Color(BROWN_R_VALUE, BROWN_G_VALUE, BROWN_B_VALUE),
        Color(BROWN_R_VALUE, BROWN_G_VALUE, BROWN_B_VALUE)
    )

    private val GreenEngine = JBColor(
        Color(GREEN_R_VALUE, MAX_RGB_VALUE, GREEN_B_VALUE),
        Color(GREEN_R_VALUE, MAX_RGB_VALUE, GREEN_B_VALUE)
    )

    private val OrangeEngine = JBColor(
        Color(ORANGE_R_VALUE, ORANGE_G_VALUE, ORANGE_B_VALUE),
        Color(ORANGE_R_VALUE, ORANGE_G_VALUE, ORANGE_B_VALUE)
    )

    private val PurpleEngine = JBColor(
        Color(MAX_RGB_VALUE, PURPLE_G_VALUE, PURPLE_B_VALUE),
        Color(MAX_RGB_VALUE, PURPLE_G_VALUE, PURPLE_B_VALUE)
    )

    private val RedEngine = JBColor(
        Color(MAX_RGB_VALUE, RED_G_VALUE, RED_B_VALUE),
        Color(MAX_RGB_VALUE, RED_G_VALUE, RED_B_VALUE)
    )

    private val YellowEngine = JBColor(
        Color(YELLOW_R_VALUE, YELLOW_G_VALUE, YELLOW_B_VALUE),
        Color(YELLOW_R_VALUE, YELLOW_G_VALUE, YELLOW_B_VALUE)
    )

    private val WhiteEngine = JBColor(
        Color(MAX_RGB_VALUE, MAX_RGB_VALUE, MAX_RGB_VALUE),
        Color(MAX_RGB_VALUE, MAX_RGB_VALUE, MAX_RGB_VALUE)
    )

    val colors = mapOf(
        "blue" to BlueEngine,
        "brown" to BrownEngine,
        "green" to GreenEngine,
        "orange" to OrangeEngine,
        "purple" to PurpleEngine,
        "red" to RedEngine,
        "yellow" to YellowEngine,
        "white" to WhiteEngine
    )
}
