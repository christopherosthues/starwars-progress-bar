package com.christopherosthues.starwarsprogressbar.ui.shapes

import com.christopherosthues.starwarsprogressbar.constants.BASE_MARGIN
import java.awt.Dimension
import java.awt.Insets

private const val LEFT_PADDING = 8

class IconShape(var x: Int, var y: Int, dimension: Dimension, insets: Insets) {
    var width: Int
    var height: Int

    init {
        x += LEFT_PADDING + insets.left
        y += if (insets.top >= BASE_MARGIN) insets.top else 0
        width = dimension.width
        height = dimension.height
    }
}
