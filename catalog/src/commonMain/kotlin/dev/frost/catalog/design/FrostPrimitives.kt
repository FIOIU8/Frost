package dev.frost.catalog.design

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur

/** 文本原语：唯一允许写 Text 的地方，其它组件都调它。 */
@Composable
fun FrostText(
    text: String,
    style: FrostTextStyle,
    color: Color = FrostColor.textPrimary,
    modifier: Modifier = Modifier,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style =
            TextStyle(
                color = color,
                fontSize = style.size,
                lineHeight = style.lineHeight,
                fontWeight = style.weight,
                fontFamily = LocalFrostFontFamily.current,
            ),
    )
}

/** 分组卡片：同组多行共用一张，圆角 22dp。 */
@Composable
fun FrostCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(FrostSize.cardRadius))
                .background(FrostColor.surface),
        content = content,
    )
}

/** 行内分隔线：左缩进 16dp，不贯穿整卡。 */
@Composable
fun FrostRowDivider() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = FrostSize.dividerInset)
                .height(FrostSize.dividerThickness)
                .background(FrostColor.separator),
    )
}

/** 右侧指示符：语义固定，见 design-spec §5.1 */
enum class FrostIndicator {
    None,
    Chevron,
    Picker,
    Switch,
}

/**
 * 设置行：标题 +（可选副标题）+（可选值）+ 指示符。
 *
 * 这是整套风格的"语法核心"——三种指示符对应三种交互，不允许新造第四种。
 */
@Composable
fun FrostRow(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    value: String? = null,
    indicator: FrostIndicator = FrostIndicator.None,
    /** 值前面是否带一个小圆点（参考图"定位显示 ● 区/县"那一行） */
    valueDot: Color? = null,
    switchOn: Boolean = false,
    accent: Color = FrostColor.accentGreen,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = if (subtitle == null) FrostSize.rowHeight else FrostSize.rowHeightTwoLine)
                .padding(horizontal = FrostSize.cardPadding, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            FrostText(title, FrostType.rowTitle)
            if (subtitle != null) {
                Spacer(Modifier.height(3.dp))
                FrostText(subtitle, FrostType.rowSubtitle, FrostColor.textSecondary)
            }
        }

        if (value != null) {
            Spacer(Modifier.width(12.dp))
            if (valueDot != null) {
                Box(
                    modifier = Modifier.size(8.dp).clip(CircleShape).background(valueDot),
                )
                Spacer(Modifier.width(6.dp))
            }
            FrostText(value, FrostType.value, FrostColor.textSecondary)
        }

        when (indicator) {
            FrostIndicator.None -> {
                Unit
            }

            FrostIndicator.Chevron -> {
                Spacer(Modifier.width(11.dp))
                FrostText("›", FrostType.value, FrostColor.textTertiary)
            }

            FrostIndicator.Picker -> {
                Spacer(Modifier.width(11.dp))
                FrostText("◇", FrostType.value, FrostColor.textTertiary)
            }

            FrostIndicator.Switch -> {
                Spacer(Modifier.width(12.dp))
                FrostSwitch(checked = switchOn, accent = accent)
            }
        }
    }
}

/** section 小标题：只用文字断句，不画线、不加背景。 */
@Composable
fun FrostSectionLabel(text: String) {
    Box(
        modifier =
            Modifier
                // top 里已经含"上一张卡片后的组间距"：参考图实测组间 56.7dp
                // = cardGap(18) + label 上下内边距(14+8) + 文字行高(16)
                .padding(
                    start = FrostSize.sectionLabelStart,
                    top = FrostSize.cardGap + FrostSize.sectionLabelTop,
                    bottom = FrostSize.sectionLabelBottom,
                ),
    ) {
        FrostText(text, FrostType.sectionLabel, FrostColor.textSecondary)
    }
}

