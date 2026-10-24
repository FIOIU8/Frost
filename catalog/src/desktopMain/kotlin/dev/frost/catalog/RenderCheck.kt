package dev.frost.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.renderComposeScene
import androidx.compose.ui.unit.Density
import dev.frost.catalog.design.FrostFontProvider
import dev.frost.catalog.design.FrostListScreen
import dev.frost.catalog.design.FrostSettingsScreen
import dev.frost.catalog.design.FrostSettingsWithDialog
import dev.frost.catalog.design.FrostWeatherScreen
import dev.frost.catalog.design.FrostWeatherSettingsScreen
import dev.frost.catalog.showcase.ShowcaseScreen
import dev.frost.catalog.showcase.showcaseSections
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import java.io.File

/**
 * 离屏渲染检查：不开窗口把界面渲成 PNG。
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

    // 1) 主题基线：同一张页面 × 四种主题模式
    themeBaselines.forEach { (name, mode) ->
        renderTo(outDir.resolve("catalog-$name.png")) {
            CatalogApp(controller = rememberCatalogController(mode))
        }
    }

    // 2) 组件基线：每个巡礼小节 × 浅/深两套
    val sections = showcaseSections()
    sections.forEach { section ->
        showcaseModes.forEach { (modeName, mode) ->
            renderTo(outDir.resolve("showcase-${section.id}-$modeName.png")) {
                ShowcaseScreen(section = section, controller = rememberCatalogController(mode))
            }
        }
    }

    // 3) Frost 视觉样张：按 22 张参考图的风格复刻，渲染尺寸与参考图一致（1240×2772 @3x）
    val frostScreens =
        listOf<Pair<String, @Composable () -> Unit>>(
            "settings" to { FrostFontProvider { FrostSettingsScreen() } },
            "settings-dialog" to { FrostFontProvider { FrostSettingsWithDialog() } },
            "list" to { FrostFontProvider { FrostListScreen() } },
            "weather" to { FrostFontProvider { FrostWeatherScreen() } },
            "weather-settings" to { FrostFontProvider { FrostWeatherSettingsScreen() } },
        )
    frostScreens.forEach { (name, content) ->
        renderTo(outDir.resolve("frost-$name.png"), width = 1240, height = 2772, content = content)
    }

    val total = themeBaselines.size + sections.size * showcaseModes.size + frostScreens.size
    println("baseline：$total 张 → ${outDir.absolutePath}")
}

private val themeBaselines =
    listOf(
        "monet-light" to ColorSchemeMode.MonetLight,
        "monet-dark" to ColorSchemeMode.MonetDark,
        "light" to ColorSchemeMode.Light,
        "dark" to ColorSchemeMode.Dark,
    )

private val showcaseModes =
    listOf(
        "light" to ColorSchemeMode.Light,
        "dark" to ColorSchemeMode.Dark,
    )

private fun renderTo(
    file: File,
    width: Int = 1080,
    height: Int = 1920,
    content: @Composable () -> Unit,
) {
    // density=3：物理像素 / 3 = 逻辑 dp，接近真实手机。按 1x 渲染会让组件小到看不清。
    val image = renderComposeScene(width = width, height = height, density = Density(3f), content = content)
    val data = image.encodeToData() ?: error("PNG 编码失败：$file")
    file.writeBytes(data.bytes)
    println("rendered: ${file.name} (${file.length()} bytes)")
}
