package com.christopherosthues.starwarsprogressbar.ui.components

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import javax.swing.JComponent

private const val TRANSPARENT = 0x00000000L
private const val NOT_TRANSPARENT = 0xFF000000

internal class ColoredImageComponent(private val image: BufferedImage) : JComponent() {
    init {
        preferredSize = Dimension(image.width, image.height)
        minimumSize = preferredSize
    }

    override fun paint(g: Graphics) {
        paint(g, 0, 0)
    }

    fun paint(g: Graphics, x: Int, y: Int) {
        val graphics2d = g.create()

        if (graphics2d is Graphics2D) {
            val transform = graphics2d.transform

            graphics2d.background = background

            graphics2d.drawImage(image, x, y, image.width, image.height) { _, _, _, _, _, _ -> false }

            graphics2d.transform = transform
            graphics2d.dispose()
        }

        super.paint(g)
    }

    override fun setForeground(fg: Color?) {
        requireNotNull(fg)

        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                val isTransparent = image.getRGB(x, y).toLong() and NOT_TRANSPARENT == TRANSPARENT
                if (!isTransparent) {
                    image.setRGB(x, y, fg.rgb)
                }
            }
        }
        super.setForeground(fg)
    }
}
