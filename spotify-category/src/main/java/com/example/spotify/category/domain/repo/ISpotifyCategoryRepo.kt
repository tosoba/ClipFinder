package com.example.spotify.category.domain.repo

import com.clipfinder.core.spotify.model.ISpotifySimplePlaylist
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.there.domain.entity.spotify.PlaylistEntity
import io.reactivex.Single

interface ISpotifyCategoryRepo {
    fun getPlaylistsForCategory(categoryId: String, offset: Int): Single<Resource<Paged<List<ISpotifySimplePlaylist>>>>
}
