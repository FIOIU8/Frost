plugins {
    `kotlin-dsl`
}

dependencies {
    // Spotless 插件实现：版本来自 gradle/libs.versions.toml，不在本文件硬编码
    implementation(libs.spotless.plugin.gradle)
}

// 约定插件为预编译脚本插件：src/main/kotlin/<id>.gradle.kts 会以文件名自动注册为插件 id。
// 现有：
//   frost.quality   代码格式与静态检查（spotless）
//   frost.toolchain JVM 工具链对齐（JDK 21）
// P1 追加：frost.kmp.library / frost.compose；P7 追加：frost.publish
