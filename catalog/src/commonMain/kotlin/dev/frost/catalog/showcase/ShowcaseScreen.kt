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
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

/**
 * 单节巡礼页：主题 + 背景 + 标题 + 该节内容。
 *
 * 抽成独立 composable 的好处：离屏渲染（基线）与将来的 App 内页面共用同一套布局，
 * 不会出现"截图和真机长得不一样"的经典问题。
 *
 * 外壳必须是 Miuix [Scaffold] 而不是裸 Surface：Overlay* 浮层把 DialogState 注册进
 * LocalDialogStates，由 Scaffold 消费渲染（实读 MiuixPopupUtils/Scaffold 源码确认）。
 * 没有它，浮层静默不可见——离屏基线里 dialog 一节全空白就是踩的这条。
 */
@Composable
fun ShowcaseScreen(
    section: ShowcaseSection,
    controller: ThemeController = rememberCatalogController(),
) {
    MiuixTheme(controller = controller) {
        Scaffold(
            containerColor = MiuixTheme.colorScheme.background,
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
