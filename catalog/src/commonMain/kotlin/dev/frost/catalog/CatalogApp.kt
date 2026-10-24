package dev.frost.catalog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

/**
 * P1 的第二个验证点：`MiuixTheme` + `ThemeController(MonetSystem, keyColor)` 能否编译并渲染。
 *
 * 刻意保持最小：一个主题包一个 Text。
 * 组件巡礼页、浅/深切换、截图基线放在后续步骤。
 *
 * API 依据（读 `miuix-ui-android-0.9.4-sources.jar` 确认，非猜）：
 * - `MiuixTheme(controller: ThemeController, textStyles: TextStyles, content)`
 * - `ThemeController(colorSchemeMode, lightColors, darkColors, keyColor, colorSpec, paletteStyle, isDark)`
 * - `top.yukonga.miuix.kmp.basic.Text(text: String, modifier, ...)`
 */
@Composable
fun CatalogApp() {
    // keyColor 是 catalog 的演示用种子色（不是设计令牌，P3 起由 FrostTheme 统一管理）
    val controller =
        remember {
            ThemeController(
                colorSchemeMode = ColorSchemeMode.MonetSystem,
                keyColor = Color(0xFF4C662B),
            )
        }

    MiuixTheme(controller = controller) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text("Frost catalog · P1 · MiuixTheme OK")
        }
    }
}
