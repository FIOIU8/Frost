package dev.frost.catalog.design

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import com.kyant.backdrop.drawBackdrop
import com.kyant.backdrop.effects.blur
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.math.tanh

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
 * 返回键：**纯圆**图标按钮（用户 2026-10-24 裁决，对齐 AndroidLiquidGlass 的圆形按钮）。
 *
 * 材质走玻璃语义：有捕获源时真模糊，否则半透明填充；描边统一 1dp glassStroke。
 * 箭头保持 Canvas 手绘（字形比例对不上参考，见 git 历史里 FrostBackArrow 的校准记录）。
 */
@Composable
fun FrostBackButton(
    modifier: Modifier = Modifier,
    color: Color = FrostColor.textPrimary,
) {
    val shape = CircleShape
    val backdrop = LocalFrostBackdrop.current
    val base =
        if (backdrop == null) {
            Modifier
                .clip(shape)
                .background(FrostGradient.glassFill)
        } else {
            Modifier.drawBackdrop(
                backdrop = backdrop,
                shape = { shape },
                effects = { blur(FrostSize.glassBlur.toPx()) },
                highlight = null,
                shadow = null,
                onDrawSurface = { drawRect(FrostGradient.glassFill) },
            )
        }
    Box(
        modifier =
            modifier
                .then(base)
                .size(FrostSize.circleIcon)
                .border(1.dp, FrostGradient.glassStroke, shape),
        contentAlignment = Alignment.Center,
    ) {
        // 箭头占圆的 ~1/2，与参考图 63×58px@3x（21×19dp）的比例一致
        FrostArrowGlyph(color = color, boxSize = FrostSize.circleIcon / 2)
    }
}

/** 返回箭头图形：两条线 + 一个折角。字形画不出参考比例，只能手绘。 */
@Composable
private fun FrostArrowGlyph(
    color: Color,
    boxSize: Dp,
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

/** 顶栏：返回键（纯圆）+ 标题（居中偏左）。 */
@Composable
fun FrostNavBar(title: String) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = FrostSize.screenPadding, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FrostBackButton()
        Spacer(Modifier.width(14.dp))
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

/** 导航胶囊的单项：图标（业务自绘）+ 文字。图标为 null 时退回占位菱形。 */
data class FrostNavItem(
    val label: String,
    val icon: (@Composable () -> Unit)? = null,
)

/**
 * 橡皮筋位移：拖得多、走得少，渐近 [max] 而永不超过。
 *
 * 数学性质（有单测）：x=0 → 0；小位移近似线性；x→∞ 渐近 max；单调、保号。
 * 手感参考 iOS 的 rubber band，"只能拉动一点点，松手弹回去"。
 */
internal fun frostRubberBand(
    raw: Float,
    max: Float,
): Float = if (raw == 0f || max <= 0f) 0f else max * tanh(raw / max)

/**
 * 底部悬浮胶囊导航。
 *
 * 材质：**磨砂白实心，不做玻璃**（用户 2026-10-24 裁决——虽然现在有 Backdrop 能力了，
 * 这个组件也不用）；圆角 = 完整胶囊（参考图实测圆角 ≈ 半高）。
 *
 * 交互：**长按后可拖动**，位移经 [frostRubberBand] 压缩（不跟手，像拉皮筋），
 * 松手用弹簧动画弹回原位。位移走 `offset`（placement 阶段），不触发重排。
 */
@Composable
fun FrostFloatingNavPill(
    items: List<FrostNavItem>,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val haptics = LocalHapticFeedback.current
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }
    // 长按拖拽期间记录的"原始手指位移"（未经橡皮筋压缩）
    var rawX by remember { mutableStateOf(0f) }
    var rawY by remember { mutableStateOf(0f) }

    fun springBack() {
        rawX = 0f
        rawY = 0f
        scope.launch {
            launch { offsetX.animateTo(0f, spring(dampingRatio = 0.55f, stiffness = 380f)) }
            launch { offsetY.animateTo(0f, spring(dampingRatio = 0.55f, stiffness = 380f)) }
        }
    }

    val dragModifier =
        Modifier.pointerInput(items) {
            detectDragGesturesAfterLongPress(
                onDragStart = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    rawX = 0f
                    rawY = 0f
                    scope.launch { offsetX.snapTo(0f) }
                    scope.launch { offsetY.snapTo(0f) }
                },
                onDrag = { change, amount ->
                    change.consume()
                    rawX += amount.x
                    rawY += amount.y
                    val max = FrostSize.navDragMax.toPx()
                    scope.launch { offsetX.snapTo(frostRubberBand(rawX, max)) }
                    scope.launch { offsetY.snapTo(frostRubberBand(rawY, max)) }
                },
                onDragEnd = { springBack() },
                onDragCancel = { springBack() },
            )
        }

    Row(
        modifier =
            modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .then(dragModifier)
                .clip(CircleShape)
                .background(FrostGradient.navPillFill)
                .padding(horizontal = 28.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(36.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (item.icon != null) {
                    item.icon()
                } else {
                    FrostText("◆", FrostType.rowTitle, FrostColor.onNavPill)
                }
                Spacer(Modifier.height(2.dp))
                FrostText(item.label, FrostType.sectionLabel, FrostColor.onNavPill)
            }
        }
    }
}
