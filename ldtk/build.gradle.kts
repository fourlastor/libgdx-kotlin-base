import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.21"
    id("com.diffplug.spotless")
}

spotless {
    isEnforceCheck = false
    kotlin {
        ktfmt("0.37")
    }
}

group = "io.github.fourlastor"
version = "1.0"

@Suppress("UnstableApiUsage")
dependencies {
    api(libs.gdx)
    implementation(libs.serializationJson)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
