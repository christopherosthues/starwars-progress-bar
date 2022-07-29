package com.christopherosthues.starwarsprogressbar

import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

open class StarWarsPluginExtension @Inject constructor(objectFactory: ObjectFactory) {
    @Optional
    val readmeFilePath = objectFactory.property<String>()

    @Optional
    val factionsFilePath = objectFactory.property<String>()

    @Optional
    val messagesFilePath = objectFactory.property<String>()

    @Optional
    val gradlePropertiesFile = objectFactory.property<String>()

    @Optional
    val increaseVersion = objectFactory.property<String>()
}
