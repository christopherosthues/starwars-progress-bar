package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.ui.components.ColoredImageComponent
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import java.awt.*
import javax.swing.JComponent
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

private const val LIGHTSABER_PROGRESSBAR_HEIGHT = 20
private const val FACTION_CREST_X_POSITION = 4.0

internal class LightsaberProgressBarDecorator(private val starWarsState: () -> StarWarsState?) {
    private var lightsaberIcons: MutableList<ColoredImageComponent> = mutableListOf()
    private var lightsaberDrawings: MutableList<LightsaberDrawing> = mutableListOf()
    private lateinit var factionCrestIcon: ColoredImageComponent
    private var isPrecomputed = false

    private enum class DrawState {
        SingleBladed,
        DoubleBladed,
        NoBlade
    }

    private data class LightsaberDrawing(var state: DrawState, var bladeX: Int = 0, var secondBladeX: Int = 0, var maxBladeWidth: Int = 0)

    internal fun update(lightsabers: Lightsabers) {
        lightsaberIcons.clear()
        lightsaberDrawings.clear()
        lightsabers.lightsabers.forEach {
            if (lightsabers.isJarKai) {
                lightsaberIcons.add(ColoredImageComponent(StarWarsResourceLoader.getImage(lightsabers.fileName + "_${it.id}")))
            } else {
                lightsaberIcons.add(ColoredImageComponent(StarWarsResourceLoader.getImage(lightsabers.fileName)))
            }

            lightsaberDrawings.add(LightsaberDrawing(getDrawingState(it)))
        }
        factionCrestIcon =
            ColoredImageComponent(StarWarsResourceLoader.getFactionLogo("lightsabers", lightsabers.factionId, false))

        isPrecomputed = false
    }

    private fun getDrawingState(lightsaber: Lightsaber): DrawState {
        if (lightsaber.isDoubleBladed) {
            return DrawState.DoubleBladed
        }

        return DrawState.SingleBladed
    }

    private fun computeLightsaberDrawings(lightsabers: Lightsabers, width: Int) {
        var maxRightIconWidthSingle = 0
        var maxLeftIconWidthSingle = 0
        lightsabers.lightsabers.forEachIndexed { index, lightsaber ->
            val lightsaberIcon = lightsaberIcons[index]
            val iconWidth = lightsaberIcon.width
            if (lightsaber.id.isOdd()) {
                if (!lightsaber.isDoubleBladed) {
                    maxLeftIconWidthSingle = max(maxLeftIconWidthSingle, iconWidth)
                }
            } else {
                if (!lightsaber.isDoubleBladed) {
                    maxRightIconWidthSingle = max(maxRightIconWidthSingle, iconWidth)
                }
            }
        }

        val singleBladeLength = width - maxLeftIconWidthSingle - maxRightIconWidthSingle
        val singleLeftBladeX = maxLeftIconWidthSingle
        val singleRightBladeX = width - maxRightIconWidthSingle

        lightsabers.lightsabers.forEachIndexed { index, lightsaber ->
            val iconWidth = lightsaberIcons[index].width
            if (lightsaber.isDoubleBladed) {
                if (singleBladeLength - iconWidth >= iconWidth) {
                    lightsaberDrawings[index].bladeX = (maxLeftIconWidthSingle + singleRightBladeX) / 2 - iconWidth / 2
                    lightsaberDrawings[index].secondBladeX = (maxLeftIconWidthSingle + singleRightBladeX) / 2 + iconWidth / 2
                    lightsaberDrawings[index].maxBladeWidth = singleBladeLength - iconWidth
                } else {
                    lightsaberDrawings[index].state = DrawState.NoBlade
                }
            } else {
                val singleIconWidth = if (lightsaber.id.isOdd()) maxLeftIconWidthSingle else maxRightIconWidthSingle
                if (singleBladeLength - singleIconWidth >= iconWidth) {
                    lightsaberDrawings[index].bladeX = if (lightsaber.id.isOdd()) singleLeftBladeX else singleRightBladeX
                    lightsaberDrawings[index].maxBladeWidth = singleBladeLength
                } else {
                    lightsaberDrawings[index].state = DrawState.NoBlade
                }
            }
        }

        isPrecomputed = true
    }

