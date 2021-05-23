package com.clipfinder.core.android.spotify.visualizer.renderer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import kotlin.math.log10
import kotlin.math.min
import me.bogerchan.niervisualizer.renderer.IRenderer

class ColumnarVisualizerRenderer(private val paint: Paint) : IRenderer {
    private val lastDrawArea = Rect()
    private lateinit var renderColumns: Array<RectF>

    private val gapRatio = 0.7F
    private val radius = 10F

    override fun onStart(captureSize: Int) {
        renderColumns = Array(min(36, captureSize)) { RectF(0F, 0F, 0F, 0F) }
        lastDrawArea.set(0, 0, 0, 0)
    }

    override fun onStop() = Unit

    override fun getInputDataType() = IRenderer.DataType.FFT

    override fun calculate(drawArea: Rect, data: ByteArray) {
        if (drawArea != lastDrawArea) {
            calculateRenderData(drawArea)
            lastDrawArea.set(drawArea)
        }
        updateWave(data)
    }

    private fun updateWave(data: ByteArray) {
        for (i in 0 until min(data.size / 2, renderColumns.size)) {
            val rfk = data[i]
            val ifk = data[i + 1]
            val magnitude = (rfk * rfk + ifk * ifk).toFloat()
            val dbValue = 75 * log10(magnitude.toDouble()).toFloat()
            val rectF = renderColumns[i]
            rectF.top = -dbValue
            rectF.top = if (rectF.top > -5F) -5F else rectF.top
        }
    }

    private fun calculateRenderData(drawArea: Rect) {
        val perGap = drawArea.width().toFloat() / (renderColumns.size * (gapRatio + 1) + 1)
        renderColumns.forEachIndexed { index, rect ->
            rect.left = ((index + 1) * (1 + gapRatio) - gapRatio) * perGap
            rect.right = rect.left + gapRatio * perGap
        }
    }

    override fun render(canvas: Canvas) {
        canvas.save()
        canvas.translate(lastDrawArea.left.toFloat(), lastDrawArea.bottom.toFloat())
        renderColumns.forEach { canvas.drawRoundRect(it, radius, radius, paint) }
        canvas.restore()
    }
}
