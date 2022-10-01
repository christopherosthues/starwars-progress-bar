plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

// Set the JVM language level used to compile sources and generate files - Java 17 is required since 2022.2
kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("com.google.code.gson:gson:2.9.1")
}
