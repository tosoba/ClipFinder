package com.example.spotify.account.playlist.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.mapper.domain
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.account.playlist.domain.repo.ISpotifyAccountPlaylistsRepo
import com.example.spotifyapi.SpotifyPersonalizationApi
import com.example.spotifyapi.models.Playlist
import com.example.there.domain.entity.spotify.PlaylistEntity
import io.reactivex.Single

class SpotifyAccountPlaylistsRepo(
    private val auth: SpotifyAuth,
    private val personalizationApi: SpotifyPersonalizationApi
) : ISpotifyAccountPlaylistsRepo {

    override fun getCurrentUsersPlaylists(
        offset: Int
    ): Single<Resource<Paged<List<PlaylistEntity>>>> = auth.withTokenSingle(true) { token ->
        personalizationApi.getCurrentUsersPlaylists(authorization = token, offset = offset)
            .mapToResource {
                Paged(
                    contents = items.map(Playlist::domain),
                    offset = offset + SpotifyDefaults.LIMIT,
                    total = totalItems
                )
            }
    }
}
