package com.example.there.findclips.util

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.widget.EditText
import android.widget.SearchView
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.findclips.FindClipsApp
import com.example.there.findclips.main.MainActivity
import com.example.there.findclips.main.MainRouter
import org.joda.time.DateTimeConstants
import org.joda.time.Duration
import java.math.BigInteger
import java.util.*


val Activity.app: FindClipsApp
    get() = this.application as FindClipsApp

private const val SHARED_PREFERENCES_KEY = "SHARED_PREFERENCES_KEY"
private const val PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN"
private const val PREF_KEY_ACCESS_TOKEN_TIMESTAMP = "PREF_KEY_ACCESS_TOKEN_TIMESTAMP"

val Activity.accessToken: AccessTokenEntity?
    get() {
        val preferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val token = preferences?.getString(PREF_KEY_ACCESS_TOKEN, null)
        val timestamp = preferences?.getLong(PREF_KEY_ACCESS_TOKEN_TIMESTAMP, 0L)
        return if (token == null || timestamp == null) null
        else AccessTokenEntity(token, timestamp)
    }

fun Activity.saveAccessToken(accessToken: AccessTokenEntity) {
    val preferences = getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
    with(preferences.edit()) {
        putString(PREF_KEY_ACCESS_TOKEN, accessToken.token)
        putLong(PREF_KEY_ACCESS_TOKEN_TIMESTAMP, accessToken.timestamp)
        apply()
    }
}

fun Throwable.messageOrDefault(message: String = "Unknown error."): String {
    return this.message ?: message
}

val Context.screenOrientation: Int
    get() = resources.configuration.orientation

private val suffixes = TreeMap<Long, String>().apply {
    put(1_000L, "K")
    put(1_000_000L, "M")
    put(1_000_000_000L, "G")
}

val BigInteger.formattedString: String
    get() {
        val value = this.toLong()
        if (value < 0) return "0"
        if (value < 1000) return value.toString()

        val e = suffixes.floorEntry(value)
        val divideBy = e.key
        val suffix = e.value

        val truncated = value / (divideBy / 10)
        val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
        return if (hasDecimal) "${truncated / 10.0}$suffix" else "${truncated / 10}$suffix"
    }

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

fun Long.getPublishedAgoString(prefix: String): String {
    return if (this == 1L) {
        "$this $prefix ago"
    } else {
        "$this ${prefix}s ago"
    }
}

fun SearchView.setTextColors(textColor: Int = Color.WHITE, hintTextColor: Int = Color.LTGRAY) {
    val searchSrcTextId = resources.getIdentifier("android:id/search_src_text", null, null)
    val searchEditText = findViewById<EditText>(searchSrcTextId)
    searchEditText.setTextColor(textColor)
    searchEditText.setHintTextColor(hintTextColor)
}

val Fragment.mainActivity: MainActivity?
    get() = activity as? MainActivity

val Fragment.mainRouter: MainRouter?
    get() = mainActivity?.router