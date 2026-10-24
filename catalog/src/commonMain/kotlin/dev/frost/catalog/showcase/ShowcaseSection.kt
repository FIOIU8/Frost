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
import top.yukonga.miuix.kmp.basic.RadioButton
import top.yukonga.miuix.kmp.basic.Slider
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.Surface
import top.yukonga.miuix.kmp.basic.Switch
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.basic.TextField

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
 * 组件巡礼小节的清单。顺序按 P1 计划的分组：容器 → 交互 → 输入 → 选择 → 反馈。
 *
 * 注意：这里只摆"能一眼看出视觉差异"的最小用法，不做交互状态演示
 * （基线是静态截图，交互态交给以后的 UI 测试）。
 */
fun showcaseSections(): List<ShowcaseSection> =
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
    )
