package com.clipfinder.core.spotify.model

interface ISpotifyPrivateUser {
    val id: String
    val displayName: String?
    val images: List<ISpotifyImage>?
}
