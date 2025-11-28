// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    // AÑADIMOS EL PLUGIN DE COMPOSE AQUÍ
    alias(libs.plugins.kotlin.compose.compiler) apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false
}
