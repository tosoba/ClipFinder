package com.example.coreandroid.model.videos

import android.databinding.ObservableField
import android.os.Parcelable
import com.example.core.util.ext.formattedString
import com.example.core.util.ext.getPublishedAgoString
import com.example.coreandroid.R
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.imageview.ImageViewSrc
import kotlinx.android.parcel.Parcelize
import org.joda.time.Duration
import org.joda.time.Instant
import java.math.BigInteger

@Parcelize
data class Video(
        val id: String,
        val channelId: String,
        val title: String,
        val description: String,
        val publishedAt: String,
        val thumbnailUrl: String,
        val duration: String,
        val viewCount: BigInteger,
        var channelThumbnailUrl: ObservableField<String> = ObservableField(""),
        var playlistId: Long? = null,
        var query: String? = null,
        var relatedVideoId: String? = null
) : Parcelable {

    val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(thumbnailUrl, R.drawable.video_placeholder, R.drawable.error_placeholder)

    val details: String
        get() = "$publishedAgo • ${viewCount.formattedString} views"

    private val publishedAgo: String
        get() {
            val publishedAtMillis = Instant.parse(publishedAt).millis
            val difference = System.currentTimeMillis() - publishedAtMillis
            with(Duration.millis(difference)) {
                return when {
                    standardYears > 0 -> standardYears.getPublishedAgoString("year")
                    standardMonths > 0 -> standardMonths.getPublishedAgoString("month")
                    standardWeeks > 0 -> standardWeeks.getPublishedAgoString("week")
                    standardDays > 0 -> standardDays.getPublishedAgoString("day")
                    standardHours > 0 -> standardHours.getPublishedAgoString("hour")
                    standardMinutes > 0 -> standardMinutes.getPublishedAgoString("minute")
                    standardSeconds > 0 -> standardSeconds.getPublishedAgoString("second")
                    else -> "Just now"
                }
            }
        }
}