/**
 * MIT License
 *
 * Copyright 2020-2021 Karl Goffin
 * Copyright 2022 Christopher Osthues
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO
 * THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.christopherosthues.starwarsprogressbar.ui

import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactions
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.google.gson.Gson
import com.intellij.ui.IconManager
import com.intellij.util.ui.UIUtil
import java.awt.Image
import java.awt.image.BufferedImage
import java.util.*
import java.util.concurrent.ExecutionException
import javax.swing.Icon
import javax.swing.ImageIcon


internal object StarWarsResourceLoader {
    private const val ICON_RESOURCE_PATH = "icons/"
    private const val MAXIMUM_CACHE_SIZE = 100L
    private const val ICON_SIZE = 16

    private val cache: Cache<String, Icon> = CacheBuilder.newBuilder().maximumSize(MAXIMUM_CACHE_SIZE).build()

    fun getPluginIcon(): Icon {
        return scaleIcon(
            IconManager.getInstance().getIcon("/META-INF/pluginIcon.svg", StarWarsResourceLoader.javaClass)
        )
    }

    private fun scaleIcon(icon: Icon): ImageIcon {
        val w = icon.iconWidth
        val h = icon.iconHeight
        val bufferedImage = UIUtil.createImage(null, w, h, BufferedImage.TYPE_INT_ARGB)
        val g = bufferedImage.createGraphics()
        icon.paintIcon(null, g, 0, 0)
        g.dispose()

        val image = bufferedImage.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH)

        return ImageIcon(image)
    }

    @JvmStatic
    fun getIcon(name: String): Icon {
        return getIconInternal(name)
    }

    @JvmStatic
    fun getReversedIcon(name: String): Icon {
        return getIconInternal(name + "_r")
    }

    private fun getIconInternal(name: String): Icon {
        val resourceName = "$ICON_RESOURCE_PATH$name.png"
        return try {
            cache[resourceName, {
                Optional.ofNullable(
                    Optional.ofNullable(StarWarsResourceLoader.javaClass.classLoader.getResource(resourceName))
                        .orElseGet { StarWarsResourceLoader.javaClass.classLoader.getResource("/$resourceName") })
                    .map { icon -> ImageIcon(icon) }
                    .orElseGet { ImageIcon() }
            }]
        } catch (ex: ExecutionException) {
            ImageIcon()
        }
    }

    fun loadFactions(): StarWarsFactions {
        val json = Optional.ofNullable(
            Optional.ofNullable(StarWarsResourceLoader.javaClass.classLoader.getResource("json/factions.json"))
                .orElseGet { StarWarsResourceLoader.javaClass.classLoader.getResource("/json/factions.json") })
            .map { file -> file.readText() }
            .orElseGet { "" }
        val gson = Gson()
        val factions = gson.fromJson(json, StarWarsFactions::class.java)

        setFactionForVehicles(factions.factions)

        return factions
    }

    private fun setFactionForVehicles(factions: List<StarWarsFaction>) {
        factions.forEach { faction ->
            faction.vehicles.forEach {
                it.faction = faction
            }
        }
    }
}
