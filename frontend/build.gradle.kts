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

//    "processResources"(Copy::class) {
//        from(file(buildDir.path + "/resources"))
//        into(file(buildDir.path + "/kotlin-js-min"))
//    }

    val assembleWeb by register<Copy>("assembleWeb") {
//        group = "build"
//        configurations.compile.files.onEach { file ->
//            from(zipTree(file.absolutePath), {
//                includeEmptyDirs = false
//                include { fileTreeElement ->
//                    val path = fileTreeElement.path
//                            path.endsWith(".js") && (path.startsWith("META-INF/resources/") ||
//                            !path.startsWith("META-INF/"))
//                }
//            })
//        }

        from("${buildDir.path}/resources/")
        into("${buildDir.path}/kotlin-js-min/")

        dependsOn("classes")
    }

}

afterEvaluate {
//    tasks["assemble"].dependsOn("processResources")
//
    tasks["webpack-bundle"].dependsOn("assembleWeb")
    tasks["webpack-bundle"].dependsOn("runDceKotlinJs")
    tasks["webpack-run"].dependsOn("runDceKotlinJs")
}
