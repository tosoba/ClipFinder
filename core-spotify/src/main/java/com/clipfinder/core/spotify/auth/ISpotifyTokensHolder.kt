package com.clipfinder.core.spotify.auth

interface ISpotifyTokensHolder {
    var publicAccessToken: String?
    val privateAccessToken: String?
}