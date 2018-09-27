package com.example.there.findclips.model.entity

import android.annotation.SuppressLint
import android.databinding.ObservableField
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.*
import io.mironov.smuggler.AutoParcelable
import org.joda.time.Duration
import org.joda.time.Instant
import java.math.BigInteger

@SuppressLint("ParcelCreator")
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
) : AutoParcelable {

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

    companion object {
        val sortedListCallback: ObservableSortedList.Callback<Video> = object : ObservableSortedList.Callback<Video> {
            override fun compare(o1: Video, o2: Video): Int = -1
            override fun areItemsTheSame(item1: Video, item2: Video): Boolean = item1.id == item2.id
            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean = oldItem.id == newItem.id
        }
    }
}