    internal fun getHeight(): Int = JBUIScale.scale(LIGHTSABER_PROGRESSBAR_HEIGHT)

    internal fun paintProgressBar(
        lightsabers: Lightsabers,
        graphics2D: Graphics2D,
        component: JComponent,
        width: Int,
        height: Int,
        amountFull: Int,
    ) {
        if (!isPrecomputed) {
            computeLightsaberDrawings(lightsabers, width)
        }

        lightsabers.lightsabers.indices.forEach {
            drawLightsaber(
                graphics2D,
                component,
                lightsabers,
                it,
                width,
                height,
                amountFull
            )
        }

        drawFactionCrest(width, height, graphics2D, component)
    }

    private fun drawLightsaber(
        graphics2D: Graphics2D,
        component: JComponent,
        lightsabers: Lightsabers,
        index: Int,
        width: Int,
        height: Int,
        amountFull: Int
    ) {
        val lightsaber = lightsabers.lightsabers[index]
        val lightsaberIcon = lightsaberIcons[index]
        val paint = graphics2D.paint
        val clip = graphics2D.clip
        val color = graphics2D.color

        // Enable anti-aliasing for smoother rendering
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        val lightsaberDrawing = lightsaberDrawings[index]
        when (lightsaberDrawing.state) {
            DrawState.DoubleBladed -> {
                drawDoubleBladedLightsaberWithHilt(
                    graphics2D,
                    component,
                    lightsaber,
                    lightsaberDrawing,
                    lightsaberIcon,
                    width,
                    amountFull
                )
            }
            DrawState.SingleBladed -> {
                drawSingleBladedLightsaberWithHilt(
                    graphics2D,
                    component,
                    lightsaber,
                    lightsaberDrawing,
                    lightsaberIcon,
                    width,
                    amountFull
                )
            }
            else -> {
                drawLightsaberWithoutHilt(graphics2D, lightsaber, width, height, amountFull)
            }
        }

        graphics2D.color = color
        graphics2D.paint = paint
        graphics2D.clip = clip
    }

    private fun drawSingleBladedLightsaberWithHilt(
        graphics2D: Graphics2D,
        component: JComponent,
        lightsaber: Lightsaber,
        lightsaberDrawing: LightsaberDrawing,
        lightsaberIcon: ColoredImageComponent,
        width: Int,
        amountFull: Int,
    ) {
        val singleBladeLength = lightsaberDrawing.maxBladeWidth + abs(JBUIScale.scale(lightsaber.xBlade))
        val ratio = singleBladeLength.toFloat() / width
        val full = amountFull * ratio

        val scaledAmountFull = if (lightsaber.isShoto) full / 2 else full
        val bladeX =
            JBUIScale.scale(lightsaber.xBlade) + if (lightsaber.id.isOdd()) lightsaberDrawing.bladeX else lightsaberDrawing.bladeX - scaledAmountFull.roundToInt()
        val bladeY = JBUIScale.scale(lightsaber.yShift + lightsaber.yBlade)
        val bladeHeight = JBUIScale.scale(lightsaber.bladeSize)
        val bladeWidth = scaledAmountFull.roundToInt()

        var maxBladeWidth = lightsaberDrawing.maxBladeWidth.toFloat() + abs(JBUIScale.scale(lightsaber.xBlade)).toFloat()
        maxBladeWidth = if (lightsaber.isShoto) maxBladeWidth / 2 else maxBladeWidth
        val borderX = JBUIScale.scale(lightsaber.xBlade) + if (lightsaber.id.isOdd()) lightsaberDrawing.bladeX else lightsaberDrawing.bladeX - maxBladeWidth.roundToInt()
        drawBorder(graphics2D, lightsaber, maxBladeWidth.roundToInt(), borderX, bladeY)
        drawBlade(graphics2D, lightsaber, bladeX, bladeY, bladeWidth, bladeHeight)

        val hiltX = if (lightsaber.id.isOdd()) 0 else lightsaberDrawing.bladeX
        val hiltY = JBUIScale.scale(lightsaber.yShift)
        drawHilt(graphics2D, component, lightsaberIcon, hiltX, hiltY)
    }

