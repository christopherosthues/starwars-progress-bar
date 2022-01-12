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

import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.*
import java.util.concurrent.ExecutionException
import javax.swing.Icon
import javax.swing.ImageIcon

internal object StarWarsResourceLoader {
    private const val iconResourcePath = "com/christopherosthues/starwarsprogressbar/"

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
