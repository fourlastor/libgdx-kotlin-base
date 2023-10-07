import org.gradle.internal.os.OperatingSystem

@Suppress(
    // known false positive: https://youtrack.jetbrains.com/issue/KTIJ-19369
    "DSL_SCOPE_VIOLATION"
)
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.spotless)
    application
}

val gameName: String = requireNotNull(property("io.github.fourlastor.game.name") as? String)
val gameVersion = requireNotNull(property("io.github.fourlastor.game.version") as? String)
val currentOs = requireNotNull(OperatingSystem.current())

java {
    toolchain { languageVersion.set(JavaLanguageVersion.of(11)) }
}

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


@Suppress("GradlePackageUpdate") // false positive
dependencies {
    implementation(project(":core"))
    implementation(libs.gdx.backend.lwjgl3)
    runtimeOnly(nativesDesktop(libs.gdx.platform))
}

fun DependencyHandlerScope.nativesDesktop(
    provider: Provider<MinimalExternalModuleDependency>,
) = variantOf(provider) { classifier("natives-desktop") }