    private fun drawHilt(
        graphics2D: Graphics2D,
        component: JComponent,
        lightsaberIcon: ColoredImageComponent,
        hiltX: Int,
        hiltY: Int
    ) {
        if (starWarsState()?.drawSilhouettes ?: DEFAULT_DRAW_SILHOUETTES) {
            graphics2D.color = component.foreground
            lightsaberIcon.foreground = component.foreground
        } else {
            lightsaberIcon.foreground = null
        }

        lightsaberIcon.paint(graphics2D, hiltX, hiltY)
    }

    private fun drawLightsaberWithoutHilt(
        graphics2D: Graphics2D,
        lightsaber: Lightsaber,
        width: Int,
        height: Int,
        amountFull: Int
    ) {
        val bladeX = if (lightsaber.id.isOdd()) 0 else width
        val bladeY = JBUIScale.scale(lightsaber.yShift + lightsaber.yBlade)
        val bladeHeight = JBUIScale.scale(lightsaber.bladeSize)
        val bladeWidth = amountFull

        drawBorder(graphics2D, lightsaber, width, 0, bladeY)
        drawBlade(graphics2D, lightsaber, bladeX, bladeY, bladeWidth, bladeHeight)
    }

    private fun drawBorder(
        graphics2D: Graphics2D,
        lightsaber: Lightsaber,
        bladeWidth: Int,
        bladeX: Int,
        bladeY: Int,
    ) {
        val arc = lightsaber.bladeSize
        graphics2D.paint = JBColor(Color.LIGHT_GRAY, Color.DARK_GRAY)

        graphics2D.fillRoundRect(bladeX, bladeY, bladeWidth, lightsaber.bladeSize, arc, arc)
    }

    private fun drawBlade(
        graphics2D: Graphics2D,
        lightsaber: Lightsaber,
        bladeX: Int,
        bladeY: Int,
        bladeWidth: Int,
        bladeHeight: Int,
    ) {
        val arc = bladeHeight

        // Define colors for the vertical gradient
        val bladePaint = bladePaint(lightsaber, bladeY, bladeHeight)

        graphics2D.paint = bladeGlow(lightsaber)

        graphics2D.fillRoundRect(
            bladeX,
            bladeY - JBUIScale.scale(3),
            bladeWidth,
            bladeHeight + JBUIScale.scale(6),
            arc,
            arc
        )

        graphics2D.paint = bladePaint

        graphics2D.fillRoundRect(bladeX, bladeY, bladeWidth, bladeHeight, arc, arc)
    }

    private fun drawFactionCrest(width: Int, height: Int, graphics2D: Graphics2D, component: JComponent) {
        if (starWarsState()?.showFactionCrests ?: DEFAULT_SHOW_FACTION_CRESTS) {
            val previousClip = graphics2D.clip
            val previousColor = graphics2D.color
            graphics2D.color = component.foreground

            var x = JBUIScale.scale(FACTION_CREST_X_POSITION.toFloat()).toDouble()
            val y = (height.toDouble() - JBUIScale.scale(factionCrestIcon.preferredSize.height)) / 2

            factionCrestIcon.foreground = component.foreground
            // left crest
            factionCrestIcon.paint(graphics2D, x.toInt(), y.toInt())

            // middle crest
            x = (width.toDouble() - JBUIScale.scale(factionCrestIcon.preferredSize.width)) / 2
            factionCrestIcon.paint(graphics2D, x.toInt(), y.toInt())

            // right crest
            x = width.toDouble() - JBUIScale.scale(factionCrestIcon.preferredSize.width) - JBUIScale.scale(
                FACTION_CREST_X_POSITION.toFloat()
            )
            factionCrestIcon.paint(graphics2D, x.toInt(), y.toInt())

            graphics2D.clip = previousClip
            graphics2D.color = previousColor
        }
    }

