import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.platform.gradle.TestFrameworkType
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import kotlin.collections.listOf

plugins {
    // Kotlin support
    alias(libs.plugins.kotlin)
    // Gradle IntelliJ Plugin
    alias(libs.plugins.intelliJPlatform)
    // Gradle Changelog Plugin
    alias(libs.plugins.changelog)
    // Kotlin linter
    alias(libs.plugins.detekt)
    // Gradle Kover Plugin
    alias(libs.plugins.kover)
    // ktlint Plugin
    alias(libs.plugins.ktlint)
    // Serialization plugin
    alias(libs.plugins.serialization)
}

// Dependencies are managed with Gradle version catalog - read more: https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog
dependencies {
    implementation(libs.kotlinx.serialization.json)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter.core)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation(libs.junit.platform.launcher)
    testImplementation(libs.junit.platform.suite.engine)
    // JUnit 4 is required at runtime by the IntelliJ Platform test framework (JUnit5TestSessionListener)
    testImplementation(libs.junit4)
    testImplementation(libs.mocking.mockk)

    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
        intellijIdea(providers.gradleProperty("platformVersion"))

        testFramework(TestFrameworkType.Platform)
    }
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.register<Copy>("installGitHooks") {
    from(file("$rootDir/scripts/pre-commit"))
    into(file("$rootDir/.git/hooks"))

    doLast {
        val hookFile = file("$rootDir/.git/hooks/pre-commit")
        if (hookFile.exists()) {
            hookFile.setExecutable(true)
        }
    }
}

tasks.named("build") {
    dependsOn("installGitHooks")
}

ktlint {
    verbose = true
    version = libs.versions.ktlint.get()
    outputToConsole = true
    coloredOutput = true
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.JSON)
        reporter(ReporterType.HTML)
    }
    baseline = file("config/ktlint/baseline.xml")
}

detekt {
    toolVersion = libs.versions.detekt.get()
    config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
    baseline.set(file("$rootDir/config/detekt/baseline.xml"))
    buildUponDefaultConfig = true
    allRules = false
}

// Configure IntelliJ Platform Gradle Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-extension.html
intellijPlatform {
    pluginConfiguration {
        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        description = providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
            val start = "<!-- Plugin description -->"
            val end = "<!-- Plugin description end -->"

            with(it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        }

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
        }
    }

    publishing {
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html#specifying-a-release-channel
        channels =
            providers.gradleProperty("version").map {
                listOf(it.substringAfter('-', "").substringBefore('.').ifEmpty { "default" })
            }
    }
}

// Configure Gradle Kover Plugin - read more: https://kotlin.github.io/kotlinx-kover/gradle-plugin/#configuration-details
kover {
    reports {
        total {
            xml {
                onCheck = true
            }
        }
    }
}

// Kotlin DSL
tasks.withType<dev.detekt.gradle.Detekt>().configureEach {
    reports {
        html.required = true
        html.outputLocation = file("build/reports/mydetekt.html")
        markdown.required = false
        sarif.required = false
    }
    jvmTarget.set("21")
}

tasks.withType<dev.detekt.gradle.DetektCreateBaselineTask>().configureEach {
    jvmTarget.set("21")
}
