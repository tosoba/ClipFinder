package com.example.spotify.category.data

import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.clipfinder.spotify.api.endpoint.BrowseEndpoints
import com.example.core.SpotifyDefaults
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.retrofit.mapToResource
import com.example.spotify.category.domain.repo.ISpotifyCategoryRepo
import io.reactivex.Single

class SpotifyCategoryRepo(
    private val browseEndpoints: BrowseEndpoints,
    private val preferences: SpotifyPreferences
) : ISpotifyCategoryRepo {

    override fun getPlaylistsForCategory(
        categoryId: String,
        offset: Int
    ): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>> = browseEndpoints.getACategoriesPlaylists(
        categoryId = categoryId,
        offset = offset,
        country = preferences.country
    ).mapToResource {
        Paged<List<ISpotifySimplifiedPlaylist>>(
            contents = playlists.items,
            offset = playlists.offset + SpotifyDefaults.LIMIT,
            total = playlists.total
        )
    }
}