    private fun bladePaint(lightsaber: Lightsaber, bladeY: Int, bladeHeight: Int): Paint {
        val lightsaberColor = lightsaber.color

        if (starWarsState()?.solidProgressBarColor ?: DEFAULT_SOLID_PROGRESS_BAR_COLOR) {
            return lightsaberColor
        }

        val colorMiddle = JBColor(Color.WHITE, Color.WHITE)

        val fractions = floatArrayOf(0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f)
        val colors = arrayOf(
            lightsaberColor.brighter().brighter(),
            lightsaberColor,
            colorMiddle,
            colorMiddle,
            lightsaberColor,
            lightsaberColor.brighter().brighter()
        )
        val gradient = LinearGradientPaint(0f, bladeY.toFloat(), 0f, bladeY + bladeHeight.toFloat(), fractions, colors)
        return gradient
    }

    private fun bladeGlow(lightsaber: Lightsaber): Paint {
        val transparent = JBColor(Color(0, 0, 0, 0), Color(0, 0, 0, 0))
        if (starWarsState()?.solidProgressBarColor ?: DEFAULT_SOLID_PROGRESS_BAR_COLOR) {
            return transparent
        }

        val lightsaberColor = lightsaber.color
        return JBColor(
            Color(lightsaberColor.red, lightsaberColor.green, lightsaberColor.blue, 50),
            Color(lightsaberColor.red, lightsaberColor.green, lightsaberColor.blue, 50)
        )
    }

    private fun drawDoubleBladedLightsaberWithHilt(
        graphics2D: Graphics2D,
        component: JComponent,
        lightsaber: Lightsaber,
        lightsaberDrawing: LightsaberDrawing,
        lightsaberIcon: ColoredImageComponent,
        width: Int,
        amountFull: Int
    ) {
        val singleBladeLength = lightsaberDrawing.maxBladeWidth + abs(JBUIScale.scale(lightsaber.xBlade))
        val ratio = singleBladeLength.toFloat() / width
        var bladeWidth = amountFull * ratio / 2
        bladeWidth = if (lightsaber.isShoto) bladeWidth / 2 else bladeWidth

        var bladeX =
            JBUIScale.scale(lightsaber.xBlade) + lightsaberDrawing.bladeX - bladeWidth.roundToInt()
        val bladeY = JBUIScale.scale(lightsaber.yShift + lightsaber.yBlade)
        val bladeHeight = JBUIScale.scale(lightsaber.bladeSize)

        var maxBladeWidth = lightsaberDrawing.maxBladeWidth.toFloat() + abs(JBUIScale.scale(lightsaber.xBlade)).toFloat()
        maxBladeWidth = if (lightsaber.isShoto) maxBladeWidth / 4 else maxBladeWidth / 2
        var borderX = JBUIScale.scale(lightsaber.xBlade) + lightsaberDrawing.bladeX - maxBladeWidth.roundToInt()
        drawBorder(graphics2D, lightsaber, maxBladeWidth.roundToInt(), borderX, bladeY)
        drawBlade(graphics2D, lightsaber, bladeX, bladeY, bladeWidth.roundToInt(), bladeHeight)

        bladeX = lightsaberDrawing.secondBladeX - JBUIScale.scale(lightsaber.xBlade)
        borderX = lightsaberDrawing.secondBladeX - JBUIScale.scale(lightsaber.xBlade)
        drawBorder(graphics2D, lightsaber, maxBladeWidth.roundToInt(), borderX, bladeY)
        drawBlade(graphics2D, lightsaber, bladeX, bladeY, bladeWidth.roundToInt(), bladeHeight)

        val hiltY = JBUIScale.scale(lightsaber.yShift)
        drawHilt(graphics2D, component, lightsaberIcon, lightsaberDrawing.bladeX, hiltY)
    }
}
