package com.christopherosthues.starwarsprogressbar.versionbump

import com.christopherosthues.starwarsprogressbar.StarWarsPluginConstants

class Version(private var version: String) {
    private var majorVersion: Int
    private var minorVersion: Int
    private var patchVersion: Int

    init {
        version.let {
            majorVersion = it.substringBefore(".").toInt()
            minorVersion = it.substringAfter(".").substringBefore(".").toInt()
            patchVersion = it.substringAfterLast(".").toInt()
        }
    }

    fun increaseVersion(versionToIncrease: String) {
        when (versionToIncrease) {
            StarWarsPluginConstants.MAJOR_VERSION -> majorVersion++
            StarWarsPluginConstants.MINOR_VERSION -> minorVersion++
            StarWarsPluginConstants.PATCH_VERSION -> patchVersion++
            else -> throw IllegalArgumentException("Version $versionToIncrease to be increase is not supported using Semantic Versioning.")
        }
    }

    fun get(): String {
        return "$majorVersion.$minorVersion.$patchVersion"
    }
}
