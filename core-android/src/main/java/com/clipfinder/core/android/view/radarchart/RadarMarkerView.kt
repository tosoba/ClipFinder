package com.clipfinder.core.android.view.radarchart

import android.content.Context
import android.graphics.Typeface
import android.widget.TextView
import com.clipfinder.core.android.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class RadarMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {
    private val tvContent: TextView = findViewById(R.id.tvContent)

    init {
        tvContent.typeface = Typeface.createFromAsset(context.assets, "OpenSans-Regular.ttf")
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the content
    // (user-interface)
    override fun refreshContent(e: Entry, highlight: Highlight) {
        tvContent.text = e.y.toString()
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF =
        MPPointF((-(width / 2)).toFloat(), (-height - 10).toFloat())
}
