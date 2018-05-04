package com.example.there.findclips.category.fragment

import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.spotify.PlaylistsForCategoryUseCase
import com.example.there.findclips.base.BaseSpotifyViewModel
import com.example.there.findclips.mappers.PlaylistEntityMapper


class CategoryViewModel(accessTokenUseCase: AccessTokenUseCase,
                        private val playlistsForCategoryUseCase: PlaylistsForCategoryUseCase) : BaseSpotifyViewModel(accessTokenUseCase) {

    val viewState: CategoryFragmentViewState = CategoryFragmentViewState()

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
        addDisposable(playlistsForCategoryUseCase.loadPlaylistsForCategory(accessTokenEntity, categoryId)
                .doFinally { viewState.loadingInProgress.set(false) }
                .subscribe({
                    viewState.playlists.addAll(it.map(PlaylistEntityMapper::mapFrom).sortedBy { it.name })
                }, this::onError))
    }
}