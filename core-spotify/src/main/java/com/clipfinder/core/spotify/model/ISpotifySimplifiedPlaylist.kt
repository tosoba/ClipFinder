package com.clipfinder.core.spotify.model

interface ISpotifySimplifiedPlaylist {
    val href: String
    val description: String?
    val id: String
    val images: List<ISpotifyImage>
    val name: String
    val uri: String
}
