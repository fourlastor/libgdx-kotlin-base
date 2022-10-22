plugins {
    idea
    kotlin("jvm") version "1.7.20"
    id("com.diffplug.spotless") version "6.5.2"
}

allprojects {
    version = "1.0"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
