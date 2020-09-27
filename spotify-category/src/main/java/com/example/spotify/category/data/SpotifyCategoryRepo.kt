package com.example.spotify.category.data

import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.api.SpotifyAuth
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.category.domain.repo.ISpotifyCategoryRepo
import com.example.spotifyapi.SpotifyBrowseApi
import com.example.spotifyapi.models.SimplePlaylist
import com.example.spotifyapi.util.domain
import com.example.there.domain.entity.spotify.PlaylistEntity
import io.reactivex.Single

class SpotifyCategoryRepo(
    private val browseApi: SpotifyBrowseApi,
    private val auth: SpotifyAuth,
    private val preferences: SpotifyPreferences
) : ISpotifyCategoryRepo {

    override fun getPlaylistsForCategory(
        categoryId: String,
        offset: Int
    ): Single<Resource<Paged<List<PlaylistEntity>>>> = auth.withTokenSingle { token ->
        browseApi.getPlaylistsForCategory(
            authorization = token,
            categoryId = categoryId,
            offset = offset,
            country = preferences.country
        ).mapToResource {
            Paged(
                contents = result.items.map(SimplePlaylist::domain),
                offset = result.offset + SpotifyDefaults.LIMIT,
                total = result.total
            )
        }
    }
}
