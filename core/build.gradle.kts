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

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(11)) }
}

spotless {
    isEnforceCheck = false
    kotlin {
        ktfmt("0.37")
    }
}

dependencies {
    implementation(libs.dagger.core)
    kapt(libs.dagger.compiler)
    api(libs.gdx.core)
    implementation(libs.gdx.ai)
    implementation(libs.ktx.actors)
    api(libs.ktx.app)
    implementation(libs.ktx.graphics)
    implementation(libs.ktx.math)
    implementation(libs.ktx.vis)
}
