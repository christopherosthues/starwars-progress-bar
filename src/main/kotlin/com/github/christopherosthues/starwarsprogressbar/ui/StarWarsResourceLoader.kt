package com.github.christopherosthues.starwarsprogressbar.ui

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.*
import java.util.concurrent.ExecutionException
import javax.swing.Icon
import javax.swing.ImageIcon

internal object StarWarsResourceLoader {
    private const val iconResourcePath = "com/github/christopherosthues/starwarsprogressbar/"

    private val cache: Cache<String, Icon> = CacheBuilder.newBuilder().maximumSize(100L).build()

    @JvmStatic
    fun getIcon(name: String): Icon {
        return getIconInternal(name)
    }

    @JvmStatic
    fun getReversedIcon(name: String): Icon {
        return getIconInternal(name + "_r")
    }

    private fun getIconInternal(name: String): Icon {
        val resourceName = "$iconResourcePath$name.png"
        return try {
            cache[resourceName, {
                Optional.ofNullable(
                        Optional.ofNullable(StarWarsResourceLoader.javaClass.classLoader.getResource(resourceName))
                                .orElseGet{ StarWarsResourceLoader.javaClass.classLoader.getResource("/$resourceName") })
                        .map { icon -> ImageIcon(icon) }
                        .orElseGet { ImageIcon() }
            }]
        }
        catch (ex: ExecutionException) {
            ImageIcon()
        }
    }
}
