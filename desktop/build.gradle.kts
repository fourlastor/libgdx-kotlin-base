import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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

val gameName: String = requireNotNull(property("io.github.fourlastor.game.name") as? String)
val gameVersion = requireNotNull(property("io.github.fourlastor.game.version") as? String)


spotless {
    isEnforceCheck = false
    kotlin {
        ktfmt("0.37")
    }
}

group = "io.github.fourlastor"
version = gameVersion

application {
    mainClass.set("io.github.fourlastor.game.DesktopLauncherKt")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set(gameName)
    }
}

@Suppress("GradlePackageUpdate") // false positive
dependencies {
    api(project(":core"))
    api("com.badlogicgames.gdx:gdx-platform:${libs.versions.gdx.get()}:natives-desktop")
    api("com.badlogicgames.gdx:gdx-box2d-platform:${libs.versions.gdx.get()}:natives-desktop")
    api(libs.gdxBackendLwjgl3)
    api(libs.controllersDesktop)
}
