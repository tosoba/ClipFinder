package com.example.core.android.spotify.repo

import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.repo.ISpotifyRepo
import com.clipfinder.spotify.api.endpoint.AlbumEndpoints
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import io.reactivex.Single

class SpotifyRepo(private val albumEndpoints: AlbumEndpoints) : ISpotifyRepo {
    override fun getAlbum(id: String): Single<Resource<ISpotifySimplifiedAlbum>> = albumEndpoints
        .getAnAlbum(id = id)
        .mapToResource { this }
}