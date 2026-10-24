
// :catalog —— 演示与验证模块（P1）
//
// P1-1：验证 Miuix 0.9.4 子模块坐标可解析（见 log 2026-10-24 15:25）
// P1-2：接通 Compose，跑通 MiuixTheme + ThemeController
// P1-3（本次）：补 Desktop 入口，并把 run 任务钉到工具链 JDK 21
//
// 注意：AGP >= 9.0 不允许 com.android.library 与 KMP 插件共存，
// KMP 模块必须用 com.android.kotlin.multiplatform.library + kotlin { android {} } 新 DSL。

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

    // AGP-KMP 新 DSL：namespace / compileSdk / minSdk 都在这里声明
    android {
        namespace = "dev.frost.catalog"
        compileSdk = compileSdkVersion
        minSdk = minSdkVersion
    }

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            // 聚合构件 top.yukonga.miuix.kmp:miuix 只到 0.8.8，必须逐个子模块引用
            implementation(libs.bundles.miuix)

            // Compose 运行库版本由 libs.versions.toml 的 composeMultiplatform 决定（1.12.2），
            // 编译器插件版本跟随 Kotlin（2.4.21），两者由 CMP/Kotlin 插件各自保证
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
        }

        // Desktop 入口需要 Skiko 运行时（按当前 OS 解析）
        getByName("desktopMain").dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

compose.desktop {
    application {
        mainClass = "dev.frost.catalog.MainKt"
    }
}

// Desktop 运行注意：本工程编译目标是 JDK 21（toolchain），但 Gradle 自身可以跑在
// JDK 17 上。若本机 JAVA_HOME=17，`:catalog:run` 会用 17 去启动 21 的 class，
// 报 UnsupportedClassVersionError。两种解法，任选其一：
//   1) 运行前把 Gradle 的 JVM 换成 21（推荐，最简单）：
//        Windows: set JAVA_HOME=%USERPROFILE%\.jdks\jdk-21.0.12.1+1 && gradlew :catalog:run
//        bash   : JAVA_HOME=~/.jdks/jdk-21.0.12.1+1 ./gradlew :catalog:run
//   2) 在本机全局 gradle.properties 里设 org.gradle.java.home 指向 JDK 21
// 之前试过在 JavaExec 上强制 javaLauncher，但 compose 插件会同时设 executable，
// Gradle 9 报 "Toolchain from `executable` does not match ... `javaLauncher`"，
// 清空 executable 又会因属性只读失败，故不再在构建脚本里对抗它。
