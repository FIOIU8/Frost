pluginManagement {
    // 约定插件源码在 build-logic（独立构建），通过 includeBuild 参与编译
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // 仓库只允许在此声明，禁止模块级 repositories（防止各模块各写一套）
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "frost"

// P0：零模块骨架（已完成，Gate G0 绿）
// P1：加入 :catalog 演示模块，用于验证 Miuix 依赖能否真实解析
include(":catalog")
