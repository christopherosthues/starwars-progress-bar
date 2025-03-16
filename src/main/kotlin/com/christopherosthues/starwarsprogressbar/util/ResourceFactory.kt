package com.christopherosthues.starwarsprogressbar.util

import com.christopherosthues.starwarsprogressbar.models.Lightsaber
import com.christopherosthues.starwarsprogressbar.models.StarWarsEntity
import com.christopherosthues.starwarsprogressbar.models.StarWarsFactions
import com.christopherosthues.starwarsprogressbar.models.StarWarsVehicle
import com.intellij.util.ui.UIUtil
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.decodeFromString
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import java.awt.Image
import java.awt.image.BufferedImage
import java.net.URL
import javax.swing.ImageIcon

internal fun createClassLoader(): ClassLoader = StarWarsResourceLoader.javaClass.classLoader

internal fun readTextFromUrl(url: URL): String = url.readText()

internal fun parseFactionsFromJson(json: String): StarWarsFactions {
    val starWarsModule = SerializersModule {
        polymorphic(StarWarsEntity::class) {
            subclass(Lightsaber::class, Lightsaber.serializer())
            subclass(StarWarsVehicle::class, StarWarsVehicle.serializer())
        }
    }
    val jsonFormat = Json {
        serializersModule = starWarsModule
        ignoreUnknownKeys = true
        classDiscriminator = "type"
    }

    var loadedFactions = StarWarsFactions(listOf(), listOf())
    try {
        loadedFactions = jsonFormat.decodeFromString(StarWarsFactions.serializer(), json)
    } catch (exception: Exception) {
        return loadedFactions
    }

    // TODO: test me
    return if (loadedFactions.vehicles != null || loadedFactions.lightsabers != null) loadedFactions else StarWarsFactions(
        listOf(),
        listOf()
    )
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
