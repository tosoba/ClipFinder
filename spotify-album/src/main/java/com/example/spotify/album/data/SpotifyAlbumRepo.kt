package com.example.spotify.album.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapSuccessOrThrow
import com.example.core.retrofit.mapToResource
import com.example.spotify.album.domain.repo.ISpotifyAlbumRepo
import com.example.spotifyapi.SpotifyAlbumsApi
import com.example.spotifyapi.SpotifyTracksApi
import com.example.spotifyapi.models.Track
import com.example.spotifyapi.util.domain
import com.example.there.domain.entity.spotify.TrackEntity
import io.reactivex.Single

class SpotifyAlbumRepo(
    private val auth: SpotifyAuth,
    private val albumsApi: SpotifyAlbumsApi,
    private val tracksApi: SpotifyTracksApi
) : ISpotifyAlbumRepo {

    override fun getTracksFromAlbum(
        albumId: String,
        offset: Int
    ): Single<Resource<Paged<List<TrackEntity>>>> = auth.withTokenSingle { token ->
        albumsApi.getAlbumsTracks(
            authorization = token,
            id = albumId,
            offset = offset
        ).mapSuccessOrThrow {
            Paged(
                contents = items.joinToString(separator = ",") { it.id },
                offset = offset,
                total = total
            )
        }.flatMap { idsPage ->
            tracksApi.getTracks(
                authorization = token,
                ids = idsPage.contents
            ).mapToResource {
                Paged(
                    contents = result.map(Track::domain),
                    offset = idsPage.offset + SpotifyDefaults.LIMIT,
                    total = idsPage.total
                )
            }
        }
    }
}
