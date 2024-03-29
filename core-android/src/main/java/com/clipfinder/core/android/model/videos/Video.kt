package com.clipfinder.core.android.model.videos

import android.os.Parcelable
import android.view.View
import androidx.databinding.ObservableField
import com.clipfinder.core.android.R
import com.clipfinder.core.android.VideoItemBindingModel_
import com.clipfinder.core.android.util.ext.standardMonths
import com.clipfinder.core.android.util.ext.standardWeeks
import com.clipfinder.core.android.util.ext.standardYears
import com.clipfinder.core.android.view.imageview.ImageViewSrc
import com.clipfinder.core.ext.formattedString
import com.clipfinder.core.ext.getPublishedAgoString
import kotlinx.android.parcel.Parcelize
import org.joda.time.Duration
import org.joda.time.Instant
import java.math.BigInteger

@Parcelize
data class Video(
    val id: String,
    val channelId: String = "",
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
        get() =
            ImageViewSrc.with(
                thumbnailUrl,
                R.drawable.video_placeholder,
                R.drawable.error_placeholder
            )

    val details: String
        get() = "$publishedAgo • ${viewCount.formattedString} views"

    private val publishedAgo: String
        get() {
            val difference = System.currentTimeMillis() - Instant.parse(publishedAt).millis
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

fun Video.clickableListItem(itemClicked: (View) -> Unit): VideoItemBindingModel_ =
    VideoItemBindingModel_().id(id).itemClicked(itemClicked).video(this)
