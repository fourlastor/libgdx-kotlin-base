@Suppress(
    // known false positive: https://youtrack.jetbrains.com/issue/KTIJ-19369
    "DSL_SCOPE_VIOLATION"
)
plugins {
    id("application")
    alias(libs.plugins.beryx.runtime)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
    alias(libs.plugins.spotless)
    application
}

spotless {
    isEnforceCheck = false
    kotlin {
        ktfmt("0.37")
    }
}

group = "io.github.fourlastor"
version = "1.0"

application {
    mainClass.set("io.github.fourlastor.game.DesktopLauncherKt")
}

dependencies {
    api(project(":core"))
    api("com.badlogicgames.gdx:gdx-platform:${libs.versions.gdx.get()}:natives-desktop")
    api("com.badlogicgames.gdx:gdx-box2d-platform:${libs.versions.gdx.get()}:natives-desktop")
    api(libs.gdxBackendLwjgl3)
    api(libs.controllersDesktop)
}