/** 开关：51×31，滑块 27。开=强调色，关=#3A3A3A */
@Composable
fun FrostSwitch(
    checked: Boolean,
    accent: Color = FrostColor.accentGreen,
) {
    Box(
        modifier =
            Modifier
                .size(FrostSize.switchWidth, FrostSize.switchHeight)
                .clip(CircleShape)
                .background(if (checked) accent else FrostColor.switchOff),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .padding(2.dp)
                    .size(FrostSize.switchThumb)
                    .clip(CircleShape)
                    .background(FrostColor.switchThumb),
        )
    }
}

/** 单选圆点：外圈 1.5dp 描边，选中时内圈填强调色。 */
@Composable
fun FrostRadio(
    selected: Boolean,
    accent: Color = FrostColor.accentRed,
) {
    Box(
        modifier =
            Modifier
                .size(FrostSize.radioSize)
                .clip(CircleShape)
                .background(if (selected) accent else Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier =
                    Modifier
                        .size(FrostSize.radioDot)
                        .clip(CircleShape)
                        .background(Color.White),
            )
        } else {
            // 未选中：只有一圈描边，中心透明（参考图里是空心环，不是实心灰点）
            Box(
                modifier =
                    Modifier
                        .size(FrostSize.radioSize)
                        .clip(CircleShape)
                        .border(1.5.dp, FrostColor.radioRing, CircleShape),
            )
        }
    }
}

/** 胶囊按钮：主操作填色，次操作灰底。 */
@Composable
fun FrostPillButton(
    text: String,
    container: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(FrostSize.pillHeight)
                .clip(CircleShape)
                .background(container),
        contentAlignment = Alignment.Center,
    ) {
        FrostText(text, FrostType.button, contentColor)
    }
}

/** 列表左侧计数角标 */
@Composable
fun FrostCountBadge(
    count: Int,
    color: Color,
) {
    Box(
        modifier =
            Modifier
                .size(FrostSize.badgeSize)
                .clip(CircleShape)
                .background(color),
        contentAlignment = Alignment.Center,
    ) {
        FrostText(count.toString(), FrostType.sectionLabel, Color.White)
    }
}

/**
 * 返回箭头：Canvas 手绘。
 *
 * 为什么不用 "←" 字形：字形的高度远小于参考图（参考 63×58px，字形只有 25px 高），
 * 会导致标题整体左移，靠调字号永远对不上。用两条线 + 一个折角反而精确。
 */
@Composable
fun FrostBackArrow(
    color: Color = FrostColor.textPrimary,
    boxSize: Dp = 24.dp,
) {
    androidx.compose.foundation.Canvas(Modifier.size(boxSize)) {
        val w = size.width
        val h = size.height
        val cy = h / 2f
        val stroke = 2.dp.toPx()
        drawLine(
            color = color,
            start =
                androidx.compose.ui.geometry
                    .Offset(w * 0.08f, cy),
            end =
                androidx.compose.ui.geometry
                    .Offset(w * 0.95f, cy),
            strokeWidth = stroke,
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
        )
        drawLine(
            color = color,
            start =
                androidx.compose.ui.geometry
                    .Offset(w * 0.42f, cy - h * 0.40f),
            end =
                androidx.compose.ui.geometry
                    .Offset(w * 0.08f, cy),
            strokeWidth = stroke,
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
        )
        drawLine(
            color = color,
            start =
                androidx.compose.ui.geometry
                    .Offset(w * 0.42f, cy + h * 0.40f),
            end =
                androidx.compose.ui.geometry
                    .Offset(w * 0.08f, cy),
            strokeWidth = stroke,
            cap = androidx.compose.ui.graphics.StrokeCap.Round,
        )
    }
}

/** 顶栏：返回箭头 + 标题（居中偏左）。 */
@Composable
fun FrostNavBar(title: String) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = FrostSize.screenPadding, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // 参考图箭头实测 63×58px（21×19dp）。用字形画不出这个比例，改为 Canvas 绘制
        FrostBackArrow()
        // 箭头右端到标题左端实测 66px ≈ 22dp
        Spacer(Modifier.width(22.dp))
        FrostText(title, FrostType.navTitle)
    }
}

