import org.jetbrains.changelog.date
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.tasks.RunPluginVerifierTask.FailureLevel
import kotlin.collections.listOf

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    id("java")
    // Kotlin support
    kotlin("jvm")
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.9.0"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "1.3.1"
    // Gradle Qodana Plugin
    id("org.jetbrains.qodana") version "0.1.13"
    // Kotlin linter
    id("io.gitlab.arturbosch.detekt").version("1.21.0-RC2")
    // Build Plugins
    id("com.christopherosthues.build")
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

// Set the JVM language level used to compile sources and generate files - Java 17 is required since 2022.2
kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.10")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.1")
    testImplementation("org.junit.platform:junit-platform-launcher:1.9.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.1")
    testImplementation("org.junit.platform:junit-platform-suite-engine:1.9.1")
    testImplementation("io.mockk:mockk:1.13.2")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

detekt {
    toolVersion = "1.21.0"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

// Configure Gradle IntelliJ Plugin - read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    updateSinceUntilBuild.set(false)

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    version.set(properties("pluginVersion"))
    groups.set(listOf("Added", "Changed", "Removed", "Fixed"))
    header.set(provider { "[${version.get()}] - ${date()}" })
}

// Configure Gradle Qodana Plugin - read more: https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    cachePath.set(projectDir.resolve(".qodana").canonicalPath)
    reportPath.set(projectDir.resolve("build/reports/inspections").canonicalPath)
    saveReport.set(true)
    showReport.set(System.getenv("QODANA_SHOW_REPORT")?.toBoolean() ?: false)
}

// Kotlin DSL
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        html.outputLocation.set(file("build/reports/mydetekt.html"))
        md.required.set(false)
        sarif.required.set(false)
        txt.required.set(false)
        xml.required.set(false)
    }
}

// Kotlin DSL
tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    this.jvmTarget = "17"
}
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    this.jvmTarget = "17"
}

tasks {
    wrapper {
        gradleVersion = properties("gradleVersion")
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        untilBuild.set(null as String?)
        sinceBuild.set(properties("pluginSinceBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(
            projectDir.resolve("README.md").readText().lines().run {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end))
            }.joinToString("\n").run { markdownToHTML(this) }
        )

        // Get the latest available change notes from the changelog file
        changeNotes.set(
            provider {
                changelog.run {
                    getOrNull(properties("pluginVersion")) ?: getLatest()
                }.toHTML()
            }
        )
    }

    runPluginVerifier {
        ideVersions.set(properties("pluginVerifierIdeVersions").split(",").map { it.trim() }.toList())
        failureLevel.set(FailureLevel.ALL)
    }

    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        dependsOn("includeVehicles")
        token.set(System.getenv("PUBLISH_TOKEN"))
        // pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(listOf(properties("pluginVersion").split('-').getOrElse(1) { "default" }.split('.').first()))
    }
}
