package com.clipfinder.core.android.spotify.visualizer.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.animation.LinearInterpolator
import me.bogerchan.niervisualizer.renderer.IRenderer
import me.bogerchan.niervisualizer.util.NierAnimator
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.sin

class CircularVisualizerRenderer(
    private val paint: Paint = getDefaultPaint(),
    private val divisions: Int = 4,
    private val type: Type = Type.TYPE_A,
    private val modulationStrength: Float = 0.4f,
    private val amplification: Float = 1f,
    private val animator: NierAnimator = getDefaultAnimator()
) : IRenderer {
    private var aggressive = 0.4f
    private var modulation = 0.0
    private var angleModulation = 0f
    private lateinit var fftPoints: FloatArray
    private val lastDrawArea = Rect()

    override fun onStart(captureSize: Int) {
        fftPoints = FloatArray(captureSize * 4)
        animator.start()
    }

    override fun onStop() {
        animator.stop()
    }

    override fun calculate(drawArea: Rect, data: ByteArray) {
        if (lastDrawArea != drawArea) {
            lastDrawArea.set(drawArea)
        }
        val drawHeight = drawArea.height()

        for (i in 0 until data.size / divisions) {
            val rfk = data[divisions * i]
            val ifk = data[divisions * i + 1]
            val magnitude = (rfk * rfk + ifk * ifk).toFloat()
            val dbValue =
                (75 * log10(magnitude.toDouble()).toFloat() * amplification).let {
                    if (it < 20f) 20f else it
                }
            val cartPoint =
                when (type) {
                    Type.TYPE_A ->
                        floatArrayOf((i * divisions).toFloat() / (data.size - 1), drawHeight / 2f)
                    Type.TYPE_B ->
                        floatArrayOf(
                            (i * divisions).toFloat() / (data.size - 1),
                            drawHeight / 2f - dbValue
                        )
                    Type.TYPE_A_AND_TYPE_B ->
                        floatArrayOf(
                            (i * divisions).toFloat() / (data.size - 1),
                            drawHeight / 2f - dbValue
                        )
                }

            val polarPoint = toPolar(cartPoint, drawArea)
            fftPoints[i * 4] = polarPoint[0]
            fftPoints[i * 4 + 1] = polarPoint[1]

            val cartPoint2 =
                when (type) {
                    Type.TYPE_A ->
                        floatArrayOf(
                            (i * divisions).toFloat() / (data.size - 1),
                            drawHeight / 2f + dbValue
                        )
                    Type.TYPE_B ->
                        floatArrayOf((i * divisions).toFloat() / (data.size - 1), drawHeight / 2f)
                    Type.TYPE_A_AND_TYPE_B ->
                        floatArrayOf(
                            (i * divisions).toFloat() / (data.size - 1),
                            drawHeight / 2f + dbValue
                        )
                }

            val polarPoint2 = toPolar(cartPoint2, drawArea)
            fftPoints[i * 4 + 2] = polarPoint2[0]
            fftPoints[i * 4 + 3] = polarPoint2[1]
        }
    }

    override fun render(canvas: Canvas) {
        canvas.save()
        canvas.rotate(
            animator.computeCurrentValue(),
            (lastDrawArea.left + lastDrawArea.right) / 2F,
            (lastDrawArea.top + lastDrawArea.bottom) / 2F
        )
        canvas.drawLines(fftPoints, paint)
        canvas.restore()
    }

    override fun getInputDataType(): IRenderer.DataType = IRenderer.DataType.FFT

    private fun toPolar(cartesian: FloatArray, rect: Rect): FloatArray {
        val cX = (rect.width() / 2).toDouble()
        val cY = (rect.height() / 2).toDouble()
        val angle = cartesian[0].toDouble() * 2.0 * Math.PI
        val radius =
            (rect.width() / 2 * (1 - aggressive) + aggressive * cartesian[1] / 2) *
                (1 - modulationStrength + modulationStrength * (1 + sin(modulation)) / 2)
        return floatArrayOf(
            (cX + radius * sin(angle + angleModulation)).toFloat(),
            (cY + radius * cos(angle + angleModulation)).toFloat()
        )
    }

    companion object {
        private fun getDefaultPaint(): Paint =
            Paint(Paint.ANTI_ALIAS_FLAG).apply {
                strokeWidth = 3f
                color = Color.parseColor("#e6ebfe")
            }

        private fun getDefaultAnimator(): NierAnimator =
            NierAnimator(
                interpolator = LinearInterpolator(),
                duration = 20000,
                values = floatArrayOf(0f, 360f)
            )
    }

    enum class Type {
        TYPE_A,
        TYPE_B,
        TYPE_A_AND_TYPE_B
    }
}
