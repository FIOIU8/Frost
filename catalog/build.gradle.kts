// :catalog —— 演示与验证模块（P1）
//
// P1-1 已验证：Miuix 0.9.4 子模块坐标能解析并参与编译（见 log 2026-10-24 15:25）。
// P1-2（本次）：加 Compose 插件，只跑通 MiuixTheme + ThemeController + 一个基础组件，
// 仍然不做页面，避免 UI 问题与构建问题混在一起。
//
// 注意：AGP >= 9.0 不允许 com.android.library 与 KMP 插件共存，
// KMP 模块必须用 com.android.kotlin.multiplatform.library + 新的 androidLibrary {} DSL。

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

// 先取成局部变量：ktlint 会把超长链式调用强制拆行，可读性反而更差
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

kotlin {
    jvmToolchain(jvmToolchainVersion)

    // AGP-KMP 新 DSL：同时声明 namespace / compileSdk / minSdk，无需再写 android {}
    androidLibrary {
        namespace = "dev.frost.catalog"
        compileSdk = compileSdkVersion
        minSdk = minSdkVersion
    }

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            // 聚合构件 top.yukonga.miuix.kmp:miuix 只到 0.8.8，必须逐个子模块引用
            implementation(libs.bundles.miuix)

            // Compose 编译器插件版本跟随 Kotlin（见 libs.versions.toml plugins 段），
            // 运行库版本由 org.jetbrains.compose 插件决定 = 1.12.2
            implementation(compose.runtime)
            implementation(compose.foundation)
        }
    }
}
