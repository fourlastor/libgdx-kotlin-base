plugins {
    idea
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
