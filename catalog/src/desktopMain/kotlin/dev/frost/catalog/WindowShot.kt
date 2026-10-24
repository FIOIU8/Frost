package dev.frost.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.frost.catalog.showcase.ShowcaseScreen
import dev.frost.catalog.showcase.showcaseSections
import kotlinx.coroutines.delay
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import java.awt.GraphicsEnvironment
import java.awt.Rectangle
import java.awt.Robot
import java.io.File
import javax.imageio.ImageIO

/**
 * Window* 浮层的成对基线：用**真实平台窗口**打开（WindowDialog / WindowBottomSheet），
 * 再用 java.awt.Robot 抓屏保存。
 *
 * 为什么不能用离屏渲染：`Window*` 走 Compose `Dialog()`（独立平台窗口），
 * `renderComposeScene` 只光栅化场景树，子窗口不在其中；无头 CI 里甚至直接 HeadlessException。
 * 所以离屏基线里这些 section 保持关闭态（showcaseSections(windowOpen=false)），
 * 打开态由本任务产出（本地有桌面环境时运行；CI 无头时自动跳过）。
 *
 * 用法：`./gradlew :catalog:renderBaselineWindowed`（会短暂弹出真实窗口，属预期）。
 */
fun main(args: Array<String>) {
    val outDir = File(args.firstOrNull() ?: "build/baseline-windowed").apply { mkdirs() }
    if (GraphicsEnvironment.isHeadless()) {
        println("headless 环境：Window* 基线需要真实窗口，跳过（离屏基线不含它们，属预期）")
        return
    }

    // 与离屏基线同名同序：showcase-<sectionId>-<mode>.png，方便 P5 双轨比对
    val tasks =
        listOf(
            WindowedShot("showcase-window-dialog-light.png", ColorSchemeMode.Light, "window-dialog"),
            WindowedShot("showcase-window-dialog-dark.png", ColorSchemeMode.Dark, "window-dialog"),
            WindowedShot("showcase-window-bottom-sheet-light.png", ColorSchemeMode.Light, "window-bottom-sheet"),
            WindowedShot("showcase-window-bottom-sheet-dark.png", ColorSchemeMode.Dark, "window-bottom-sheet"),
        )
    val sections = showcaseSections(windowOpen = true).associateBy { it.id }

    application {
        var index by remember { mutableStateOf(0) }
        if (index >= tasks.size) {
            ExitWhenReady()
        } else {
            val task = tasks[index]
            val section = sections.getValue(task.sectionId)
            val state =
                rememberWindowState(size = DpSize(360.dp, 640.dp))
            Window(
                onCloseRequest = { index = tasks.size },
                state = state,
                title = "frost windowed baseline",
                alwaysOnTop = true,
            ) {
                ShowcaseScreen(section = section, controller = rememberCatalogController(task.mode))
                LaunchedEffect(index) {
                    // 等窗口显示 + 浮层入场动画走完
                    delay(1200)
                    captureShowingWindows(outDir.resolve(task.fileName))
                    index++
                }
            }
        }
    }
    println("windowed baselines → ${outDir.absolutePath}")
}

private data class WindowedShot(
    val fileName: String,
    val mode: ColorSchemeMode,
    val sectionId: String,
)

/** 抓取当前所有可见 AWT 窗口的并集（父窗口 + Dialog 子窗口）。 */
private fun captureShowingWindows(file: File) {
    val windows =
        java.awt.Window
            .getWindows()
            .filter { it.isVisible }
    require(windows.isNotEmpty()) { "没有可见窗口可截取" }
    windows.forEach { w ->
        println(
            "window: ${w.javaClass.simpleName} loc=${w.locationOnScreen} " +
                "size=${w.size} scale=${w.graphicsConfiguration.defaultTransform.scaleX}",
        )
    }
    val robot = Robot()
    // Windows（Java 9+）的 AWT 窗口坐标/尺寸已是设备像素，Robot 也吃设备像素：直接用，不做换算。
    // 种子必须是第一个窗口的矩形：空 Rectangle() 在原点 (0,0)，会把窗口左上方的屏幕内容裹进来。
    val union =
        windows
            .map { Rectangle(it.locationOnScreen.x, it.locationOnScreen.y, it.size.width, it.size.height) }
            .reduce { acc, r -> acc.union(r) as Rectangle }
    println("union: $union")
    val image = robot.createScreenCapture(union)
    ImageIO.write(image, "png", file)
    println("captured: ${file.name} (${image.width}x${image.height})")
}

/** 组合结束后退出应用（放在组合里，等最后一帧真正画完再走）。 */
@Composable
private fun ApplicationScope.ExitWhenReady() {
    LaunchedEffect(Unit) {
        delay(300)
        exitApplication()
    }
}
