pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "promptlin"

include("promptlin-core")
include("promptlin-console")
include("promptlin-discord")
include("promptlin-discord-jda")