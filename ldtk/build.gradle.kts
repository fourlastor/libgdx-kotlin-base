import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.7.20"
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

dependencies {
    api(libs.gdx)
    api(libs.dagger)
    implementation(libs.serializationJson)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
