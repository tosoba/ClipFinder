package com.clipfinder.core.spotify.model

interface ISpotifyAlbum {
    val href: String
    val images: List<ISpotifyImage>
    val id: String
    val name: String
    val artists: List<ISpotifySimplifiedArtist>
    val albumType: String
}
