val projectVersion: String by project
val jvmToolchainVersion: String by project

plugins {
    kotlin("jvm") version "2.1.21" apply false
}

version = projectVersion

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.apply("org.jetbrains.kotlin.jvm")

    extensions.configure<JavaPluginExtension>("java") {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(jvmToolchainVersion.toInt()))
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = jvmToolchainVersion
        }
    }
}
