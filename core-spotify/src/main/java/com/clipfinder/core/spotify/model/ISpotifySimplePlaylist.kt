package com.clipfinder.core.spotify.model

interface ISpotifySimplePlaylist {
    val href: String
    val description: String?
    val id: String
    val images: List<IImage>
    val name: String
    val uri: String
}
