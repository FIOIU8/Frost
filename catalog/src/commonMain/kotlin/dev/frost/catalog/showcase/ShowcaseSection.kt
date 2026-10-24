package dev.frost.catalog.showcase

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Checkbox
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.InfiniteProgressIndicator
import top.yukonga.miuix.kmp.basic.LinearProgressIndicator
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarItem
import top.yukonga.miuix.kmp.basic.NavigationRail
import top.yukonga.miuix.kmp.basic.NavigationRailItem
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.TabRowWithContour
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.GridView
import top.yukonga.miuix.kmp.icon.extended.Home
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.window.WindowBottomSheet
import top.yukonga.miuix.kmp.window.WindowDialog

/**
 * 巡礼页的一个小节：一个组件（或一组同族组件）配一个标题。
 *
 * `@Composable ColumnScope.()` 允许小节自己用 Column 的布局作用域（如 `weight`），
 * 但多数小节直接用 Column 堆即可。
 */
data class ShowcaseSection(
    val id: String,
    val title: String,
    val content: @Composable ColumnScope.() -> Unit,
)

/**
 * 组件巡礼小节的清单。顺序按 P1 计划的分组：容器 → 交互 → 输入 → 选择 → 浮层 → 导航。
 *
 * 注意：这里只摆"能一眼看出视觉差异"的最小用法，不做交互状态演示
 * （基线是静态截图，交互态交给以后的 UI 测试）。
 *
 * [windowOpen] 仅供 `:catalog:renderBaselineWindowed`（真实窗口截图）使用：
 * `Window*` 系列浮层走平台 Dialog（独立窗口），离屏渲染捕获不到，所以离屏基线里
 * 它们必须是关闭态（false），否则无头 CI 会直接 HeadlessException。
 */
