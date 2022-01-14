package com.christopherosthues.readme

import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.Optional
import org.gradle.kotlin.dsl.property
import javax.inject.Inject

open class ReadmePluginExtension @Inject constructor(objectFactory: ObjectFactory) {
    @Optional
    val readmeFilePath = objectFactory.property<String>()

    @Optional
    val factionsFilePath = objectFactory.property<String>()

    @Optional
    val messagesFilePath = objectFactory.property<String>()
}