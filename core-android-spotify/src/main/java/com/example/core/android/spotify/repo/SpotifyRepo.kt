package com.example.core.android.spotify.repo

import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.spotify.api.endpoint.AlbumEndpoints
import com.clipfinder.spotify.api.endpoint.ArtistEndpoints
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import io.reactivex.Single

class SpotifyRepo(
    private val albumEndpoints: AlbumEndpoints,
    private val artistEndpoints: ArtistEndpoints
) : ISpotifyRepo {

    override fun getAlbum(id: String): Single<Resource<ISpotifySimplifiedAlbum>> = albumEndpoints
        .getAnAlbum(id = id)
        .mapToResource { this }

    override fun getArtists(ids: List<String>): Single<Resource<List<ISpotifyArtist>>> = artistEndpoints
        .getMultipleArtists(ids = ids.joinToString(separator = ","))
        .mapToResource { artists }
}