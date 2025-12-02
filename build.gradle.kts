// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.3.1")
        classpath("com.google.gms:google-services:4.4.1") // Firebase plugin
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.52")
    }
}