package com.example.there.findclips.activities.category

import android.arch.lifecycle.MutableLiveData
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetPlaylistsForCategory
import com.example.there.findclips.base.BaseSpotifyViewModel
import com.example.there.findclips.model.entities.Playlist
import com.example.there.findclips.model.mappers.PlaylistEntityMapper


class CategoryViewModel(getAccessToken: GetAccessToken,
                        private val getPlaylistsForCategory: GetPlaylistsForCategory) : BaseSpotifyViewModel(getAccessToken) {

    val viewState: CategoryViewState = CategoryViewState()

    val playlists: MutableLiveData<List<Playlist>> = MutableLiveData()

    fun loadPlaylists(accessToken: AccessTokenEntity?, categoryId: String) {
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessToken, categoryId)
        } else {
            loadAccessToken { loadData(it, categoryId) }
        }
    }

    private fun loadData(accessTokenEntity: AccessTokenEntity, categoryId: String) {
        viewState.loadingInProgress.set(true)
        addDisposable(getPlaylistsForCategory.execute(accessTokenEntity, categoryId)
                .doFinally { viewState.loadingInProgress.set(false) }
                .subscribe({
                    playlists.value = it.map(PlaylistEntityMapper::mapFrom).sortedBy { it.name }
                }, this::onError))
    }
}