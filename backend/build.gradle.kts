import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "co.makery.todomvc.backend"

plugins {
  application
  kotlin("jvm") version kotlinVersion
}

application {
  mainClassName = "io.ktor.server.netty.DevelopmentEngine"
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(kotlin("stdlib-jdk8", kotlinVersion))
  implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
  implementation("io.ktor:ktor-server-netty:$ktorVersion")
  implementation("io.ktor:ktor-html-builder:$ktorVersion")
  implementation("ch.qos.logback:logback-classic:$logbackVersion")
  implementation("azadev.kotlin:aza-kotlin-css:$cssVersion")
  implementation(project(":common"))
}
