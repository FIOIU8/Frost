import com.diffplug.gradle.spotless.SpotlessExtension

// 代码格式与静态检查约定。
// 只在根工程应用一次：targets 使用 **/ 递归，天然覆盖所有子模块源码，
// 避免每个模块各配一套导致 spotlessCheck 重复扫描。

plugins {
    id("com.diffplug.spotless")
}

extensions.configure<SpotlessExtension> {
    // Kotlin 源码
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**", "**/.gradle/**", "docs/**")
        ktlint()
    }

    // Gradle 构建脚本（含 build-logic 的预编译脚本）
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**", "**/.gradle/**")
        ktlint()
    }

    // 配置与文档类文本
    format("misc") {
        target("**/*.md", "**/*.yml", "**/*.yaml", "**/*.toml", "**/*.properties")
        targetExclude("**/build/**", "**/.gradle/**", "docs/**")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

// 注：ktlint 未指定版本，使用 Spotless 8.x 内置默认版本；
// 等 JDK 21 就绪、首次 spotlessCheck 跑通后再显式钉死，避免现在写一个未经构建验证的版本号。
// 注：LICENSE 不在任何 target 内，绝不允许格式化（法律文本必须一字不改）。
