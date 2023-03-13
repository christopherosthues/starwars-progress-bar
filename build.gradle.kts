import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.markdownToHTML
import org.jetbrains.intellij.tasks.RunPluginVerifierTask.FailureLevel
import kotlin.collections.listOf

fun properties(key: String) = providers.gradleProperty(key)
fun environment(key: String) = providers.environmentVariable(key)

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.13.2"
    // Gradle Changelog Plugin
    id("org.jetbrains.changelog") version "2.0.0"
    // Gradle Qodana Plugin
    id("org.jetbrains.qodana") version "0.1.13"
    // Kotlin linter
    id("io.gitlab.arturbosch.detekt").version("1.22.0")
    // Gradle Kover Plugin
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    // Build Plugins
//    id("com.christopherosthues.build")
}

group = properties("pluginGroup").get()
version = properties("pluginVersion").get()

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
    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("org.junit.platform:junit-platform-launcher:1.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.junit.platform:junit-platform-suite-engine:1.9.2")
    testImplementation("io.mockk:mockk:1.13.4")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

detekt {
    toolVersion = "1.22.0"
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
    plugins.set(properties("platformPlugins").map { it.split(',').map(String::trim).filter(String::isNotEmpty) })
}

// Configure Gradle Changelog Plugin - read more: https://github.com/JetBrains/gradle-changelog-plugin
changelog {
    groups.set(listOf("Added", "Changed", "Removed", "Fixed"))
    repositoryUrl.set(properties("pluginRepositoryUrl"))
}

// Configure Gradle Qodana Plugin - read more: https://github.com/JetBrains/gradle-qodana-plugin
qodana {
    cachePath.set(provider { file(".qodana").canonicalPath })
    reportPath.set(provider { file("build/reports/inspections").canonicalPath })
    saveReport.set(true)
    showReport.set(environment("QODANA_SHOW_REPORT").map { it.toBoolean() }.getOrElse(false))
}

// Configure Gradle Kover Plugin - read more: https://github.com/Kotlin/kotlinx-kover#configuration
kover.xmlReport {
    onCheck.set(true)
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
        gradleVersion = properties("gradleVersion").get()
    }

    patchPluginXml {
        version.set(properties("pluginVersion"))
        untilBuild.set(null as String?)
        sinceBuild.set(properties("pluginSinceBuild"))

        // Extract the <!-- Plugin description --> section from README.md and provide for the plugin's manifest
        pluginDescription.set(providers.fileContents(layout.projectDirectory.file("README.md")).asText.map {
                val start = "<!-- Plugin description -->"
                val end = "<!-- Plugin description end -->"

            with (it.lines()) {
                if (!containsAll(listOf(start, end))) {
                    throw GradleException("Plugin description section not found in README.md:\n$start ... $end")
                }
                subList(indexOf(start) + 1, indexOf(end)).joinToString("\n").let(::markdownToHTML)
            }
        })

        val changelog = project.changelog // local variable for configuration cache compatibility
        // Get the latest available change notes from the changelog file
        changeNotes.set(properties("pluginVersion").map { pluginVersion ->
            with(changelog) {
                renderItem(
                    (getOrNull(pluginVersion) ?: getUnreleased())
                        .withHeader(false)
                        .withEmptySections(false),
//                        getOrNull(properties("pluginVersion"))
//                            ?: runCatching { getLatest() }.getOrElse { getUnreleased() },
                        Changelog.OutputType.HTML,
                    )
                }
            }
        )
    }

    runPluginVerifier {
        ideVersions.set(properties("pluginVerifierIdeVersions").map { it.split(",").map { split -> split.trim() }.toList() })
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
        certificateChain.set(environment("CERTIFICATE_CHAIN"))
        privateKey.set(environment("PRIVATE_KEY"))
        password.set(environment("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
//        dependsOn("includeVehicles")
        token.set(environment("PUBLISH_TOKEN"))
        // The pluginVersion is based on the SemVer (https://semver.org) and supports pre-release labels, like 2.1.7-alpha.3
        // Specify pre-release label to publish the plugin in a custom Release Channel automatically. Read more:
        // https://plugins.jetbrains.com/docs/intellij/deployment.html#specifying-a-release-channel
        channels.set(properties("pluginVersion").map { listOf(it.split('-').getOrElse(1) { "default" }.split('.').first()) })
    }
}
