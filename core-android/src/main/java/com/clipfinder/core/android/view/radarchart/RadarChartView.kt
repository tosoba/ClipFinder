package com.clipfinder.core.android.view.radarchart

import android.graphics.Color
import android.graphics.Typeface
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.formatter.IAxisValueFormatter

class RadarChartView(
    val data: RadarData,
    val xAxisView: RadarChartAxisView,
    val yAxisView: RadarChartAxisView,
    val backgroundColor: Int = Color.TRANSPARENT,
    val descriptionEnabled: Boolean = false,
    val webLineWidth: Float = 1f,
    val webColor: Int = Color.LTGRAY,
    val webLineWidthInner: Float = 1f,
    val webColorInner: Int = Color.LTGRAY,
    val webAlpha: Int = 100,
    val markerView: _root_ide_package_.com.clipfinder.core.android.view.radarchart.RadarMarkerView,
    val legendEnabled: Boolean = false,
    val nestedScrollingEnabled: Boolean = true
)

class RadarChartAxisView(
    val typeface: Typeface,
    val textSize: Float = 12f,
    val xOffset: Float = 0f,
    val yOffset: Float = 0f,
    val valueFormatter: IAxisValueFormatter? = null,
    val textColor: Int = Color.WHITE,
    val axisMinimum: Float = 0f,
    val axisMaximum: Float = 0f,
    val drawLabels: Boolean = true
)
