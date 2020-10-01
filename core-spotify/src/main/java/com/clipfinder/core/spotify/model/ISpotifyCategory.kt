package com.clipfinder.core.spotify.model

interface ISpotifyCategory {
    val href: String
    val icons: List<ISpotifyImage>
    val id: String
    val name: String
}
