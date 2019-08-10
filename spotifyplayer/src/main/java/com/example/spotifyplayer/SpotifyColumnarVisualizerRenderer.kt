package com.example.spotifyplayer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import me.bogerchan.niervisualizer.renderer.IRenderer
import kotlin.math.log10
import kotlin.math.min

class SpotifyColumnarVisualizerRenderer(paint: Paint) : IRenderer {

    private val mPaint: Paint = paint
    private val mLastDrawArea = Rect()
    private lateinit var mRenderColumns: Array<RectF>
    // per column' width equals to twice of gap
    private val mGapRatio = 0.7F
    private val mRadius = 10F

    override fun onStart(captureSize: Int) {
        mRenderColumns = Array(min(36, captureSize)) { RectF(0F, -5F, 0F, 0F) }
        mLastDrawArea.set(0, 0, 0, 0)
    }

    override fun onStop() {

    }

    override fun getInputDataType() = IRenderer.DataType.WAVE

    override fun calculate(drawArea: Rect, data: ByteArray) {
        if (drawArea != mLastDrawArea) {
            calculateRenderData(drawArea)
            mLastDrawArea.set(drawArea)
        }
        updateWave(data)
    }

    private fun updateWave(data: ByteArray) {
        for (i in 0 until min(data.size / 2, mRenderColumns.size)) {
            // Calculate dbValue
            val rfk = data[i]
            val ifk = data[i + 1]
            val magnitude = (rfk * rfk + ifk * ifk).toFloat()
            val dbValue = 75 * log10(magnitude.toDouble()).toFloat()
            val rectF = mRenderColumns[i]
            rectF.top = -dbValue
            rectF.top = if (rectF.top > -5F) -5F else rectF.top
        }
    }

    private fun calculateRenderData(drawArea: Rect) {
        val perGap = drawArea.width().toFloat() / (mRenderColumns.size * (mGapRatio + 1) + 1)
        mRenderColumns.forEachIndexed { index, rect ->
            rect.left = ((index + 1) * (1 + mGapRatio) - mGapRatio) * perGap
            rect.right = rect.left + mGapRatio * perGap
        }
    }

    override fun render(canvas: Canvas) {
        canvas.save()
        canvas.translate(mLastDrawArea.left.toFloat(), mLastDrawArea.bottom.toFloat())
        mRenderColumns.forEach {
            canvas.drawRoundRect(it, mRadius, mRadius, mPaint)
        }
        canvas.restore()
    }
}