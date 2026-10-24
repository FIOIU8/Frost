package dev.frost.catalog.design

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * 橡皮筋位移曲线的性质测试：拖得多走得少、渐近不超限、松手回弹由弹簧动画负责（不在此测）。
 */
class FrostRubberBandTest {
    @Test
    fun `zero input stays zero`() {
        assertEquals(0f, frostRubberBand(0f, 100f))
    }

    @Test
    fun `sign is preserved`() {
        assertTrue(frostRubberBand(-50f, 100f) < 0f)
        assertTrue(frostRubberBand(50f, 100f) > 0f)
    }

    @Test
    fun `small drags are near-linear`() {
        // 10% 以内的位移几乎不打折（tanh 线性区）
        val out = frostRubberBand(10f, 100f)
        assertTrue(out > 9.5f, "expected near-linear, got $out")
        assertTrue(out < 10f, "must never exceed raw input, got $out")
    }

    @Test
    fun `large drags compress and never exceed max`() {
        val out = frostRubberBand(1000f, 100f)
        // Float 精度下 tanh(10)×100 会四舍五入到恰好 max，所以断言是 <= 而非 <
        assertTrue(out <= 100f, "asymptote is max, got $out")
        assertTrue(out > 99f, "far drags should approach max closely, got $out")
    }

    @Test
    fun `always less than raw input`() {
        var x = 1f
        repeat(20) {
            assertTrue(frostRubberBand(x, 60f) < x)
            x *= 1.6f
        }
    }

    @Test
    fun `monotonically increasing`() {
        var prev = 0f
        var x = 0f
        while (x < 500f) {
            x += 7f
            val cur = frostRubberBand(x, 42f)
            // 渐近区 Float 可能四舍五入成相等值，允许 >=
            assertTrue(cur >= prev, "must be monotonic at x=$x")
            prev = cur
        }
    }

    @Test
    fun `symmetric around zero`() {
        assertTrue(abs(frostRubberBand(37f, 60f) + frostRubberBand(-37f, 60f)) < 1e-6f)
    }

    @Test
    fun `non-positive max collapses to zero`() {
        assertEquals(0f, frostRubberBand(123f, 0f))
        assertEquals(0f, frostRubberBand(123f, -5f))
    }
}
