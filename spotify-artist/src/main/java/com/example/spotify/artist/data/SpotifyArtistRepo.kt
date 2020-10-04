package com.example.spotify.artist.data

import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.model.ISpotifySimplifiedAlbum
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.clipfinder.spotify.api.endpoint.ArtistEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.artist.domain.repo.ISpotifyArtistRepo
import io.reactivex.Single

class SpotifyArtistRepo(
    private val artistEndpoints: ArtistEndpoints,
    private val preferences: SpotifyPreferences
) : ISpotifyArtistRepo {

    override fun getAlbumsFromArtist(
        artistId: String, offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedAlbum>>>> = artistEndpoints
        .getAnArtistsAlbums(id = artistId, offset = offset)
        .mapToResource {
            Paged<List<ISpotifySimplifiedAlbum>>(
                contents = items,
                offset = this.offset + SpotifyDefaults.LIMIT,
                total = total
            )
        }

    override fun getRelatedArtists(
        artistId: String
    ): Single<Resource<List<ISpotifyArtist>>> = artistEndpoints
        .getAnArtistsRelatedArtists(id = artistId)
        .mapToResource { artists }


    override fun getTopTracksFromArtist(
        artistId: String
    ): Single<Resource<List<ISpotifyTrack>>> = artistEndpoints
        .getAnArtistsTopTracks(id = artistId, market = preferences.country)
        .mapToResource { tracks }
}
