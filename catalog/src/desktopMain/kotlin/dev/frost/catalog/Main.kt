package dev.frost.catalog

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

/**
 * Desktop 入口（P1 双入口之一）。
 *
 * 运行：`./gradlew :catalog:run`
 * 打包：`./gradlew :catalog:packageDistributionForCurrentOS`
 */
fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Frost catalog",
        ) {
            CatalogApp()
        }
    }
}
