package com.christopherosthues.starwarsprogressbar.notification

import com.intellij.openapi.ui.popup.Balloon
import com.intellij.ui.GotItTooltip
import java.awt.Component
import java.awt.Point
import javax.swing.JComponent

class GotIt(
    private val tooltip: GotItTooltip,
    private val component: JComponent,
    private val pointProvider: (Component, Balloon) -> Point
) {
    fun show() {
        tooltip.show(component, pointProvider)
        tooltip.dispose()
    }
}
