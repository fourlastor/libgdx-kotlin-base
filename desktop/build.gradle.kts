import org.gradle.internal.os.OperatingSystem

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
val currentOs = requireNotNull(OperatingSystem.current())


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

shadow {
    this.applicationDistribution
}

@Suppress("GradlePackageUpdate") // false positive
dependencies {
    api(project(":core"))
    api("com.badlogicgames.gdx:gdx-platform:${libs.versions.gdx.get()}:natives-desktop")
    api("com.badlogicgames.gdx:gdx-box2d-platform:${libs.versions.gdx.get()}:natives-desktop")
    api(libs.gdxBackendLwjgl3)
    api(libs.controllersDesktop)
}

runtime {
    options.set(listOf(
        "--strip-debug",
        "--compress", "2",
        "--no-header-files",
        "--no-man-pages",
    ))

    if (currentOs.isWindows) {
        options.set(options.get() + "--strip-native-commands")
    }
    
    modules.set(listOf(
        "java.base" ,
        "java.desktop",
        "java.logging",
        "jdk.incubator.foreign",
        "jdk.unsupported",
    ))

    launcher {
        unixScriptTemplate = rootProject.file("packaging/in/scripts/unixrun.mustache")
    }

    if (currentOs.isLinux) {
        targetPlatform("linux-x64") {
            setJdkHome(jdkDownload("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.4.1%2B1/OpenJDK17U-jdk_x64_linux_hotspot_17.0.4.1_1.tar.gz"))
        }
        targetPlatform("linux-aarch64") {
            setJdkHome(jdkDownload("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.4.1%2B1/OpenJDK17U-jdk_aarch64_linux_hotspot_17.0.4.1_1.tar.gz"))
        }
        targetPlatform("mac-x64") {
            setJdkHome(jdkDownload("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.4.1%2B1/OpenJDK17U-jdk_x64_mac_hotspot_17.0.4.1_1.tar.gz"))
        }
        targetPlatform("mac-aarch64") {
            setJdkHome(jdkDownload("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.4.1%2B1/OpenJDK17U-jdk_aarch64_mac_hotspot_17.0.4.1_1.tar.gz"))
        }
    }

    jpackage {
        jpackageHome = requireNotNull(property("io.github.fourlastor.game.jpackage") as? String)
        skipInstaller = true
        imageName = gameName
        if (currentOs.isWindows) {
//            imageOptions.addAll(listOf("--icon", rootProject.file("packaging/in/icons/icon.ico").absolutePath))
        }
    }
}
