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
package com.christopherosthues.starwarsprogressbar.util

import com.christopherosthues.starwarsprogressbar.models.Lightsabers
import com.christopherosthues.starwarsprogressbar.models.StarWarsFaction
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactions
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import com.intellij.openapi.util.IconLoader
import java.awt.Image
import java.awt.image.BufferedImage
import java.util.Optional
import java.util.concurrent.ExecutionException
import javax.swing.Icon
import javax.swing.ImageIcon

internal object StarWarsResourceLoader {
    private const val ICON_RESOURCE_PATH = "icons/"
    private const val MAXIMUM_CACHE_SIZE = 100L
    private const val SCALED_ICON_SIZE = 16
    private const val ICON_SIZE = 32
    private const val EMPTY_FACTIONS_JSON_DATA = "{\"lightsabers\": [],\"vehicles\": []}"

    private val cache: Cache<String, Icon> = CacheBuilder.newBuilder().maximumSize(MAXIMUM_CACHE_SIZE).build()

    fun getPluginIcon(): Icon = scaleIcon(
        IconLoader.getIcon("/META-INF/pluginIcon.svg", StarWarsResourceLoader.javaClass),
    )

    private fun scaleIcon(icon: Icon): ImageIcon {
        val w = icon.iconWidth
        val h = icon.iconHeight
        if (w <= 0 || h <= 0) {
            return createScaledEmptyImageIcon(SCALED_ICON_SIZE)
        }
        val bufferedImage = createEmptyTranslucentBufferedImage(w, h)
        val g = bufferedImage.createGraphics()
        icon.paintIcon(null, g, 0, 0)
        g.dispose()

        val image = bufferedImage.getScaledInstance(SCALED_ICON_SIZE, SCALED_ICON_SIZE, Image.SCALE_SMOOTH)

        return createImageIconFromImage(image)
    }

    @JvmStatic
    fun getFactionLogo(type: String, factionName: String, largeIcon: Boolean): BufferedImage {
        val iconSize = if (largeIcon) "@2x" else ""
        val icon = getIconInternal("$type/$factionName/logo$iconSize")

        return getImage(icon)
    }

    private fun getImage(icon: Icon): BufferedImage {
        val iconWidth = icon.iconWidth
        val iconHeight = icon.iconHeight

        val image = createEmptyBufferedImage(iconWidth, iconHeight)
        val graphics = image.createGraphics()
        icon.paintIcon(null, graphics, 0, 0)
        graphics.dispose()

        return image
    }

    @JvmStatic
    fun getImage(name: String): BufferedImage = getImage(getIconInternal(name))

    @JvmStatic
    fun getReversedImage(name: String): BufferedImage = getImage(getIconInternal(name + "_r"))

    @JvmStatic
    fun getIcon(name: String): Icon = getIconInternal(name)

    private fun getIconInternal(name: String): Icon {
        val resourceName = "$ICON_RESOURCE_PATH$name.png"
        return try {
            cache[
                resourceName, {
                    Optional.ofNullable(
                        Optional.ofNullable(createClassLoader().getResource(resourceName))
                            .orElseGet { createClassLoader().getResource("/$resourceName") },
                    )
                        .map { icon -> createImageIconFromURL(icon) }
                        .orElseGet { createEmptyImageIconFromBufferedImage(ICON_SIZE) }
                },
            ]
        } catch (ex: ExecutionException) {
            createEmptyImageIcon()
        }
    }

    fun loadFactions(): StarWarsFactions {
        val factionsFileName = "json/factions.json"
        val json = Optional.ofNullable(
            Optional.ofNullable(createClassLoader().getResource(factionsFileName))
                .orElseGet { createClassLoader().getResource("/$factionsFileName") },
        )
            .map { file -> readTextFromUrl(file) }
            .orElseGet { EMPTY_FACTIONS_JSON_DATA }
        val factions = parseFactionsFromJson(json)

        setFactionForVehicles(factions.vehicles)
        setFactionForLightsabers(factions.lightsabers)

        return factions
    }

    private fun setFactionForVehicles(factions: List<StarWarsFaction<StarWarsVehicle>>) {
        factions.forEach { faction ->
            faction.data.forEach {
                it.factionId = faction.id
            }
        }
    }

    private fun setFactionForLightsabers(factions: List<StarWarsFaction<Lightsabers>>) {
        factions.forEach { faction ->
            faction.data.forEach {
                it.factionId = faction.id
            }
        }
    }
}
