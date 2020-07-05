package com.example.videosrepo.util

import com.example.youtubeapi.model.ThumbnailsApiModel

val ThumbnailsApiModel.urlHigh: String
    get() = maxres?.url ?: high?.url ?: medium?.url ?: default?.url ?: fallbackURL

val ThumbnailsApiModel.urlMedium: String
    get() = medium?.url ?: high?.url ?: default?.url ?: fallbackURL

private val fallbackURL: String
    get() = "https://i.ytimg.com/vi/T0Jqdjbed40/sddefault.jpg"
