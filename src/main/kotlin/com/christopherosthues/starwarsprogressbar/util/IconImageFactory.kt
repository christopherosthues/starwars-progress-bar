package com.christopherosthues.starwarsprogressbar.util

import com.christopherosthues.starwarsprogressbar.models.StarWarsFactions
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.intellij.util.ui.UIUtil
import java.awt.Image
import java.awt.image.BufferedImage
import java.net.URL
import javax.swing.ImageIcon

// TODO: unit test me

internal fun createClassLoader(): ClassLoader {
    return StarWarsResourceLoader.javaClass.classLoader
}

internal fun readTextFromUrl(url: URL): String {
    return url.readText()
}

internal fun parseFactionsFromJson(json: String): StarWarsFactions {
    val gson = Gson()
    var loadedFactions = StarWarsFactions(listOf())
    try {
        loadedFactions = gson.fromJson(json, StarWarsFactions::class.java)
    } catch (npe: NullPointerException) {
        return loadedFactions
    } catch (jse: JsonSyntaxException) {
        return loadedFactions
    }

    if (loadedFactions.factions == null) {
        return StarWarsFactions(listOf())
    }

    return loadedFactions
}

internal fun createScaledEmptyImageIcon(size: Int): ImageIcon {
    return ImageIcon(createEmptyBufferedImage(size, size))
}

internal fun createEmptyImageIconFromBufferedImage(size: Int): ImageIcon {
    return ImageIcon(BufferedImage(size, size, BufferedImage.TRANSLUCENT))
}

internal fun createImageIconFromImage(image: Image): ImageIcon {
    return ImageIcon(image)
}

internal fun createImageIconFromURL(url: URL): ImageIcon {
    return ImageIcon(url)
}

internal fun createEmptyImageIcon(): ImageIcon {
    return ImageIcon()
}

internal fun createEmptyTranslucentBufferedImage(width: Int, height: Int): BufferedImage {
    return UIUtil.createImage(null, width, height, BufferedImage.TRANSLUCENT)
}

internal fun createEmptyBufferedImage(width: Int, height: Int): BufferedImage {
    return BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
}
