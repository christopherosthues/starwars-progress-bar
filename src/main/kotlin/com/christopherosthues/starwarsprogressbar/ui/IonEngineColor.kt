package com.christopherosthues.starwarsprogressbar.ui

import com.intellij.ui.JBColor
import java.awt.Color

internal object IonEngineColor {
    val BlueEngine = JBColor(Color(74, 228, 220), Color(74, 228, 220))
    private val BrownEngine = JBColor(Color(117, 80, 62), Color(117, 80, 62))
    private val GreenEngine = JBColor(Color(85, 255, 36), Color(85, 255, 36))
    private val OrangeEngine = JBColor(Color(240, 200, 91), Color(240, 200, 91))
    private val PurpleEngine = JBColor(Color(255, 73, 245), Color(255, 73, 245))
    private val RedEngine = JBColor(Color(255, 107, 107), Color(255, 107, 107))
    private val YellowEngine = JBColor(Color(245, 238, 60), Color(245, 238, 60))
    private val WhiteEngine = JBColor(Color(255, 255, 255), Color(255, 255, 255))

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
