@Suppress(
    // known false positive: https://youtrack.jetbrains.com/issue/KTIJ-19369
    "DSL_SCOPE_VIOLATION"
)
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.spotless)
}

val assetsDir = rootProject.files("assets")

sourceSets.main.configure {
    resources.srcDir(assetsDir)
}

spotless {
    isEnforceCheck = false
    kotlin {
        ktfmt("0.37")
    }
}

dependencies {
    api(project(":ldtk"))
    api(libs.artemis)
    api(libs.controllers)
    api(libs.dagger)
    kapt(libs.daggerCompiler)
    api(libs.gdx)
    api(libs.gdxAi)
    api(libs.gdxBox2d)
    api(libs.ktxActors)
    api(libs.ktxApp)
    api(libs.ktxBox2d)
    api(libs.ktxGraphics)
    api(libs.ktxMath)
    api(libs.ktxVis)
    api(libs.serializationJson)
}
