package com.christopherosthues.starwarsprogressbar.configuration.borders

import com.christopherosthues.starwarsprogressbar.constants.BASE_MARGIN
import com.christopherosthues.starwarsprogressbar.constants.BORDER_LINE_MARGIN
import com.christopherosthues.starwarsprogressbar.constants.BORDER_MARGIN
import com.christopherosthues.starwarsprogressbar.constants.ICON_TEXT_SPACING
import com.christopherosthues.starwarsprogressbar.constants.LEFT_RIGHT_BORDER_MARGIN
import com.christopherosthues.starwarsprogressbar.ui.components.ColoredImageComponent
import com.christopherosthues.starwarsprogressbar.ui.shapes.BorderShape
import com.christopherosthues.starwarsprogressbar.ui.shapes.IconShape
import com.christopherosthues.starwarsprogressbar.ui.shapes.LabelShape
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import java.awt.Component
import java.awt.Graphics
import java.awt.Insets
import javax.swing.JLabel
import javax.swing.border.AbstractBorder

internal class TitledIconBorder(title: String, iconName: String) : AbstractBorder() {
    private val label: JLabel = JLabel(title)
    private val factionIcon = StarWarsResourceLoader.getFactionLogo(iconName, true)
    private val icon = ColoredImageComponent(factionIcon)

    init {
        require(title.isNotEmpty()) { "Title must not be empty!" }
        label.isOpaque = false
        icon.isOpaque = false
    }

    override fun isBorderOpaque(): Boolean {
        return false
    }

    override fun getBorderInsets(c: Component, insets: Insets): Insets {
        updateLabel(c)

        val top = if (BASE_MARGIN < icon.preferredSize.height) icon.preferredSize.height - BASE_MARGIN else BASE_MARGIN
        insets.set(top + BASE_MARGIN, BORDER_MARGIN, BORDER_MARGIN, BORDER_MARGIN)
        return insets
    }

    override fun getBaseline(c: Component, width: Int, height: Int): Int {
        require(width >= 0) { "The width was $width but a value >= 0 has to be provided." }
        require(height >= 0) { "The height was $height but a value >= 0 has to be provided." }
        updateLabel(c)

        val size = icon.preferredSize
        val insets = Insets(BASE_MARGIN + (BASE_MARGIN - size.height) / 2, BASE_MARGIN, BASE_MARGIN, BASE_MARGIN)

        val baseline = icon.getBaseline(size.width, size.height)

        return if (insets.top < BASE_MARGIN) baseline else baseline + insets.top
    }

    override fun getBaselineResizeBehavior(c: Component): Component.BaselineResizeBehavior {
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT
    }

    override fun paintBorder(c: Component, g: Graphics, x: Int, y: Int, width: Int, height: Int) {
        updateLabel(c)
        val insets = Insets(
            BASE_MARGIN + BASE_MARGIN / 2 - icon.preferredSize.height / 2,
            BASE_MARGIN + LEFT_RIGHT_BORDER_MARGIN,
            BASE_MARGIN,
            BASE_MARGIN + LEFT_RIGHT_BORDER_MARGIN,
        )

        val borderShape = BorderShape(x, y, width, height, BASE_MARGIN, insets.top)
        val iconShape = IconShape(x, y, icon.preferredSize, insets)
        val labelShape = LabelShape(label.preferredSize, insets, iconShape)

        drawBorder(c, g, borderShape)
        drawIconAndTitle(c, g, iconShape, labelShape)
    }

    private fun updateLabel(c: Component) {
        label.componentOrientation = c.componentOrientation
        label.font = c.font
        label.foreground = c.foreground
        label.isEnabled = c.isEnabled

        icon.foreground = c.foreground
    }

    private fun drawBorder(
        c: Component,
        g: Graphics,
        borderShape: BorderShape,
    ) {
        val graphics2d = g.create()
        val borderWidth = borderShape.width
        val borderHeight = borderShape.height
        val borderX = borderShape.x
        val borderY = borderShape.y

        graphics2d.translate(borderX, borderY)

        graphics2d.color = c.foreground
        graphics2d.drawRect(0, 0, borderWidth - BASE_MARGIN, borderHeight - BASE_MARGIN)

        graphics2d.color = c.foreground.darker().darker()
        graphics2d.drawLine(1, borderHeight - BORDER_LINE_MARGIN, 1, 1)
        graphics2d.drawLine(1, 1, borderWidth - BORDER_LINE_MARGIN, 1)

        graphics2d.drawLine(0, borderHeight - 1, borderWidth - 1, borderHeight - 1)
        graphics2d.drawLine(borderWidth - 1, borderHeight - 1, borderWidth - 1, 0)

        graphics2d.translate(-borderX, -borderY)
        graphics2d.dispose()
    }

    private fun drawIconAndTitle(
        c: Component,
        g: Graphics,
        iconShape: IconShape,
        labelShape: LabelShape,
    ) {
        // Draw background for icon and title
        val graphicsColor = g.color
        g.color = c.background
        g.fillRect(
            iconShape.x - BORDER_MARGIN,
            iconShape.y,
            iconShape.width + labelShape.width + ICON_TEXT_SPACING + 2 * BORDER_MARGIN,
            iconShape.height,
        )
        g.color = graphicsColor

        // Draw icon and title
        g.translate(iconShape.x, iconShape.y)
        icon.setSize(iconShape.width, iconShape.height)
        icon.paint(g)
        g.translate(-iconShape.x + labelShape.x, -iconShape.y + labelShape.y)
        label.setSize(labelShape.width, labelShape.height)
        label.paint(g)
        g.translate(-labelShape.x, -labelShape.y)
    }
}
