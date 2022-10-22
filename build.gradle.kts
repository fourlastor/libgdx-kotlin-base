@Suppress(
    // known false positive: https://youtrack.jetbrains.com/issue/KTIJ-19369
    "DSL_SCOPE_VIOLATION"
)
plugins {
    idea
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.spotless) apply false
}

allprojects {
    version = "1.0"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
