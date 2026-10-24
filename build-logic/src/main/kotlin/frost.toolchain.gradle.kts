import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion

// JVM 工具链对齐约定。
//
// 设计取舍：本插件**不应用**任何语言插件（Kotlin / Android / Java），
// 只做两件事：
//   1. 把 `frost.jvmToolchain`（gradle.properties，默认 21）暴露为 extra property
//      `frostJvmToolchain`，供模块或后续约定插件读取；
//   2. 若当前工程已应用 java 插件，则配置其 Java toolchain。
//
// 这样做的原因：Android / KMP 工程的 toolchain 必须写在各家 DSL 里
// （`kotlin { jvmToolchain(...) }`、`android { compileOptions }`），
// 强行在通用插件里猜扩展类型会在不同模块类型上炸。
// P1 的 frost.kmp.library 会读取 frostJvmToolchain 统一设置，届时本插件成为其内部依赖。

val frostJvmToolchain: Int =
    (project.findProperty("frost.jvmToolchain") as String?)?.toIntOrNull() ?: 21

extensions.extraProperties["frostJvmToolchain"] = frostJvmToolchain

pluginManager.withPlugin("java") {
    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(frostJvmToolchain))
    }
}
