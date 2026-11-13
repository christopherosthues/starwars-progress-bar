package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.configuration.StarWarsState
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_DRAW_SILHOUETTES
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_FACTION_CRESTS
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SHOW_ICON
import com.christopherosthues.starwarsprogressbar.constants.DEFAULT_SOLID_PROGRESS_BAR_COLOR
import com.christopherosthues.starwarsprogressbar.models.Blade
import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.ui.components.ColoredImageComponent
import com.christopherosthues.starwarsprogressbar.util.StarWarsResourceLoader
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import java.awt.Color
import java.awt.Graphics2D
import java.awt.LinearGradientPaint
import java.awt.Paint
import java.awt.RenderingHints
import javax.swing.JComponent

private const val LIGHTSABER_PROGRESSBAR_HEIGHT = 20
private const val FACTION_CREST_X_POSITION = 4.0

internal class LightsaberProgressBarDecorator(private val starWarsState: () -> StarWarsState?) {
    private var lightsaberIcons: MutableList<ColoredImageComponent> = mutableListOf()
    private lateinit var factionCrestIcon: ColoredImageComponent

    internal fun update(lightsabers: Lightsabers) {
        lightsaberIcons.clear()
        lightsabers.lightsabers.forEach {
            if (lightsabers.isJarKai) {
                lightsaberIcons.add(
                    ColoredImageComponent(StarWarsResourceLoader.getImage(lightsabers.fileName + "_${it.id}")),
                )
            } else {
                lightsaberIcons.add(ColoredImageComponent(StarWarsResourceLoader.getImage(lightsabers.fileName)))
            }
        }

        factionCrestIcon =
            ColoredImageComponent(StarWarsResourceLoader.getFactionLogo("lightsabers", lightsabers.factionId, false))
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
        val showIcon = starWarsState()?.showIcon ?: DEFAULT_SHOW_ICON
        val layouts = LightsaberLayoutCalculator.compute(
            lightsabers,
            lightsaberIcons.map { it.width },
            width,
            amountFull,
            showIcon,
        )

        layouts.zip(lightsaberIcons).forEach { (layout, icon) ->
            drawLightsaber(graphics2D, component, icon, layout)
        }

        drawFactionCrest(width, height, graphics2D, component)
    }

    private fun drawLightsaber(
        graphics2D: Graphics2D,
        component: JComponent,
        lightsaberIcon: ColoredImageComponent,
        layout: LightsaberLayout,
    ) {
        val paint = graphics2D.paint
        val clip = graphics2D.clip
        val color = graphics2D.color

        // Enable anti-aliasing for smoother rendering
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        layout.blades.forEach { bladeGeometry ->
            drawBorder(
                graphics2D,
                bladeGeometry.borderWidth,
                bladeGeometry.borderX,
                bladeGeometry.y,
                bladeGeometry.height,
            )
            drawBlade(
                graphics2D,
                bladeGeometry.blade,
                bladeGeometry.x,
                bladeGeometry.y,
                bladeGeometry.width,
                bladeGeometry.height,
            )
        }

        layout.hilt?.let { hilt ->
            drawHilt(graphics2D, component, lightsaberIcon, hilt.x, hilt.y)
        }

        graphics2D.color = color
        graphics2D.paint = paint
        graphics2D.clip = clip
    }

    private fun drawHilt(
        graphics2D: Graphics2D,
        component: JComponent,
        lightsaberIcon: ColoredImageComponent,
        hiltX: Int,
        hiltY: Int,
    ) {
        if (starWarsState()?.drawSilhouettes ?: DEFAULT_DRAW_SILHOUETTES) {
            graphics2D.color = component.foreground
            lightsaberIcon.foreground = component.foreground
        } else {
            lightsaberIcon.foreground = null
        }

        lightsaberIcon.paint(graphics2D, hiltX, hiltY)
    }

    private fun drawBorder(graphics2D: Graphics2D, bladeWidth: Int, bladeX: Int, bladeY: Int, bladeSize: Int) {
        graphics2D.paint = JBColor(Color.LIGHT_GRAY, Color.DARK_GRAY)

        graphics2D.fillRoundRect(bladeX, bladeY, bladeWidth, bladeSize, bladeSize, bladeSize)
    }

    private fun drawBlade(
        graphics2D: Graphics2D,
        blade: Blade,
        bladeX: Int,
        bladeY: Int,
        bladeWidth: Int,
        bladeHeight: Int,
    ) {
        val arc = bladeHeight

        // Define colors for the vertical gradient
        val bladePaint = bladePaint(blade, bladeY, bladeHeight)

        graphics2D.paint = bladeGlow(blade)

        val bladeYScale = 3
        val bladeHeightScale = 6
        graphics2D.fillRoundRect(
            bladeX,
            bladeY - JBUIScale.scale(bladeYScale),
            bladeWidth,
            bladeHeight + JBUIScale.scale(bladeHeightScale),
            arc,
            arc,
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
                FACTION_CREST_X_POSITION.toFloat(),
            )
            factionCrestIcon.paint(graphics2D, x.toInt(), y.toInt())

            graphics2D.clip = previousClip
            graphics2D.color = previousColor
        }
    }

    private fun bladePaint(blade: Blade, bladeY: Int, bladeHeight: Int): Paint {
        val bladeColor = blade.color

        if (starWarsState()?.solidProgressBarColor ?: DEFAULT_SOLID_PROGRESS_BAR_COLOR) {
            return bladeColor
        }

        val colorMiddle = JBColor(Color.WHITE, Color.WHITE)

        val fractions = floatArrayOf(0.0f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f)
        val colors = arrayOf(
            bladeColor.brighter().brighter(),
            bladeColor,
            colorMiddle,
            colorMiddle,
            bladeColor,
            bladeColor.brighter().brighter(),
        )
        val gradient = LinearGradientPaint(0f, bladeY.toFloat(), 0f, bladeY + bladeHeight.toFloat(), fractions, colors)
        return gradient
    }

    private fun bladeGlow(blade: Blade): Paint {
        val transparent = JBColor(Color(0, 0, 0, 0), Color(0, 0, 0, 0))
        if (starWarsState()?.solidProgressBarColor ?: DEFAULT_SOLID_PROGRESS_BAR_COLOR) {
            return transparent
        }

        val bladeColor = blade.color
        val glowAlpha = 50
        return JBColor(
            Color(bladeColor.red, bladeColor.green, bladeColor.blue, glowAlpha),
            Color(bladeColor.red, bladeColor.green, bladeColor.blue, glowAlpha),
        )
    }
}
