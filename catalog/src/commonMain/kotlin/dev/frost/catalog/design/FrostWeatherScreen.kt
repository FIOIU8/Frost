package dev.frost.catalog.design

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 参考图 14–22 的复刻：沉浸式数据页。
 *
 * 这一页是整套风格里最能体现"渐变 + 玻璃"的场景：
 * - 背景是夜空渐变（不是纯黑）
 * - 卡片是半透明玻璃（这里用 alpha + 描边近似，真模糊留给 P4）
 * - 数据用超大字号表达层级，而不是靠颜色
 */
@Composable
fun FrostWeatherScreen() {
    FrostPageBackground(colors = FrostGradient.skyNight) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                Spacer(Modifier.height(12.dp))

                // 顶栏：城市 + 两个圆形操作
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FrostText("北京市", FrostType.navTitle)
                    Spacer(Modifier.weight(1f))
                    FrostCircleIcon("♪")
                    Spacer(Modifier.width(10.dp))
                    FrostCircleIcon("⋮")
                }

                Spacer(Modifier.height(10.dp))
                FrostText("⛅ 数据发布于 7 分钟前", FrostType.rowSubtitle, Color(0xCCFFFFFF))

                Spacer(Modifier.height(28.dp))

                // 大温度：96sp Light，是这一页的视觉锚点
                Row(verticalAlignment = Alignment.Bottom) {
                    FrostText("13", FrostType.temperature, Color.White)
                    FrostText(
                        "°",
                        FrostType.display,
                        Color.White,
                        modifier = Modifier.padding(bottom = 26.dp),
                    )
                }
                Spacer(Modifier.height(4.dp))
                FrostText("雾 10° / 17° 空气轻度污染 116", FrostType.rowTitle, Color(0xF2FFFFFF))

                Spacer(Modifier.height(24.dp))

                // 玻璃卡：建议/提示
                FrostGlassCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        FrostText("宜穿外套", FrostType.rowTitle, Color.White)
                        Spacer(Modifier.height(4.dp))
                        FrostText("天气偏凉，增加衣物厚度。", FrostType.sheetDesc, Color(0xCCFFFFFF))
                        Spacer(Modifier.height(10.dp))
                        FrostText("查看详情 ›", FrostType.rowSubtitle, Color(0x99FFFFFF))
                    }
                }

                Spacer(Modifier.height(12.dp))

                // 玻璃卡：小时预报（横向排布，靠间距而不是分割线）
                FrostGlassCard {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        listOf(
                            "现在" to "13°",
                            "21:00" to "11°",
                            "22:00" to "10°",
                            "23:00" to "10°",
                            "00:00" to "10°",
                        ).forEach { (time, temp) ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                FrostText(time, FrostType.sectionLabel, Color(0xCCFFFFFF))
                                Spacer(Modifier.height(6.dp))
                                FrostText("☁", FrostType.rowTitle, Color.White)
                                Spacer(Modifier.height(6.dp))
                                FrostText(temp, FrostType.value, Color.White)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // 玻璃卡：空气质量 + 渐变条
                FrostGlassCard {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                FrostText("空气轻度污染", FrostType.rowTitle, Color.White)
                                Spacer(Modifier.height(4.dp))
                                FrostText(
                                    "健康人群出现刺激症状，儿童、呼吸系统疾病患者应减少户外活动。",
                                    FrostType.sheetDesc,
                                    Color(0xCCFFFFFF),
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            FrostText("116", FrostType.navTitle, Color.White)
                        }
                        Spacer(Modifier.height(14.dp))
                        FrostGradientBar()
                    }
                }

                Spacer(Modifier.weight(1f))

                // 底部悬浮胶囊导航：磨砂白实心（非玻璃），图标按用户参考图手绘
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    FrostFloatingNavPill(
                        items =
                            listOf(
                                FrostNavItem("首页") { FrostShieldBoltIcon() },
                                FrostNavItem("实用工具") { FrostGridIcon() },
                            ),
                    )
                }
                Spacer(Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun FrostCircleIcon(glyph: String) {
    FrostGlassCircle(size = FrostSize.circleIcon) {
        FrostText(glyph, FrostType.value, Color.White)
    }
}

/**
 * 参考图（用户 2026-10-24 截图）里的"首页"图标：实心盾牌 + 白色闪电。
 * 业务图标，demo 用 Canvas 手绘；正式图标集是另一件事（Miuix icons / 自绘库）。
 */
@Composable
private fun FrostShieldBoltIcon() {
    androidx.compose.foundation.Canvas(Modifier.size(FrostSize.navIcon)) {
        val w = size.width
        val h = size.height
        val ink = FrostColor.onNavPill
        // 盾牌：宽顶、圆肩、收底尖
        val shield =
            androidx.compose.ui.graphics.Path().apply {
                moveTo(w * 0.50f, h * 0.06f)
                cubicTo(w * 0.38f, h * 0.14f, w * 0.24f, h * 0.19f, w * 0.14f, h * 0.21f)
                lineTo(w * 0.14f, h * 0.48f)
                cubicTo(w * 0.14f, h * 0.70f, w * 0.30f, h * 0.86f, w * 0.50f, h * 0.95f)
                cubicTo(w * 0.70f, h * 0.86f, w * 0.86f, h * 0.70f, w * 0.86f, h * 0.48f)
                lineTo(w * 0.86f, h * 0.21f)
                cubicTo(w * 0.76f, h * 0.19f, w * 0.62f, h * 0.14f, w * 0.50f, h * 0.06f)
                close()
            }
        drawPath(shield, ink)
        // 闪电（挖白）
        val bolt =
            androidx.compose.ui.graphics.Path().apply {
                moveTo(w * 0.56f, h * 0.26f)
                lineTo(w * 0.38f, h * 0.53f)
                lineTo(w * 0.49f, h * 0.53f)
                lineTo(w * 0.44f, h * 0.74f)
                lineTo(w * 0.62f, h * 0.46f)
                lineTo(w * 0.51f, h * 0.46f)
                lineTo(w * 0.56f, h * 0.26f)
                close()
            }
        drawPath(bolt, FrostColor.switchThumb)
    }
}

/** 参考图里的"实用工具"图标：2×2 圆角方块。 */
@Composable
private fun FrostGridIcon() {
    androidx.compose.foundation.Canvas(Modifier.size(FrostSize.navIcon)) {
        val w = size.width
        val cell = w * 0.42f
        val gap = w - cell * 2f
        val radius = cell * 0.32f
        val ink = FrostColor.onNavPill
        listOf(0f, 1f).forEach { cx ->
            listOf(0f, 1f).forEach { cy ->
                drawRoundRect(
                    color = ink,
                    topLeft =
                        androidx.compose.ui.geometry
                            .Offset(cx * (cell + gap), cy * (cell + gap)),
                    size =
                        androidx.compose.ui.geometry
                            .Size(cell, cell),
                    cornerRadius =
                        androidx.compose.ui.geometry
                            .CornerRadius(radius, radius),
                )
            }
        }
    }
}
