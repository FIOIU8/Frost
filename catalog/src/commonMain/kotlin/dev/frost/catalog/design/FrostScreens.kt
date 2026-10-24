package dev.frost.catalog.design

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.emptyBackdrop
import com.kyant.backdrop.drawBackdrop
import androidx.compose.foundation.layout.fillMaxSize as fillMax

/**
 * 参考图 1/2/3 的复刻：设置页 + 底部单选弹层。
 *
 * 这里刻意不用 Miuix 组件，而是用 Frost 自己的 token + 原语拼——
 * 目的就是验证"这套视觉能不能用纯基础件复现"，能复现才谈得上做成库。
 */
@Composable
fun FrostSettingsScreen(accent: Color = FrostColor.accentRed) {
    FrostPageBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            FrostNavBar("设置")
            Spacer(Modifier.height(6.dp))

            FrostCard(modifier = Modifier.padding(horizontal = FrostSize.screenPadding)) {
                FrostRow("录音云同步", value = "未开启", indicator = FrostIndicator.Chevron)
                FrostRowDivider()
                FrostRow("录音文件格式", value = "MP3", indicator = FrostIndicator.Picker)
                FrostRowDivider()
                FrostRow("录音模式", value = "标准模式", indicator = FrostIndicator.Picker)
            }

            FrostSectionLabel("AI 录音")

            FrostCard(modifier = Modifier.padding(horizontal = FrostSize.screenPadding)) {
                FrostRow("默认声源语言", value = "简体中文", indicator = FrostIndicator.Picker)
                FrostRowDivider()
                FrostRow(
                    title = "AI 录音助手",
                    subtitle = "录音时自动开启实时转写（本月剩余 100/100 分钟，每月1日0点刷新）",
                    indicator = FrostIndicator.Switch,
                    switchOn = true,
                    accent = accent,
                )
            }

            Spacer(Modifier.height(FrostSize.cardGap))

            FrostCard(modifier = Modifier.padding(horizontal = FrostSize.screenPadding)) {
                FrostRow("第三方信息共享清单", indicator = FrostIndicator.Chevron)
            }
        }
    }
}

/**
 * 单选悬浮窗（**不是抽屉**）。
 *
 * 关键几何：参考图里弹窗**不贴屏幕边缘**——左右各留 12dp、底部留 24dp，
 * 四个角都是圆的（28dp），下方还能露出被遮住页面的一行内容。
 * 这是"悬浮窗"与"bottom sheet"最本质的区别，不能做成贴底 + 只有上面两角圆。
 *
 * 悬浮感（用户 2026-10-24 裁决）：走 [frostFloatingShadow]（暗色底部发光 / 亮色底部投影），
 * 比 [dev.frost.catalog.design.FrostGlassCard] 高一档（elevated）；1dp 描边保留。
 */
@Composable
fun FrostOptionDialog(
    title: String,
    options: List<Pair<String, String?>>,
    selectedIndex: Int,
    accent: Color = FrostColor.accentRed,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(FrostSize.sheetRadius)
    val isDark = LocalFrostIsDark.current
    Column(
        modifier =
            modifier
                .padding(horizontal = 12.dp, vertical = 24.dp)
                .fillMaxWidth()
                .drawBackdrop(
                    backdrop = emptyBackdrop(),
                    shape = { shape },
                    effects = {},
                    highlight = null,
                    shadow = { frostFloatingShadow(isDark, elevated = true) },
                    onDrawSurface = { drawRect(FrostColor.surfaceElevated) },
                ).border(1.dp, FrostColor.dialogStroke, shape)
                .padding(horizontal = 20.dp, vertical = 14.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            FrostText(title, FrostType.sheetTitle)
        }
        Spacer(Modifier.height(10.dp))

        options.forEachIndexed { index, option ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val desc = option.second
                Column(modifier = Modifier.weight(1f)) {
                    FrostText(option.first, FrostType.rowTitle)
                    if (desc != null) {
                        Spacer(Modifier.height(3.dp))
                        FrostText(desc, FrostType.sheetDesc, FrostColor.textSecondary)
                    }
                }
                Spacer(Modifier.width(12.dp))
                FrostRadio(selected = index == selectedIndex, accent = accent)
            }
            if (index != options.lastIndex) FrostRowDivider()
        }

        Spacer(Modifier.height(12.dp))
        FrostPillButton(
            text = "取消",
            container = FrostColor.surfacePressed,
            contentColor = FrostColor.textPrimary,
        )
        Spacer(Modifier.height(14.dp))
    }
}

