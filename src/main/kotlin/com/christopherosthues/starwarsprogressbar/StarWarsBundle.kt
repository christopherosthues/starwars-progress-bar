package com.christopherosthues.starwarsprogressbar

import com.intellij.AbstractBundle
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey
import java.text.MessageFormat
import java.util.*

@NonNls
private const val BUNDLE = "messages.StarWarsBundle"

internal object StarWarsBundle : AbstractBundle(BUNDLE) {
    private var currentLocale: Locale = Locale.getDefault()

    fun setLocale(locale: Locale) {
        currentLocale = locale
        ResourceBundle.clearCache()
    }

    @JvmStatic
    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
        val pattern = ResourceBundle.getBundle(BUNDLE, currentLocale).getString(key)
        return MessageFormat.format(pattern, *params)
    }
}
