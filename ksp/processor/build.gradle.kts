plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "org.example.ksp"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(libs.ksp.processing)
}
