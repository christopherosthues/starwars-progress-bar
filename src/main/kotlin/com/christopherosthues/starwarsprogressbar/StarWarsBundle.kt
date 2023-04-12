package com.christopherosthues.starwarsprogressbar

import com.intellij.AbstractBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey

@NonNls
private const val BUNDLE = "messages.StarWarsBundle"

internal object StarWarsBundle : AbstractBundle(BUNDLE) {
    // TODO: unit test me
    @Suppress("SpreadOperator")
    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any) =
        getMessage(key, *params)
}
