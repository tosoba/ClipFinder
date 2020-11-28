package com.clipfinder.core.youtube.ext

import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.ThumbnailDetails
import java.util.regex.Pattern

val ThumbnailDetails.highestResUrl: String
    get() = maxres?.url ?: high?.url ?: medium?.url ?: default?.url ?: fallbackURL

val ThumbnailDetails.mediumUrl: String
    get() = medium?.url ?: high?.url ?: default?.url ?: fallbackURL

private val fallbackURL: String
    get() = "https://i.ytimg.com/vi/T0Jqdjbed40/sddefault.jpg"

val SearchResult.isValid: Boolean
    get() = id?.videoId != null
        && snippet?.title != null
        && snippet?.description != null
        && snippet?.publishedAt != null
        && snippet?.thumbnails != null

private val Long.timeString: String
    get() = if (this < 10) "0$this" else toString()

private fun convertDuration(isoString: String): String {
    fun toString(days: Long, hours: Long, minutes: Long, seconds: Long): String = when {
        days > 0 -> "${days.timeString}:${hours.timeString}:${minutes.timeString}:${seconds.timeString}"
        hours > 0 -> "${hours.timeString}:${minutes.timeString}:${seconds.timeString}"
        else -> "${minutes.timeString}:${seconds.timeString}"
    }

    val matcherDay = Pattern.compile("P(\\d+)DT(\\d+)H(\\d+)M(\\d+)S").matcher(isoString)
    if (matcherDay.matches()) {
        return toString(matcherDay.group(1).toLong(), matcherDay.group(2).toLong(), matcherDay.group(3).toLong(), matcherDay.group(4).toLong())
    }

    val matcherHour = Pattern.compile("PT(\\d+)H(\\d+)M(\\d+)S").matcher(isoString)
    if (matcherHour.matches()) {
        return toString(0L, matcherHour.group(1).toLong(), matcherHour.group(2).toLong(), matcherHour.group(3).toLong())
    }

    val matcherMinute = Pattern.compile("PT(\\d+)M(\\d+)S").matcher(isoString)
    if (matcherMinute.matches()) {
        return toString(0L, 0L, matcherMinute.group(1).toLong(), matcherMinute.group(2).toLong())
    }

    val matcherSecond = Pattern.compile("PT(\\d+)S").matcher(isoString)
    if (matcherSecond.matches()) {
        return toString(0L, 0L, 0L, matcherSecond.group(1).toLong())
    }

    return toString(0L, 0L, 0L, 0L)
}
