package com.clipfinder.core.spotify.model

interface ISpotifyTrack {
    val id: String
    val name: String
    val album: ISpotifySimplifiedAlbum
    val artists: List<ISpotifySimplifiedArtist>
    val popularity: Int
    val trackNumber: Int
    val uri: String
    val durationMs: Int
    val previewUrl: String?
}