/** 设置页 + 打开状态的悬浮窗（参考图 1 的整体效果） */
@Composable
fun FrostSettingsWithDialog(accent: Color = FrostColor.accentRed) {
    // 背景渐变由 FrostSettingsScreen 自己的 FrostPageBackground 提供，这里只叠遮罩与浮层
    Box(modifier = Modifier.fillMaxSize()) {
        FrostSettingsScreen(accent)
        Box(modifier = Modifier.fillMaxSize().background(Color(0x8C000000)))
        FrostOptionDialog(
            title = "录音模式",
            options =
                listOf(
                    "标准模式" to "自然拾音，适用于大部分录音场景",
                    "会议模式" to "放大人声，适用于会议、课堂等远距离录音场景",
                    "采访模式" to "智能降噪，适用于采访等近距离录音场景",
                ),
            selectedIndex = 0,
            accent = accent,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

/** 参考图 4 的复刻：列表页（大标题 + 筛选 chip + 分组项 + 圆形播放） */
@Composable
fun FrostListScreen(accent: Color = FrostColor.accentBlue) {
    FrostPageBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(Modifier.height(FrostSize.screenPadding))

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = FrostSize.screenPadding),
                verticalAlignment = Alignment.Bottom,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    FrostText("录音", FrostType.display)
                    Spacer(Modifier.height(2.dp))
                    FrostText("338 个通话录音", FrostType.rowSubtitle, FrostColor.textSecondary)
                }
                FrostText("⌕", FrostType.navTitle, FrostColor.textPrimary)
                Spacer(Modifier.width(18.dp))
                FrostText("⋮", FrostType.navTitle, FrostColor.textPrimary)
            }

            Spacer(Modifier.height(14.dp))

            // 筛选 chips
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = FrostSize.screenPadding),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FrostChip("全部录音", selected = true, accent = accent)
                FrostChip("通话录音", selected = false, accent = accent)
                FrostChip("普通录音", selected = false, accent = accent)
            }

            Spacer(Modifier.height(FrostSize.cardGap))

            // 提示横幅
            Row(
                modifier =
                    Modifier
                        .padding(horizontal = FrostSize.screenPadding)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(FrostSize.cardRadius))
                        .background(FrostColor.surface)
                        .padding(horizontal = FrostSize.cardPadding, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FrostText("开启录音自动同步，保障录音数据安全", FrostType.rowSubtitle, FrostColor.textSecondary)
                Spacer(Modifier.weight(1f))
                FrostPillButtonCompact("开启", accent)
            }

            Spacer(Modifier.height(FrostSize.cardGap))

            val items =
                listOf(
                    Triple("20260727233713.aac", "QQ-20260727233713", "7/27 23:59 · 00:21:52"),
                    Triple("20250324201935.mp3", "QQ-20250324201935", "2025/3/24 20:47 · 00:28:01"),
                    Triple("20250323213351.mp3", "QQ-20250323213351", "2025/3/23 21:33 · 00:00:05"),
                )

            items.forEachIndexed { index, item ->
                Column(modifier = Modifier.padding(horizontal = FrostSize.screenPadding)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FrostCountBadge(2, FrostColor.badgeColors[index % FrostColor.badgeColors.size])
                        Spacer(Modifier.width(8.dp))
                        FrostText(item.first, FrostType.rowSubtitle, FrostColor.textSecondary)
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(FrostSize.cardRadius))
                                .background(FrostColor.surface)
                                .padding(start = FrostSize.cardPadding, end = 8.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            FrostText(item.second, FrostType.rowTitle)
                            Spacer(Modifier.height(3.dp))
                            FrostText(item.third, FrostType.rowSubtitle, FrostColor.textSecondary)
                        }
                        Box(
                            modifier =
                                Modifier
                                    .size(FrostSize.playButton)
                                    .clip(CircleShape)
                                    .background(FrostColor.surfacePressed),
                            contentAlignment = Alignment.Center,
                        ) {
                            FrostText("▸", FrostType.navTitle, FrostColor.textPrimary)
                        }
                    }
                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
private fun FrostChip(
    text: String,
    selected: Boolean,
    accent: Color,
) {
    Box(
        modifier =
            Modifier
                .height(36.dp)
                .clip(CircleShape)
                .background(if (selected) accent.copy(alpha = 0.22f) else FrostColor.surface)
                .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        FrostText(text, FrostType.rowSubtitle, if (selected) accent else FrostColor.textPrimary)
    }
}

@Composable
private fun FrostPillButtonCompact(
    text: String,
    accent: Color,
) {
    Box(
        modifier =
            Modifier
                .height(34.dp)
                .clip(CircleShape)
                .background(accent)
                .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        FrostText(text, FrostType.rowSubtitle, Color.White)
    }
}
