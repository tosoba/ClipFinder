package com.example.spotify.category.domain.repo

import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.example.core.model.Paged
import com.example.core.model.Resource
import io.reactivex.Single

interface ISpotifyCategoryRepo {
    fun getPlaylistsForCategory(categoryId: String, offset: Int): Single<Resource<Paged<List<ISpotifySimplifiedPlaylist>>>>
}
