package com.example.there.data.util

import com.example.there.data.entity.spotify.IconData
import com.example.there.data.entity.videos.ThumbnailsData
import io.reactivex.Maybe
import io.reactivex.Single

private const val DEFAULT_ICON_URL = "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"

val List<IconData>.firstIconUrlOrDefault: String
    get() = getOrNull(0)?.url ?: DEFAULT_ICON_URL

val List<IconData>.secondIconUrlOrDefault: String
    get() = getOrNull(1)?.url ?: getOrNull(0)?.url ?: DEFAULT_ICON_URL

val ThumbnailsData.urlHigh: String
    get() = maxres?.url ?: high?.url ?: medium?.url ?: default?.url ?: fallbackURL

val ThumbnailsData.urlMedium: String
    get() = medium?.url ?: high?.url ?: default?.url ?: fallbackURL

val ThumbnailsData.fallbackURL: String
    get() = "https://i.ytimg.com/vi/T0Jqdjbed40/sddefault.jpg"

fun <T> Maybe<T>.mapToSingleBoolean(): Single<Boolean> = this
        .map { true }
        .defaultIfEmpty(false)
        .toSingle()