package com.example.there.findclips.entities

import android.databinding.ObservableField
import com.example.there.domain.util.Duration
import com.example.there.findclips.util.*
import org.joda.time.Instant
import java.math.BigInteger

data class Video(
        val id: String,
        val channelId: String,
        val title: String,
        val description: String,
        val publishedAt: String,
        val thumbnailUrl: String,
        val duration: Duration,
        val viewCount: BigInteger,
        var channelThumbnailUrl: ObservableField<String> = ObservableField("")
) {
    val details: String
        get() = "$publishedAgo • ${viewCount.formattedString} views"

    private val publishedAgo: String
        get() {
            val publishedAtMillis = Instant.parse(publishedAt).millis
            val diff = System.currentTimeMillis() - publishedAtMillis
            val diffDurationMillis = org.joda.time.Duration.millis(diff)
            return when {
                diffDurationMillis.standardYears > 0 -> diffDurationMillis.standardYears.getPublishedAgoString("year")
                diffDurationMillis.standardMonths > 0 -> diffDurationMillis.standardMonths.getPublishedAgoString("month")
                diffDurationMillis.standardWeeks > 0 -> diffDurationMillis.standardWeeks.getPublishedAgoString("week")
                diffDurationMillis.standardDays > 0 -> diffDurationMillis.standardDays.getPublishedAgoString("day")
                diffDurationMillis.standardHours > 0 -> diffDurationMillis.standardHours.getPublishedAgoString("hour")
                diffDurationMillis.standardMinutes > 0 -> diffDurationMillis.standardMinutes.getPublishedAgoString("minute")
                diffDurationMillis.standardSeconds > 0 -> diffDurationMillis.standardSeconds.getPublishedAgoString("second")
                else -> "Just now"
            }
        }
}