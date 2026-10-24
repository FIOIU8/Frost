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
    val surface = Color(0xFF1A1A1A)

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

    /** 磨砂白胶囊上的内容色（图标/文字），参考图取近黑 */
    val onNavPill = Color(0xFF111111)

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
 * 说明：glass 卡片的**真实背景模糊**已通过 Backdrop 2.0.1 接线（[dev.frost.catalog.design.FrostGlassCard]），
 * 依赖 RenderEffect（Android 31+ / 桌面 Skiko）。无捕获源或低于 API 门槛时回退为这里的
 * 半透明近似。完整的能力矩阵与降级策略仍在 P4。
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
    val navTitle = FrostTextStyle(21.sp, 27.sp, FontWeight.Medium)
    val rowTitle = FrostTextStyle(19.sp, 25.sp, FontWeight.Normal)
    val rowSubtitle = FrostTextStyle(13.sp, 18.sp, FontWeight.Normal)
    val value = FrostTextStyle(15.sp, 20.sp, FontWeight.Normal)
    val sectionLabel = FrostTextStyle(13.sp, 16.sp, FontWeight.Normal)
    val sheetTitle = FrostTextStyle(20.sp, 26.sp, FontWeight.Medium)
    val sheetDesc = FrostTextStyle(13.sp, 19.sp, FontWeight.Normal)
    val button = FrostTextStyle(17.sp, 22.sp, FontWeight.Medium)
    val temperature = FrostTextStyle(96.sp, 100.sp, FontWeight.Light)
}

/** 尺寸，见 design-spec §4 */
object FrostSize {
    val screenPadding = 19.dp
    val cardPadding = 19.dp
    val cardRadius = 22.dp
    val sheetRadius = 28.dp
    val iconRadius = 14.dp

    val rowHeight = 57.dp
    val rowHeightTwoLine = 72.dp
    val dividerThickness = 1.dp
    val dividerInset = 19.dp

    val cardGap = 18.dp
    val sectionLabelTop = 14.dp
    val sectionLabelBottom = 8.dp
    val sectionLabelStart = 38.dp

    val pillHeight = 52.dp

    /** 玻璃模糊半径：design-spec §2.5 给的区间是 24–40dp（背景越复杂越大），平滑渐变取下限 */
    val glassBlur = 24.dp
    val switchWidth = 51.dp
    val switchHeight = 31.dp
    val switchThumb = 27.dp
    val radioSize = 22.dp
    val radioDot = 12.dp
    val badgeSize = 28.dp
    val playButton = 56.dp

    /** 纯圆按钮直径（返回键、圆形图标位），参考 AndroidLiquidGlass 的圆形按钮 */
    val circleIcon = 40.dp

    /** 导航胶囊里的图标边长（参考图实测 ≈ 胶囊高度的 1/3） */
    val navIcon = 24.dp

    /** 导航胶囊橡皮筋拖拽的渐近上限：再怎么拉，位移也不会超过它（见 frostRubberBand） */
    val navDragMax = 20.dp
}
