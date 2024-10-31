@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("plugin.serialization") version "2.0.20"
}

android {
    namespace = "com.example.verygoodgraphics.android.demo"
    ndkVersion = "27.0.12077973"

    defaultConfig {
        minSdk = 26
        compileSdk = 34
        compileSdkVersion = "android-34"

    }

    // for jni debug
    packagingOptions.jniLibs.useLegacyPackaging = true
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.annotation)
    implementation(projects.container)
    implementation(libs.kotlinx.serialization.json)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
