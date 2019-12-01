import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.frontend.KotlinFrontendExtension
import org.jetbrains.kotlin.gradle.frontend.npm.NpmExtension
import org.jetbrains.kotlin.gradle.frontend.webpack.WebPackExtension
import org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile

group = "co.makery.todomvc.frontend"

plugins {
    id("kotlin2js") version kotlinVersion
    id("kotlin-dce-js") version kotlinVersion
    id("org.jetbrains.kotlin.frontend") version frontendPluginVersion
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-js:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-test-js:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:$kotlinxHtmlVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutinesVersion")

    implementation(project(":common"))
}

kotlinFrontend {
    sourceMaps = true

    npm {
        dependency("css-loader")
        dependency("style-loader")
        devDependency("karma")
    }

    bundle<WebPackExtension>("webpack", {
        (this as WebPackExtension).apply {
            port = 8080
            publicPath = "/frontend/"
            proxyUrl = "http://localhost:9000"
        }
    })

    define("PRODUCTION", true)
}

tasks {
    "compileKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            metaInfo = true
            outputFile = "${project.buildDir.path}/js/${project.name}.js"
            sourceMap = true
            sourceMapEmbedSources = "always"
            moduleKind = "commonjs"
            main = "call"
        }
    }

    "compileTestKotlin2Js"(Kotlin2JsCompile::class) {
        kotlinOptions {
            metaInfo = true
            outputFile = "${project.buildDir.path}/js-tests/${project.name}-tests.js"
            sourceMap = true
            moduleKind = "commonjs"
            main = "call"
        }
    }


    val assembleWeb by register<Copy>("assembleWeb") {

        from("${buildDir.path}/resources/")
        into("${buildDir.path}/kotlin-js-min/")

        dependsOn("classes")
    }

}

afterEvaluate {
    tasks["webpack-bundle"].dependsOn("assembleWeb")
    tasks["webpack-bundle"].dependsOn("runDceKotlinJs")
    tasks["webpack-run"].dependsOn("runDceKotlinJs")
}
