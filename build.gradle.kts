plugins {
    idea
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://teavm.org/maven/repository/")
    }
}
