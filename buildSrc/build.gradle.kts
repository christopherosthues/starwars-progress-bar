plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

// Set the JVM language level used to compile sources and generate files - Java 11 is required since 2020.3
kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("com.google.code.gson:gson:2.9.0")
}
