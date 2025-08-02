import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val projectVersion: String by project
val jvmToolchainVersion: String by project

plugins {
    kotlin("jvm") version "2.1.21" apply false
}

allprojects {
    group = "me.centauri07.promptlin"
    version = projectVersion

    repositories {
        mavenCentral()
    }
}

subprojects {
    plugins.apply("org.jetbrains.kotlin.jvm")
    plugins.apply("maven-publish")

    extensions.configure<JavaPluginExtension>("java") {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(jvmToolchainVersion.toInt()))
        }

        withSourcesJar()
        withJavadocJar()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget = JvmTarget.fromTarget(jvmToolchainVersion)
        }
    }

    extensions.configure<PublishingExtension>("publishing") {
        publications {
            create<MavenPublication>("maven") {
                from(components["java"])
            }
        }
    }
}
