package com.example.spotify.account.saved.data

import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.spotify.api.endpoint.LibraryEndpoints
import com.clipfinder.spotify.api.model.SavedAlbumObject
import com.clipfinder.spotify.api.model.SavedTrackObject
import com.example.core.SpotifyDefaults
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.ext.mapToResource
import com.example.core.ext.toPaged
import com.example.spotify.account.saved.domain.repo.ISpotifyAccountSavedRepo
import io.reactivex.Single

class SpotifyAccountSavedRepo(
    private val libraryEndpoints: LibraryEndpoints
) : ISpotifyAccountSavedRepo {

    override fun getCurrentUsersSavedTracks(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = libraryEndpoints
        .getUsersSavedTracks(offset = offset)
        .mapToResource { toPaged(mapItems = SavedTrackObject::track) }

    override fun getCurrentUsersSavedAlbums(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = libraryEndpoints
        .getUsersSavedAlbums(offset = offset)
        .mapToResource { toPaged(mapItems = SavedAlbumObject::album) }
}
