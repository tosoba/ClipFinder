package com.clipfinder.core.youtube.ext

import com.google.api.services.youtube.model.ThumbnailDetails

val ThumbnailDetails.highUrl: String
    get() = maxres?.url ?: high?.url ?: medium?.url ?: default?.url ?: fallbackURL

val ThumbnailDetails.mediumUrl: String
    get() = medium?.url ?: high?.url ?: default?.url ?: fallbackURL

private val fallbackURL: String
    get() = "https://i.ytimg.com/vi/T0Jqdjbed40/sddefault.jpg"
