import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

val jvmPluginId = libs.plugins.kotlin.jvm.get().pluginId
subprojects {
    repositories {
        mavenCentral()
    }

    plugins.withId(jvmPluginId) {
        configure<KotlinJvmProjectExtension> {
            jvmToolchain(16)
        }
    }
}
