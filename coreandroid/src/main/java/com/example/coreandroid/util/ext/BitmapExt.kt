package com.example.coreandroid.util.ext

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.AsyncTask
import android.support.v7.graphics.Palette


fun Bitmap.generateColorGradient(
        onGenerated: (GradientDrawable) -> Unit
): AsyncTask<Bitmap, Void, Palette> = Palette.from(this).generate { palette ->
    palette?.let {
        onGenerated(GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                        it.vibrantSwatch?.rgb
                                ?: it.lightVibrantSwatch?.rgb
                                ?: it.lightMutedSwatch?.rgb
                                ?: it.mutedSwatch?.rgb ?: Color.TRANSPARENT,
                        it.dominantSwatch?.rgb ?: it.darkVibrantSwatch?.rgb
                        ?: it.darkMutedSwatch?.rgb ?: it.mutedSwatch?.rgb ?: Color.TRANSPARENT
                )
        ).apply { cornerRadius = 0f })
    }
}
