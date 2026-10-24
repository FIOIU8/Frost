package dev.frost.catalog.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.frost.catalog.CatalogApp

/**
 * catalog 的 Android 入口：只做一件事——把 `:catalog` 的 Compose UI 挂上去。
 * 主题（含 Monet 动态取色）、组件、页面全在 `:catalog` 的 commonMain 里。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatalogApp()
        }
    }
}
