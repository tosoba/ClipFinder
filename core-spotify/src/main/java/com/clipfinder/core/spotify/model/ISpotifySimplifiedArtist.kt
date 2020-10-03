package com.clipfinder.core.spotify.model

interface ISpotifySimplifiedArtist {
    val id: String
    val name: String
    val href: String
}

interface ISpotifyArtist : ISpotifySimplifiedArtist {
    val images: List<ISpotifyImage>
    val popularity: Int
}
