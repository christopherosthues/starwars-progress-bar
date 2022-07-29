package com.christopherosthues.starwarsprogressbar.ui.shapes

import com.christopherosthues.starwarsprogressbar.configuration.borders.ICON_TEXT_SPACING
import java.awt.Dimension
import java.awt.Insets
import kotlin.math.max

class LabelShape(dimension: Dimension, insets: Insets, iconShape: IconShape) {
    var x: Int
    var y: Int
    var width: Int
    var height: Int

    init {
        width = max(dimension.width, dimension.width - insets.left - insets.right)
        height = dimension.height
        x = iconShape.x + iconShape.width + ICON_TEXT_SPACING
        y = iconShape.y + iconShape.height / 2 - height / 2
    }
}
