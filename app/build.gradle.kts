import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.google.ksp)
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    with(project(":ksp:processor")) {
        implementation(this)
        ksp(this)
    }
}

val generateCodeTask = tasks.register("generateCodeTask") {
    val inputFile = tasks.named("kspKotlin").map { kspTask ->
        RegularFile {
            kspTask.outputs.files.asFileTree.matching { include("**/Greeting.txt") }.singleFile
        }
    }
    inputs.file(inputFile)

    val outputDir = project.layout.buildDirectory.dir("generated/gradletask")
    outputs.dir(outputDir)

    doLast {
        val greeting = inputFile.get().asFile.readText()
        val file = outputDir.get().file("org/example/HelloFromGradle.kt").asFile
        file.parentFile.mkdirs()
        file.writeText(
            """
            package org.example

            class HelloFromGradle {
                fun sayHello() {
                    println("Hello, $greeting, from Gradle!")
                }
            }
            """.trimIndent()
        )
    }
}

kotlin {
    sourceSets.main {
        // To fix (1): Comment out this line
        kotlin.srcDir(generateCodeTask)
    }
}

// To fix (2): Uncomment out this block
//tasks.named<KotlinJvmCompile>("compileKotlin") {
//    source(generateCodeTask)
//}

application {
    mainClass.set("MainKt")
}
