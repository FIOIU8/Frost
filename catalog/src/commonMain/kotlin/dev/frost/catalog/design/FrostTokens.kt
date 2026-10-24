package dev.frost.catalog.design

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Frost 视觉 token —— 数值来自 `docs/design-spec.md`（对 22 张参考截图做直方图取样得出）。
 *
 * 规则：组件里**只允许引用这里的 token**，不允许写字面量颜色/字号（PLAN §13 质量门）。
 */
object FrostColor {
    val bg = Color(0xFF000000)
    val surface = Color(0xFF181818)

    /** 弹层比卡片略亮一档，才能从遮罩里"浮"出来 */
    val surfaceElevated = Color(0xFF1F1F1F)
    val surfacePressed = Color(0xFF303030)

    /** 行内分隔线：不贯穿整卡，左缩进 16dp */
    val separator = Color(0xFF303030)

    val textPrimary = Color(0xFFE8E8E8)
    val textSecondary = Color(0xFF909090)
    val textTertiary = Color(0xFF6E6E6E)

    val switchOff = Color(0xFF3A3A3A)
    val radioRing = Color(0xFF4A4A4A)

    /** 悬浮窗描边：dark 下阴影不可见，靠 1dp 亮边把浮层从背景里拉出来 */
    val dialogStroke = Color(0xFF333333)
    val switchThumb = Color(0xFFFFFFFF)

    /** 强调色：由业务选，结构不变（对应 ThemeController(keyColor) 的模型） */
    val accentRed = Color(0xFFF86860)
    val accentBlue = Color(0xFF1888F8)
    val accentGreen = Color(0xFF20B030)

    /** 列表左侧计数角标的配色轮换 */
    val badgeColors =
        listOf(
            Color(0xFFF86860),
            Color(0xFF1888F8),
            Color(0xFFB060F8),
            Color(0xFF20B0C0),
        )
}

/**
 * 材质层：颜色只是底，外观来自「渐变 + 模糊」（design-spec §2.5）。
 *
 * 说明：这里的 glass 只是**半透明 + 描边**的近似，真正的背景模糊（RenderEffect / miuix-blur）
 * 放到 P4，因为 miuix-blur 要求 minSdk 33。别误以为这里已经做了模糊。
 */
object FrostGradient {
    /** 普通页面背景：极淡自上而下渐变，用来"托起"状态栏 */
    val pageDark = listOf(Color(0xFF141414), Color(0xFF000000))
    val pageLight = listOf(Color(0xFFF7F7F9), Color(0xFFEDEDF0))

    /** 沉浸页：夜空（顶部深蓝 → 雾蓝 → 近黑） */
    val skyNight =
        listOf(
            Color(0xFF1B2A4A),
            Color(0xFF3A4A63),
            Color(0xFF4A5A72),
            Color(0xFF2E3A4E),
            Color(0xFF0B0B0C),
        )

    /** 空气质量条：绿→黄→橙→红 */
    val airQuality =
        listOf(
            Color(0xFF20B030),
            Color(0xFFE8C020),
            Color(0xFFF09030),
            Color(0xFFF04838),
        )

    /** 玻璃卡片填充 / 描边（暗色） */
    val glassFill = Color(0x14FFFFFF)
    val glassStroke = Color(0x1AFFFFFF)

    /** 底部悬浮胶囊导航（磨砂白） */
    val navPillFill = Color(0xF2FFFFFF)
}

/** 字号 / 行高 / 字重，见 design-spec §3 */
data class FrostTextStyle(
    val size: TextUnit,
    val lineHeight: TextUnit,
    val weight: FontWeight,
)

object FrostType {
    val display = FrostTextStyle(34.sp, 40.sp, FontWeight.Bold)
    val navTitle = FrostTextStyle(22.sp, 28.sp, FontWeight.Medium)
    val rowTitle = FrostTextStyle(17.sp, 22.sp, FontWeight.Normal)
    val rowSubtitle = FrostTextStyle(13.sp, 18.sp, FontWeight.Normal)
    val value = FrostTextStyle(16.sp, 21.sp, FontWeight.Normal)
    val sectionLabel = FrostTextStyle(13.sp, 16.sp, FontWeight.Normal)
    val sheetTitle = FrostTextStyle(20.sp, 26.sp, FontWeight.Medium)
    val sheetDesc = FrostTextStyle(13.sp, 19.sp, FontWeight.Normal)
    val button = FrostTextStyle(17.sp, 22.sp, FontWeight.Medium)
    val temperature = FrostTextStyle(96.sp, 100.sp, FontWeight.Light)
}

/** 尺寸，见 design-spec §4 */
object FrostSize {
    val screenPadding = 16.dp
    val cardPadding = 16.dp
    val cardRadius = 22.dp
    val sheetRadius = 28.dp
    val iconRadius = 14.dp

    val rowHeight = 56.dp
    val rowHeightTwoLine = 72.dp
    val dividerThickness = 1.dp
    val dividerInset = 16.dp

    val cardGap = 12.dp
    val sectionLabelTop = 20.dp
    val sectionLabelBottom = 6.dp
    val sectionLabelStart = 20.dp

    val pillHeight = 52.dp
    val switchWidth = 51.dp
    val switchHeight = 31.dp
    val switchThumb = 27.dp
    val radioSize = 22.dp
    val radioDot = 12.dp
    val badgeSize = 28.dp
    val playButton = 56.dp
}
