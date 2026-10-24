import com.diffplug.gradle.spotless.SpotlessExtension

// 代码格式与静态检查约定。
// 只在根工程应用一次：targets 使用 **/ 递归，天然覆盖所有子模块源码，
// 避免每个模块各配一套导致 spotlessCheck 重复扫描。

plugins {
    id("com.diffplug.spotless")
}

// 排除项分两类：
//   generated —— 构建产物与工具内部状态（必须排除，否则会被"格式化"，且每次构建内容都变）
//   localOnly —— 只在本机存在、不入库的目录（docs/ 计划书、.workbuddy/ 等 AI 目录）
//               .gitignore 只管 git，管不到 spotless，这里必须显式排除
val generated = listOf("**/build/**", "**/.gradle/**", "**/.git/**")
val localOnly = listOf("docs/**", "**/.workbuddy/**")
val excluded = (generated + localOnly).toTypedArray()

extensions.configure<SpotlessExtension> {
    // Kotlin 源码
    kotlin {
        target("**/*.kt")
        targetExclude(*excluded)
        ktlint()
    }

    // Gradle 构建脚本（含 build-logic 的预编译脚本）
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude(*excluded)
        ktlint()
    }

    // 配置与文档类文本
    format("misc") {
        target("**/*.md", "**/*.yml", "**/*.yaml", "**/*.toml", "**/*.properties")
        targetExclude(*excluded)
        trimTrailingWhitespace()
        endWithNewline()
    }
}

// 注：ktlint 未指定版本，使用 Spotless 8.x 内置默认版本；
// 等 JDK 21 就绪、首次 spotlessCheck 跑通后再显式钉死，避免现在写一个未经构建验证的版本号。
// 注：LICENSE 不在任何 target 内，绝不允许格式化（法律文本必须一字不改）。