fun showcaseSections(windowOpen: Boolean = false): List<ShowcaseSection> =
    listOf(
        ShowcaseSection("surface", "Surface") {
            Surface {
                Text("Surface 容器", modifier = Modifier.padding(16.dp))
            }
        },
        ShowcaseSection("card", "Card") {
            Card {
                Text("Card 内容")
                Text("第二行用于观察行距")
            }
        },
        ShowcaseSection("button", "Button") {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {}) { Text("默认") }
                Button(onClick = {}, enabled = false) { Text("禁用") }
            }
        },
        ShowcaseSection("text-button", "TextButton") {
            TextButton(text = "文字按钮", onClick = {})
        },
        ShowcaseSection("icon-button", "IconButton") {
            IconButton(onClick = {}) { Text("★") }
        },
        ShowcaseSection("switch", "Switch") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Switch(checked = true, onCheckedChange = {})
                Switch(checked = false, onCheckedChange = {})
            }
        },
        ShowcaseSection("checkbox", "Checkbox") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Checkbox(state = ToggleableState.On, onClick = {})
                Checkbox(state = ToggleableState.Off, onClick = {})
            }
        },
        ShowcaseSection("radio-button", "RadioButton") {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                RadioButton(selected = true, onClick = {})
                RadioButton(selected = false, onClick = {})
            }
        },
        ShowcaseSection("slider", "Slider") {
            Column {
                Slider(value = 0.35f, onValueChange = {})
                Spacer(Modifier.height(8.dp))
                Slider(value = 0.75f, onValueChange = {})
            }
        },
        ShowcaseSection("text-field", "TextField") {
            TextField(state = rememberTextFieldState("Frost"), label = "标签")
        },
        ShowcaseSection("small-title", "SmallTitle") {
            SmallTitle(text = "小节标题")
        },
        ShowcaseSection("divider", "HorizontalDivider") {
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
        },
        ShowcaseSection("linear-progress", "LinearProgressIndicator") {
            LinearProgressIndicator(progress = 0.6f, modifier = Modifier.fillMaxWidth())
        },
        ShowcaseSection("circular-progress", "CircularProgressIndicator") {
            CircularProgressIndicator(progress = 0.6f)
        },
        ShowcaseSection("infinite-progress", "InfiniteProgressIndicator") {
            InfiniteProgressIndicator()
        },
        // ---- 浮层组（成对：Overlay* 应用内 / Window* 独立窗口，inventory §3 坑 1）----
        ShowcaseSection("overlay-dialog", "OverlayDialog") {
            var show by remember { mutableStateOf(true) }
            OverlayDialog(
                show = show,
                title = "OverlayDialog",
                summary = "应用内浮层 · 离屏可捕获",
                onDismissRequest = { show = false },
            ) {
                Text("同一组合成树内的浮层，dim 与面板同帧出图。")
            }
        },
        ShowcaseSection("overlay-bottom-sheet", "OverlayBottomSheet") {
            var show by remember { mutableStateOf(true) }
            OverlayBottomSheet(
                show = show,
                title = "OverlayBottomSheet",
                onDismissRequest = { show = false },
            ) {
                Text("应用内 bottom sheet。")
                Spacer(Modifier.height(8.dp))
                Text("第二行内容用于观察内边距。")
            }
        },
        ShowcaseSection("window-dialog", "WindowDialog（独立窗口）") {
            var show by remember { mutableStateOf(windowOpen) }
            WindowDialog(
                show = show,
                title = "WindowDialog",
                summary = "系统窗口浮层",
                onDismissRequest = {},
            ) {
                Text("独立平台窗口，打开态基线由 renderBaselineWindowed 截取。")
            }
            if (!windowOpen) {
                Text(
                    "Window* 走平台 Dialog（独立窗口），离屏渲染捕获不到；" +
                        "此屏只有说明文字，打开态基线见 renderBaselineWindowed。",
                )
            }
        },
        ShowcaseSection("window-bottom-sheet", "WindowBottomSheet（独立窗口）") {
            var show by remember { mutableStateOf(windowOpen) }
            WindowBottomSheet(
                show = show,
                title = "WindowBottomSheet",
                onDismissRequest = {},
            ) {
                Text("独立平台窗口的 bottom sheet。")
                Spacer(Modifier.height(8.dp))
                Text("第二行内容用于观察内边距。")
            }
            if (!windowOpen) {
                Text("同 WindowDialog：离屏出关闭态说明，打开态由窗口截图产出。")
            }
        },
        // ---- 导航组 ----
        ShowcaseSection("navigation-bar", "NavigationBar") {
            NavigationBar {
                NavigationBarItem(selected = true, onClick = {}, icon = MiuixIcons.Home, label = "首页")
                NavigationBarItem(selected = false, onClick = {}, icon = MiuixIcons.GridView, label = "工具")
                NavigationBarItem(selected = false, onClick = {}, icon = MiuixIcons.Settings, label = "设置")
            }
        },
        ShowcaseSection("navigation-rail", "NavigationRail") {
            Row(modifier = Modifier.height(260.dp)) {
                NavigationRail {
                    NavigationRailItem(selected = true, onClick = {}, icon = MiuixIcons.Home, label = "首页")
                    NavigationRailItem(selected = false, onClick = {}, icon = MiuixIcons.GridView, label = "工具")
                    NavigationRailItem(selected = false, onClick = {}, icon = MiuixIcons.Settings, label = "设置")
                }
                Spacer(Modifier.width(16.dp))
                Text("宽屏 / 横屏用的侧边 Rail（收起态）。")
            }
        },
        ShowcaseSection("tab-row", "TabRow") {
            TabRow(tabs = listOf("日", "周", "月", "年"), selectedTabIndex = 0, onTabSelected = {})
        },
        ShowcaseSection("tab-row-contour", "TabRowWithContour") {
            TabRowWithContour(tabs = listOf("日", "周", "月", "年"), selectedTabIndex = 1, onTabSelected = {})
        },
    )
