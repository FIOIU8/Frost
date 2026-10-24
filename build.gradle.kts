// 根工程：不产出任何构件，只负责把仓库级约定插件挂上。
// 模块级构建脚本（P1 起）从 frost.kmp.library / frost.compose 取约定。
plugins {
    id("frost.quality")
    id("frost.toolchain")
}

// clean 任务由 base 插件（经 spotless 传递）提供，这里只做补充配置，
// 不能重新 register，否则报 "task with that name already exists"。
tasks.named<Delete>("clean") {
    group = "build"
    description = "清理根工程构建产物"
    delete(rootProject.layout.buildDirectory)
}
