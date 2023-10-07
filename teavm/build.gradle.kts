
@Suppress(
  // known false positive: https://youtrack.jetbrains.com/issue/KTIJ-19369
  "DSL_SCOPE_VIOLATION"
)
plugins {
  java
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.gretty)
}

gretty {
  contextPath = "/"
  extraResourceBase("build/dist/webapp")
}

val mainClassName = "io.github.fourlastor.game.teavm.TeaVMBuilderKt"
java {
  toolchain { languageVersion.set(JavaLanguageVersion.of(11)) }
}

dependencies {
  implementation(libs.gdx.backend.teavm)
  implementation(libs.google.gson)
  implementation(project(":core"))
}

val buildJavaScript = tasks.register<JavaExec>("buildJavaScript") {
  dependsOn(tasks.classes)
  description = "Transpile bytecode to JavaScript via TeaVM"
  mainClass.set(mainClassName)
  setClasspath(sourceSets.main.get().runtimeClasspath)
}

tasks.build.configure { dependsOn(buildJavaScript) }
val run = tasks.register("run") {
  description = "Run the JavaScript application hosted via a local Jetty server at http://localhost:8080/"
  dependsOn(buildJavaScript, ":teavm:jettyRun")
}
