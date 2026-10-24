package dev.frost.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

/** catalog 的演示种子色。P3 起由 `FrostTheme` 统一管理，这里只是占位。 */
private val CatalogKeyColor = Color(0xFF4C662B)

/**
 * 记住一个 [ThemeController]。抽出来是为了让入口与离屏渲染能共用同一份构造逻辑，
 * 只把 [mode] 换成 Light / Dark 就能出两套基线图。
 */
@Composable
fun rememberCatalogController(mode: ColorSchemeMode = ColorSchemeMode.MonetSystem): ThemeController =
    remember(mode) {
        ThemeController(
            colorSchemeMode = mode,
            keyColor = CatalogKeyColor,
        )
    }

/**
 * P1 的验证页面：`MiuixTheme` + `ThemeController` + 一个基础组件。
 *
 * 刻意保持最小——组件巡礼页在 `inventory` 就绪后再铺开。
 *
 * API 依据（读 `miuix-ui-android-0.9.4-sources.jar` 确认，非猜）：
 * - `MiuixTheme(controller: ThemeController, textStyles: TextStyles, content)`
 * - `ThemeController(colorSchemeMode, lightColors, darkColors, keyColor, colorSpec, paletteStyle, isDark)`
 * - `top.yukonga.miuix.kmp.basic.Text(text: String, modifier, ...)`
 */
@Composable
fun CatalogApp(controller: ThemeController = rememberCatalogController()) {
    MiuixTheme(controller = controller) {
        // 必须自己铺背景：MiuixTheme 只提供 CompositionLocal，不绘制背景。
        // 少了这层，深色模式下画面仍是白底 + 浅色文字 → 文字"消失"（本目录的
        // monet-dark 基线最初就踩了这个坑）。
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MiuixTheme.colorScheme.background,
            shadowElevation = 0.dp,
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text("Frost catalog · P1 · MiuixTheme OK")
            }
        }
    }
}
