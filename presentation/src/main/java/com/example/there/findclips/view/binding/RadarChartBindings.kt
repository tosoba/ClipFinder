package com.example.there.findclips.view.binding

import android.databinding.BindingAdapter
import android.os.Build
import com.example.there.findclips.view.radarchart.RadarChartView
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData

@BindingAdapter("radarChartView")
fun bindRadarChartView(radarChart: RadarChart, view: RadarChartView) = with(radarChart) {
    setBackgroundColor(view.backgroundColor)
    description.isEnabled = view.descriptionEnabled
    webLineWidth = view.webLineWidth
    webColor = view.webColor
    webLineWidthInner = view.webLineWidthInner
    webColorInner = view.webColorInner
    webAlpha = view.webAlpha
    marker = view.markerView.apply {
        chartView = this@with
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        isNestedScrollingEnabled = view.nestedScrollingEnabled
    }

    with(xAxis) {
        typeface = view.xAxisView.typeface
        textSize = view.xAxisView.textSize
        textColor = view.xAxisView.textColor
        xOffset = view.xAxisView.xOffset
        yOffset = view.xAxisView.yOffset
        view.xAxisView.valueFormatter?.let {
            this.valueFormatter = it
        }
        axisMinimum = view.xAxisView.axisMinimum
        axisMaximum = view.xAxisView.axisMaximum
        setDrawLabels(view.xAxisView.drawLabels)
    }

    with(yAxis) {
        typeface = view.yAxisView.typeface
        textSize = view.yAxisView.textSize
        textColor = view.yAxisView.textColor
        xOffset = view.yAxisView.xOffset
        yOffset = view.yAxisView.yOffset
        view.yAxisView.valueFormatter?.let {
            this.valueFormatter = it
        }
        axisMinimum = view.yAxisView.axisMinimum
        axisMaximum = view.yAxisView.axisMaximum
        setDrawLabels(view.yAxisView.drawLabels)
    }

    legend.isEnabled = view.legendEnabled
}


@BindingAdapter("radarChartData")
fun bindRadarChartData(radarChart: RadarChart, data: RadarData?): Unit = with(radarChart) {
    data?.let {
        setData(it)
        invalidate()
        requestLayout()
    }
}