/** 大标题区：34sp Bold + 可选计数副标题 */
@Composable
fun FrostBigHeader(
    title: String,
    subtitle: String? = null,
) {
    Column(
        modifier = Modifier.padding(start = FrostSize.screenPadding, end = FrostSize.screenPadding),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        FrostText(title, FrostType.display)
        if (subtitle != null) {
            FrostText(subtitle, FrostType.rowSubtitle, FrostColor.textSecondary)
        }
    }
}

/**
 * 页面背景：垂直渐变，不是纯色（design-spec §2.5-(1)）。
 *
 * 同时它还是玻璃的**捕获源**：渐变经 [layerBackdrop] 记录进 [LocalFrostBackdrop]，
 * 页面里的 [FrostGlassCard] 借此拿到真实背景模糊。捕获的只有渐变本身——
 * `layerBackdrop` 要放在 `background` **之前**（它记录的是"内层画了什么"），
 * 且内容必须画在这个 Box 之外，否则单帧渲染里玻璃读到的是空层。
 */
@Composable
fun FrostPageBackground(
    colors: List<Color> = FrostGradient.pageDark,
    content: @Composable () -> Unit,
) {
    val backdrop = rememberLayerBackdrop()
    CompositionLocalProvider(LocalFrostBackdrop provides backdrop) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .layerBackdrop(backdrop)
                        .background(Brush.verticalGradient(colors)),
            )
            Box(modifier = Modifier.fillMaxSize()) { content() }
        }
    }
}

/**
 * 玻璃卡片：半透明填充 + 1dp 描边 + **真实背景模糊**（Backdrop 2.0.1 接线，2026-10-24 起）。
 *
 * 有捕获源（页面在 [FrostPageBackground] / [FrostGlassCapture] 里）时走 `drawBackdrop` +
 * `blur`：Android 需要 API 31+（RenderEffect），桌面 Skiko 恒可用；无捕获源或低于 API 门槛
 * 时回退为纯 alpha 近似（两种路径外观 continuity：填充/描边 token 相同，只是少了模糊）。
 * Solid / Glass 两种材质仍通过与 [FrostCard] 的分工表达，不另起一套 API。
 */
@Composable
fun FrostGlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(FrostSize.cardRadius)
    val backdrop = LocalFrostBackdrop.current
    if (backdrop == null) {
        // 回退：无捕获源。保持半透明 + 描边近似（旧路径，行为不变）
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .background(FrostGradient.glassFill)
                    .border(1.dp, FrostGradient.glassStroke, shape),
            content = content,
        )
    } else {
        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .drawBackdrop(
                        backdrop = backdrop,
                        shape = { shape },
                        effects = {
                            blur(FrostSize.glassBlur.toPx())
                        },
                        highlight = null,
                        shadow = null,
                        // 表面填充画在模糊背景之上、内容之下（替代原来的 .background）
                        onDrawSurface = { drawRect(FrostGradient.glassFill) },
                    ).border(1.dp, FrostGradient.glassStroke, shape),
            content = content,
        )
    }
}

/** 渐变条：用于空气质量、进度等"指示当前值"的场景 */
@Composable
fun FrostGradientBar(
    colors: List<Color> = FrostGradient.airQuality,
    height: Dp = 4.dp,
) {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(height)
                .clip(CircleShape)
                .background(Brush.horizontalGradient(colors)),
    )
}

/** 底部悬浮胶囊导航（磨砂白 + 深色图标），浮在内容之上 */
@Composable
fun FrostFloatingNavPill(
    items: List<String>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .clip(CircleShape)
                .background(FrostGradient.navPillFill)
                .padding(horizontal = 28.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(36.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { label ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                FrostText("◆", FrostType.rowTitle, Color(0xFF111111))
                Spacer(Modifier.height(2.dp))
                FrostText(label, FrostType.sectionLabel, Color(0xFF111111))
            }
        }
    }
}
