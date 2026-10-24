// :catalog-android —— catalog 的 Android 壳（P1 双入口之一）
//
// 为什么单独开模块而不是塞进 :catalog：
// AGP >= 9 的 com.android.application 不能与 org.jetbrains.kotlin.multiplatform 共存，
// 而 :catalog 是 KMP 模块（要同时供 Desktop）。所以壳只负责 Activity + 主题，
// 真正的 UI 全部来自 :catalog 的 androidTarget 产物。

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

val jvmToolchainVersion =
    libs.versions.jvm.toolchain
        .get()
        .toInt()
val compileSdkVersion =
    libs.versions.android.compileSdk
        .get()
        .toInt()
val minSdkVersion =
    libs.versions.android.minSdk
        .get()
        .toInt()
val targetSdkVersion =
    libs.versions.android.targetSdk
        .get()
        .toInt()

android {
    namespace = "dev.frost.catalog.android"
    compileSdk = compileSdkVersion

    defaultConfig {
        applicationId = "dev.frost.catalog"
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
        versionCode = 1
        versionName = "0.0.1"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(project(":catalog"))
    implementation(libs.androidx.activity.compose)
}
