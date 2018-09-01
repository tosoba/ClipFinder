package com.example.there.findclips.util.ext

import org.joda.time.DateTimeConstants
import org.joda.time.Duration

private val MILLIS_PER_MONTH: Long
    get() = DateTimeConstants.MILLIS_PER_DAY * 30L
private val MILLIS_PER_YEAR: Long
    get() = DateTimeConstants.MILLIS_PER_DAY * 365L
val Duration.standardWeeks: Long
    get() = millis / DateTimeConstants.MILLIS_PER_WEEK
val Duration.standardMonths: Long
    get() = millis / MILLIS_PER_MONTH
val Duration.standardYears: Long
    get() = millis / MILLIS_PER_YEAR