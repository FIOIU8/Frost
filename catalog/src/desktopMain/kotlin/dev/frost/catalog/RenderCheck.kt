package dev.frost.catalog

import androidx.compose.ui.renderComposeScene
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import java.io.File

/**
 * 离屏渲染检查：不开窗口，把 [CatalogApp] 渲成 PNG。
 *
 * 为什么要有它：
 * - 沙箱/CI 里没有可交互的桌面会话，`./gradlew :catalog:run` 无法证明"渲染不崩"；
 * - 离屏渲染能在无窗口环境跑通整条 Compose + Skiko 渲染管线，失败会直接抛异常；
 * - 产出的 PNG 就是 P1 的截图基线，也是 P6 截图测试（Roborazzi/Paparazzi 之前的最轻方案）的地基。
 *
 * 用法：`./gradlew :catalog:renderBaseline`
 */
fun main(args: Array<String>) {
    val outDir = File(args.firstOrNull() ?: "build/baseline").apply { mkdirs() }

    val targets =
        listOf(
            "monet-light" to ColorSchemeMode.MonetLight,
            "monet-dark" to ColorSchemeMode.MonetDark,
            "light" to ColorSchemeMode.Light,
            "dark" to ColorSchemeMode.Dark,
        )

    targets.forEach { (name, mode) ->
        val file = outDir.resolve("catalog-$name.png")
        renderTo(file, mode)
    }

    println("baseline 输出目录：${outDir.absolutePath}")
}

private fun renderTo(
    file: File,
    mode: ColorSchemeMode,
) {
    val image =
        renderComposeScene(width = 1080, height = 1920) {
            CatalogApp(controller = rememberCatalogController(mode))
        }
    val data = image.encodeToData() ?: error("PNG 编码失败：$file")
    file.writeBytes(data.bytes)
    println("rendered: ${file.name} (${image.width}x${image.height}, ${file.length()} bytes)")
}
