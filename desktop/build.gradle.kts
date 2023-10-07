plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spotless)
    application
}

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(11)) }
}

spotless {
    isEnforceCheck = false
    kotlin {
        ktlint("1.0.0")
    }
}

group = "io.github.fourlastor"

application {
    mainClass.set("io.github.fourlastor.game.DesktopLauncherKt")
}


@Suppress("GradlePackageUpdate") // false positive
dependencies {
    implementation(project(":core"))
    implementation(libs.gdx.backend.lwjgl3)
    runtimeOnly(nativesDesktop(libs.gdx.platform))
}

fun DependencyHandlerScope.nativesDesktop(
    provider: Provider<MinimalExternalModuleDependency>,
) = variantOf(provider) { classifier("natives-desktop") }
