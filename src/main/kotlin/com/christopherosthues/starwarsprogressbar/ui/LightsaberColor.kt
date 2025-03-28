package com.christopherosthues.starwarsprogressbar.ui

import com.intellij.ui.JBColor
import java.awt.Color

internal object LightsaberColor {
    val Cyan : JBColor = JBColor(Color(0, 255, 255), Color(0, 255, 255))
    val LightBlue : JBColor = JBColor(Color(100, 180, 230), Color(100, 180, 230))
    val Blue : JBColor = JBColor(Color(20, 90, 220), Color(20, 90, 220))
    val DarkBlue : JBColor = JBColor(Color(0, 10, 240), Color(0, 10, 240))
    val LightGreen : JBColor = JBColor(Color(120, 220, 20), Color(120, 220, 20))
    val Green : JBColor = JBColor(Color(60, 220, 20), Color(60, 220, 20))
    val DarkGreen : JBColor = JBColor(Color(10, 150, 30), Color(10, 150, 30))
    val Orange : JBColor = JBColor(Color(250, 150, 40), Color(250, 150, 40))
    val Yellow : JBColor = JBColor(Color(240, 220, 40), Color(240, 220, 40))
    val Purple : JBColor = JBColor(Color(220, 70, 250), Color(220, 70, 250))
    val White : JBColor = JBColor(Color(255, 255, 255), Color(255, 255, 255))
    val Red : JBColor = JBColor(Color(240, 10, 10), Color(240, 10, 10))

    val colors = mapOf(
        "red" to Red,
        "cyan" to Cyan,
        "lightBlue" to LightBlue,
        "blue" to Blue,
        "darkBlue" to DarkBlue,
        "lightGreen" to LightGreen,
        "green" to Green,
        "darkGreen" to DarkGreen,
        "orange" to Orange,
        "yellow" to Yellow,
        "purple" to Purple,
        "white" to White,
    )
}
