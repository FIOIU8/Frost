// :catalog —— 演示与验证模块（P1）
//
// 本阶段目标只有一个：证明 Miuix 的子模块坐标能在本机真实解析并参与编译。
// 因此这里刻意**不**应用 Compose 插件、不写 @Composable，避免把
// "依赖能不能拉到" 与 "Compose 编译器配没配对" 两类问题混在一起排查。
//
// 注意：AGP >= 9.0 不允许 com.android.library 与 KMP 插件共存，
// KMP 模块必须用 com.android.kotlin.multiplatform.library + 新的 androidLibrary {} DSL。

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kmp.library)
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
        }
    }
}
