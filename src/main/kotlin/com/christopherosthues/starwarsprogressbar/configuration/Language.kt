package com.christopherosthues.starwarsprogressbar.configuration

import java.util.Locale

enum class Language(private val languageCode: String, private val displayLanguage: String) {
    ENGLISH("en", "English"),
    GERMAN("de", "Deutsch"),
    SPANISH("es", "Espa\u00f1ol"),
    ;

    // TODO: use Locale.of after migration to Java 21
    fun toLocale(): Locale = Locale(languageCode)

    override fun toString(): String = displayLanguage
}
