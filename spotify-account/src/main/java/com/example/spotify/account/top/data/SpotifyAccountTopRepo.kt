package com.example.spotify.account.top.data

import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.spotify.api.endpoint.PersonalizationEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.ext.mapToResource
import com.example.core.ext.toPaged
import com.example.spotify.account.top.domain.repo.ISpotifyAccountTopRepo
import io.reactivex.Single

class SpotifyAccountTopRepo(
    private val personalizationEndpoints: PersonalizationEndpoints
) : ISpotifyAccountTopRepo {

    override fun getCurrentUsersTopTracks(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyTrack>>>> = personalizationEndpoints
        .getUsersTopTracks(offset = offset)
        .mapToResource { toPaged() }

    override fun getCurrentUsersTopArtists(
        offset: Int
    ): Single<Resource<Paged<List<ISpotifyArtist>>>> = personalizationEndpoints
        .getUsersTopArtists(offset = offset)
        .mapToResource { toPaged() }
}
