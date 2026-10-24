// build-logic 是独立构建：它需要自己能看到版本目录（唯一版本真源在 ../gradle/libs.versions.toml）
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"
