// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}
buildscript {
    repositories {
        // Check that you have the following line (if not, add it)
        google()
        mavenCentral()// Google's Maven repository
    }
    dependencies {
        classpath(libs.google)
    }

}
