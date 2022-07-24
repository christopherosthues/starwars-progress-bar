package com.christopherosthues.starwarsprogressbar.notification

object GotItService {
    private val gotItTooltips: MutableList<GotIt> = mutableListOf()

    fun addGotItMessage(tooltip: GotIt) {
        gotItTooltips.add(tooltip)
    }

    fun showGotItTooltips() {
        gotItTooltips.forEach { it.show() }
    }
}
