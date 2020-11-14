package com.example.spotify.account.saved.data

import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.spotify.api.endpoint.LibraryEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.ext.mapToResource
import com.example.spotify.account.saved.domain.repo.ISpotifyAccountSavedRepo
import io.reactivex.Single

class SpotifyAccountSavedRepo(
    private val libraryEndpoints: LibraryEndpoints
) : ISpotifyAccountSavedRepo {

    override fun getCurrentUsersSavedTracks(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = libraryEndpoints
        .getUsersSavedTracks(offset = offset)
        .mapToResource {
            Paged<List<ISpotifyTrack>>(
                contents = items.map { it.track },
                offset = offset + SpotifyDefaults.LIMIT,
                total = total
            )
        }

    override fun getCurrentUsersSavedAlbums(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = libraryEndpoints
        .getUsersSavedAlbums(offset = offset)
        .mapToResource {
            Paged<List<ISpotifySimplifiedAlbum>>(
                contents = items.map { it.album },
                offset = offset + SpotifyDefaults.LIMIT,
                total = total
            )
        }
}
