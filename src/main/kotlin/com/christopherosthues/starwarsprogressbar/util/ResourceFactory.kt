package com.christopherosthues.starwarsprogressbar.util

import com.christopherosthues.starwarsprogressbar.models.StarWarsFactions
import com.intellij.util.ui.UIUtil
import kotlinx.serialization.json.Json.Default.decodeFromString
import java.awt.Image
import java.awt.image.BufferedImage
import java.net.URL
import javax.swing.ImageIcon

internal fun createClassLoader(): ClassLoader = StarWarsResourceLoader.javaClass.classLoader

internal fun readTextFromUrl(url: URL): String = url.readText()

internal fun parseFactionsFromJson(json: String): StarWarsFactions {
    var loadedFactions = StarWarsFactions(listOf())
    try {
        loadedFactions = decodeFromString(StarWarsFactions.serializer(), json)
    } catch (exception: Exception) {
        return loadedFactions
    }

    return if (loadedFactions.factions != null) loadedFactions else StarWarsFactions(listOf())
}

internal fun createScaledEmptyImageIcon(size: Int): ImageIcon = ImageIcon(createEmptyBufferedImage(size, size))

internal fun createEmptyImageIconFromBufferedImage(size: Int): ImageIcon =
    ImageIcon(BufferedImage(size, size, BufferedImage.TRANSLUCENT))

internal fun createImageIconFromImage(image: Image): ImageIcon = ImageIcon(image)

internal fun createImageIconFromURL(url: URL): ImageIcon = ImageIcon(url)

internal fun createEmptyImageIcon(): ImageIcon = ImageIcon()

internal fun createEmptyTranslucentBufferedImage(width: Int, height: Int): BufferedImage =
    UIUtil.createImage(null, width, height, BufferedImage.TRANSLUCENT)

internal fun createEmptyBufferedImage(width: Int, height: Int): BufferedImage =
    BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
