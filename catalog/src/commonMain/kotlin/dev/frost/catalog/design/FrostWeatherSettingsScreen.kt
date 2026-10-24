package dev.frost.catalog.design

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * **1:1 复刻参考图**（`Screenshot_2026-10-24-19-57-20-77`，天气 App 设置页）。
 *
 * 为什么要单独做这一页：之前用自编内容去和参考图叠，文字全错位，叠图毫无意义。
 * 只有内容和结构都一致时，叠图才能暴露出真实的几何偏差。
 *
 * 参考图实测（1240×2772 @3x）：
 * - 卡片左右外边距 57px → **19~20dp**
 * - 卡片色 `#1A1A1A`，页面底色 `#000000`
 * - 单行卡高 182px → **60.7dp**；两行卡 350px → 116.7dp（≈58.3dp/行）
 * - 四行卡 686px → 228.7dp（≈57.2dp/行）
 * - 组与组（含 section 文字）间距 ≈ 170px → **56.7dp**
 */
@Composable
fun FrostWeatherSettingsScreen() {
    FrostPageBackground {
        Column(modifier = Modifier.fillMaxSize()) {
            FrostNavBar("设置")
            Spacer(Modifier.height(10.dp))

            // 组 1：单行
            FrostCard(modifier = Modifier.padding(horizontal = FrostSize.screenPadding)) {
                FrostRow(
                    title = "定位显示",
                    value = "区/县",
                    valueDot = Color(0xFFF86860),
                    indicator = FrostIndicator.Chevron,
                )
            }

            FrostSectionLabel("天气提醒")
            FrostCard(modifier = Modifier.padding(horizontal = FrostSize.screenPadding)) {
                FrostRow("定位城市提醒", indicator = FrostIndicator.Chevron)
                FrostRowDivider()
                FrostRow("特别关注城市提醒", indicator = FrostIndicator.Chevron)
            }

            FrostSectionLabel("单位")
            FrostCard(modifier = Modifier.padding(horizontal = FrostSize.screenPadding)) {
                FrostRow("温度", value = "℃ - 摄氏度", indicator = FrostIndicator.Picker)
                FrostRowDivider()
                FrostRow("风力", value = "蒲福风力等级", indicator = FrostIndicator.Picker)
                FrostRowDivider()
                FrostRow("气压", value = "hPa - 百帕", indicator = FrostIndicator.Picker)
                FrostRowDivider()
                FrostRow("能见度", value = "km - 千米", indicator = FrostIndicator.Picker)
            }

            Spacer(Modifier.height(FrostSize.cardGap))

            FrostCard(modifier = Modifier.padding(horizontal = FrostSize.screenPadding)) {
                FrostRow("用户体验计划", indicator = FrostIndicator.Chevron)
                FrostRowDivider()
                FrostRow("关于天气", indicator = FrostIndicator.Chevron)
                FrostRowDivider()
                FrostRow("收集个人信息明示清单", indicator = FrostIndicator.Chevron)
                FrostRowDivider()
                FrostRow("第三方信息共享清单", indicator = FrostIndicator.Chevron)
                FrostRowDivider()
                FrostRow("隐私", indicator = FrostIndicator.Chevron)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
