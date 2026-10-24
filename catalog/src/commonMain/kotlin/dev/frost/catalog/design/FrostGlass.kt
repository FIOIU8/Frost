package dev.frost.catalog.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.layerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop

/**
 * 玻璃材质的"背景捕获"层（Backdrop 2.0.1，P4 正式模块出现前的 catalog 级接线）。
 *
 * 接线规则来自对 `LayerBackdrop` 源码的实读（不是猜的）：
 * - `Modifier.layerBackdrop(backdrop)` 挂在**捕获源**上，它在自身内容画完**之后**才把画面记录进层；
 * - 因此玻璃元素必须画在捕获源**之后**（兄弟节点、content 在源之外），同一帧内才能读到已记录的画面。
 *   若把玻璃卡片放进捕获源内部，离屏单帧渲染时层还是空的 → 只有半透明填充、没有模糊；
 * - 没有捕获源时（`LocalFrostBackdrop == null`），[FrostGlassCard] 自动回退为 alpha 近似。
 *
 * 能力差异（`Platform.kt` 实读）：`blur` 需要 RenderEffect——Android API 31+ / 桌面 Skiko 恒可用；
 * `lens` / `colorControls` 等着色器效果需要 API 33+（桌面恒可用）。低于门槛时 Backdrop 静默跳过，
 * 视觉退化为半透明填充，与回退路径一致。
 */
val LocalFrostBackdrop = staticCompositionLocalOf<LayerBackdrop?> { null }

/**
 * 捕获一个背景层，供浮在它之上的玻璃元素取用。
 *
 * 两个坑都踩过（见 log 2026-10-24）：
 * 1. `CompositionLocalProvider` 的作用域必须**同时覆盖捕获源与玻璃元素**——只包捕获内容的话，
 *    玻璃元素（[content] 里的）读到的是 null，静默走回退，看起来"就是没模糊"；
 * 2. 玻璃元素必须画在捕获源**之后**（[content] 里、捕获盒的兄弟节点），同一帧内才读得到已记录
 *    的画面；放进捕获盒内部则离屏单帧渲染时层是空的。
 *
 * 所以拆成两个槽：[backdropLayer] 是被捕获的"底"，[content] 是浮在底上的东西（含玻璃）。
 */
@Composable
fun FrostGlassCapture(
    modifier: Modifier = Modifier,
    backdropLayer: @Composable () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val backdrop = rememberLayerBackdrop()
    CompositionLocalProvider(LocalFrostBackdrop provides backdrop) {
        Box(modifier = modifier) {
            Box(modifier = Modifier.matchParentSize().layerBackdrop(backdrop)) { backdropLayer() }
            content()
        }
    }
}

/**
 * 玻璃模糊探针：验证样张（**不是**参考图复刻），专门用来肉眼区分"真模糊"与"alpha 近似"。
 *
 * 高对比度色带上放两张同样的卡片：第一张有捕获源（真模糊，色带边界在卡内被明显抹匀）；
 * 第二张显式清空捕获源（回退，边界依然锐利）。两者并排，差异即证据。
 */
@Composable
fun FrostGlassProbeScreen() {
    FrostGlassCapture(
        // 高频背景：airQuality 色带重复两轮（8 条），保证每张卡片都跨在色带边界上，
        // 否则模糊落在平坦区域里根本显不出来（首版探针的教训，见 log）
        backdropLayer = {
            Column(modifier = Modifier.fillMaxSize()) {
                (FrostGradient.airQuality + FrostGradient.airQuality).forEach { color ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .background(color),
                    )
                }
            }
        },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = FrostSize.screenPadding, vertical = 96.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            FrostGlassCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    FrostText("玻璃 · 真模糊", FrostType.rowTitle)
                    Spacer(Modifier.height(4.dp))
                    FrostText(
                        "背后是 Backdrop 捕获的实时画面，经 RenderEffect 模糊后垫在卡片底下。",
                        FrostType.sheetDesc,
                        FrostColor.textSecondary,
                    )
                }
            }

            // 显式清空捕获源 → 走无模糊的回退路径，作为对照组
            CompositionLocalProvider(LocalFrostBackdrop provides null) {
                FrostGlassCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FrostText("玻璃 · 回退（无捕获源）", FrostType.rowTitle)
                        Spacer(Modifier.height(4.dp))
                        FrostText(
                            "只有半透明填充与描边，背后的色带边缘依然锐利。",
                            FrostType.sheetDesc,
                            FrostColor.textSecondary,
                        )
                    }
                }
            }
        }
    }
}
