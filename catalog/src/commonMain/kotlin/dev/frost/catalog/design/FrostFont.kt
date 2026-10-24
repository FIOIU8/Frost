package dev.frost.catalog.design

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontFamily
import dev.frost.catalog.resources.NotoSansSC_Regular
import dev.frost.catalog.resources.Res
import org.jetbrains.compose.resources.Font

/**
 * 字体：**Noto Sans SC Regular**（OFL-1.1）。
 *
 * 为什么必须固定字体：之前离屏渲染走 Skiko 的 CJK 回退字体（Windows 上是微软雅黑），
 * 与参考图（Android 系统中文黑体）字宽不同 → 同样字号下行宽、右对齐的值都会横向错位，
 * 叠图永远对不齐。这是"看起来不像"的最后一个结构性原因。
 *
 * 只在 @Composable 里能加载，所以用 CompositionLocal 注入，[FrostText] 自动取用。
 */
val LocalFrostFontFamily = staticCompositionLocalOf<FontFamily> { FontFamily.Default }

/** 在页面根部包一层，把 Bundle 里的字体注入下去。 */
@Composable
fun FrostFontProvider(content: @Composable () -> Unit) {
    val family = FontFamily(listOf(Font(Res.font.NotoSansSC_Regular)))
    CompositionLocalProvider(LocalFrostFontFamily provides family, content = content)
}
