package com.github.christopherosthues.starwarsprogressbar.ui

import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.GraphicsUtil
import com.intellij.util.ui.UIUtil
import java.awt.*
import java.awt.geom.RoundRectangle2D
import javax.swing.JComponent
import javax.swing.SwingConstants
import javax.swing.plaf.basic.BasicProgressBarUI

internal class StarWarsProgressBarUI(private val starWarsVehicle: StarWarsVehicle) : BasicProgressBarUI() {
    private val forwardIcon = StarWarsResourceLoader.getIcon(starWarsVehicle.fileName)
    private val backwardIcon = StarWarsResourceLoader.getReversedIcon(starWarsVehicle.fileName)
    private var velocity = starWarsVehicle.velocity
    private var position = 0

    override fun getBoxLength(availableLength: Int, otherDimension: Int): Int {
        return availableLength
    }

    override fun getPreferredSize(c: JComponent?): Dimension {
        return Dimension(super.getPreferredSize(c).width, JBUIScale.scale(20))
    }

    override fun paintIndeterminate(g: Graphics?, c: JComponent?) {
        paintProgressBar(g, c, false)
        updatePositionAndVelocity()
    }

    override fun paintDeterminate(g: Graphics?, c: JComponent?) {
        resetPositionAndVelocity()
        paintProgressBar(g, c, true)
    }

    private fun updatePositionAndVelocity() {
        val actualVelocity = velocity
        val actualPosition: Int = position
        if (velocity < 0) {
            if (position <= 0) {
                velocity = starWarsVehicle.velocity
                position = 0
            } else {
                position = actualPosition + JBUIScale.scale(velocity).toInt()
                velocity = actualVelocity - 0
            }
        } else if (velocity > 0) {
            if (position >= progressBar.width) {
                velocity = -starWarsVehicle.velocity
                position = progressBar.width
            } else {
                position = actualPosition + JBUIScale.scale(velocity).toInt()
                velocity = actualVelocity + 0
            }
        }
    }

    private fun resetPositionAndVelocity() {
        velocity = starWarsVehicle.velocity
        position = 0
    }

    private fun paintProgressBar(g: Graphics?, c: JComponent?, paintDeterminate: Boolean) {
        if (g == null || c == null){
            return
        }

        if (isUnsupported(g, c)) {
            if (paintDeterminate) {
                super.paintDeterminate(g, c)
            } else {
                super.paintIndeterminate(g, c)
            }
            return
        }
        val config = GraphicsUtil.setupAAPainting(g)
        val graphics2D = g as Graphics2D
        val border = progressBar.insets
        val width = progressBar.width
        var height = progressBar.preferredSize.height
        if (isOdd(c.height - height)) {
            height++
        }
        val barRectWidth = width - (border.right + border.left)
        val barRectHeight = height - (border.top + border.bottom)
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return
        }
        val amountFull = if (paintDeterminate) getAmountFull(border, barRectWidth, barRectHeight) else position
        val parent = c.parent
        val background = if (parent != null) parent.background else UIUtil.getPanelBackground()
        graphics2D.color = background
        if (c.isOpaque) {
            g.fillRect(0, 0, width, height)
        }
        val rectangle2D = getRoundRectangle(width, height)
        drawProgress(width, height, amountFull, graphics2D, rectangle2D)
        drawIcon(amountFull, graphics2D, rectangle2D)
        drawBorder(rectangle2D, graphics2D)
        paintStringIfNeeded(graphics2D, c, height, border, barRectWidth, barRectHeight, amountFull)
        config.restore()
    }

    private fun isUnsupported(graphics: Graphics, component: JComponent): Boolean {
        return graphics !is Graphics2D || progressBar.orientation != SwingConstants.HORIZONTAL ||
                !component.componentOrientation.isLeftToRight
    }

    private fun isOdd(value: Int): Boolean {
        return value % 2 == 1
    }

    private fun getRoundRectangle(width: Int, height: Int): RoundRectangle2D {
        val arcLength = JBUIScale.scale(9f)
        val offset = JBUIScale.scale(2f)
        return RoundRectangle2D.Float(JBUIScale.scale(1f),
                JBUIScale.scale(1f),
                width - offset,
                height - offset,
                arcLength,
                arcLength)
    }

    private fun drawProgress(width: Int, height: Int, progress: Int, graphics2D: Graphics2D, rectangle2D: RoundRectangle2D) {
        val paint = graphics2D.paint
        val clip = graphics2D.clip
        val movingRight = velocity >= 0
        val fillPaint = getFillPaint()
        graphics2D.paint = fillPaint
        graphics2D.clip = if (movingRight) Rectangle(progress, height) else Rectangle(progress, 0, progressBar.width, height)
        graphics2D.fill(rectangle2D)
        graphics2D.paint = getTransparencyPaint(progressBar.background, progress, width, movingRight)
        graphics2D.clip = if (movingRight) Rectangle(progress, height) else Rectangle(progress, 0, progressBar.width, height)
        graphics2D.fill(rectangle2D)
        graphics2D.paint = paint
        graphics2D.clip = clip
    }

    private fun getTransparencyPaint(backgroundColor: Color, progress: Int, width: Int, movingRight: Boolean): Paint {
        val transparent = JBColor(Color(0, 0, 0, 0), Color(0, 0, 0, 0))
        if (movingRight) {
            return if (progress > 0) {
                LinearGradientPaint(0f, JBUIScale.scale(2f), progress.toFloat(), JBUIScale.scale(2f), floatArrayOf(0f, 1f), arrayOf(backgroundColor, transparent))
            } else transparent
        }
        return if (progress < width) {
            LinearGradientPaint(progress.toFloat(), JBUIScale.scale(2f), width.toFloat(), JBUIScale.scale(2f), floatArrayOf(0f, 1f), arrayOf(transparent, backgroundColor))
        } else transparent
    }

    private fun getFillPaint(): Paint {
        return starWarsVehicle.color
    }

    private fun drawIcon(amountFull: Int, graphics2D: Graphics2D, clip: Shape) {
        val previousClip = graphics2D.clip
        graphics2D.clip = clip
        val isMovingRight = velocity >= 0
        val icon = if (isMovingRight) forwardIcon else backwardIcon
        val x = amountFull + if (isMovingRight) JBUIScale.scale(starWarsVehicle.xShift) else JBUIScale.scale(-icon.iconWidth - starWarsVehicle.xShift)
        val y = starWarsVehicle.yShift
        icon.paintIcon(progressBar, graphics2D, x, y)
        graphics2D.clip = previousClip
    }

    private fun drawBorder(rectangle2D: RoundRectangle2D, graphics2D: Graphics2D) {
        val color = graphics2D.color
        val stroke = graphics2D.stroke
        graphics2D.color = progressBar.foreground
        graphics2D.stroke = BasicStroke(2f)
        graphics2D.draw(rectangle2D)
        graphics2D.color = color
        graphics2D.stroke = stroke
    }

    private fun paintStringIfNeeded(graphics2D: Graphics2D, component: Component, height: Int, border: Insets, barRectWidth: Int, barRectHeight: Int, amountFull: Int) {
        if (progressBar.isStringPainted) {
            graphics2D.translate(0, -(component.height - height) / 2)
            paintString(graphics2D, border.left, border.top, barRectWidth, barRectHeight, amountFull, border)
        }
    }
}