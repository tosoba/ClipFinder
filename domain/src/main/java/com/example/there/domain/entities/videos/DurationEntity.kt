package com.example.there.domain.entities.videos

import com.example.there.domain.util.timeString
import java.util.regex.Pattern

data class DurationEntity(private val isoString: String) {
    private val days: Long
    private val hours: Long
    private val minutes: Long
    private val seconds: Long

    init {
        val matcher1Day = Pattern.compile("P(\\d+)DT(\\d+)H(\\d+)M(\\d+)S").matcher(isoString)
        if (matcher1Day.matches()) {
            days = matcher1Day.group(1).toLong()
            hours = matcher1Day.group(2).toLong()
            minutes = matcher1Day.group(3).toLong()
            seconds = matcher1Day.group(4).toLong()
        } else {
            val matcher1Hour = Pattern.compile("PT(\\d+)H(\\d+)M(\\d+)S").matcher(isoString)
            if (matcher1Hour.matches()) {
                days = 0L
                hours = matcher1Hour.group(1).toLong()
                minutes = matcher1Hour.group(2).toLong()
                seconds = matcher1Hour.group(3).toLong()
            } else {
                val matcher1Minute = Pattern.compile("PT(\\d+)M(\\d+)S").matcher(isoString)
                if (matcher1Minute.matches()) {
                    days = 0L
                    hours = 0L
                    minutes = matcher1Minute.group(1).toLong()
                    seconds = matcher1Minute.group(2).toLong()
                } else {
                    val matcher1Second = Pattern.compile("PT(\\d+)S").matcher(isoString)
                    if (matcher1Second.matches()) {
                        days = 0L
                        hours = 0L
                        minutes = 0L
                        seconds = matcher1Second.group(1).toLong()
                    } else {
                        days = 0L
                        hours = 0L
                        minutes = 0L
                        seconds = 0L
                    }
                }
            }
        }
    }

    override fun toString(): String = when {
        days > 0 -> "${days.timeString}:${hours.timeString}:${minutes.timeString}:${seconds.timeString}"
        hours > 0 -> "${hours.timeString}:${minutes.timeString}:${seconds.timeString}"
        else -> "${minutes.timeString}:${seconds.timeString}"
    }
}