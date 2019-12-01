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

kotlin {
  experimental.coroutines = Coroutines.ENABLE
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(kotlin("stdlib", kotlinVersion))
  implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinxHtmlVersion")
  implementation("io.ktor:ktor-server-netty:$ktorVersion")
  implementation("io.ktor:ktor-html-builder:$ktorVersion")
  implementation("ch.qos.logback:logback-classic:$logbackVersion")
  implementation(project(":common"))
  implementation("azadev.kotlin:aza-kotlin-css:$cssVersion")
}
