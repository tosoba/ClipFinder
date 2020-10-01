package com.clipfinder.core.spotify.ext

import com.clipfinder.core.spotify.model.ISpotifyImage

private const val spotifyLogoUrl = "https://developer.spotify.com/assets/branding-guidelines/icon3@2x.png"

fun Collection<ISpotifyImage>.firstImageUrl(
    fallbackUrl: String = spotifyLogoUrl
): String = elementAtOrNull(0)?.url ?: fallbackUrl

fun Collection<ISpotifyImage>.secondImageUrl(
    fallbackUrl: String = spotifyLogoUrl
): String = elementAtOrNull(1)?.url ?: fallbackUrl
