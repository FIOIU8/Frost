package dev.frost.catalog.showcase

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.frost.catalog.rememberCatalogController
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

/**
 * 单节巡礼页：主题 + 背景 + 标题 + 该节内容。
 *
 * 抽成独立 composable 的好处：离屏渲染（基线）与将来的 App 内页面共用同一套布局，
 * 不会出现"截图和真机长得不一样"的经典问题。
 */
@Composable
fun ShowcaseScreen(
    section: ShowcaseSection,
    controller: ThemeController = rememberCatalogController(),
) {
    MiuixTheme(controller = controller) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MiuixTheme.colorScheme.background,
            shadowElevation = 0.dp,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(24.dp),
            ) {
                Text(section.title)
                Spacer(Modifier.height(20.dp))
                // content 是 ColumnScope 的扩展函数类型，这里把当前 ColumnScope 传进去
                section.content.invoke(this)
            }
        }
    }
}
