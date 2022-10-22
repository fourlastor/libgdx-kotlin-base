@file:Suppress("UnstableApiUsage")

include("desktop")
include(":core")
include("ldtk")

dependencyResolutionManagement {
    versionCatalogs { create("libs") { from(files("libs.versions.toml")) } }
}
