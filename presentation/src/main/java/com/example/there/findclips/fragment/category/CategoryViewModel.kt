package com.example.there.findclips.fragment.category

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.GetAccessToken
import com.example.there.domain.usecase.spotify.GetPlaylistsForCategory
import com.example.there.domain.usecase.spotify.InsertCategory
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.model.mapper.CategoryEntityMapper
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
import javax.inject.Inject


class CategoryViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getPlaylistsForCategory: GetPlaylistsForCategory,
        private val insertCategory: InsertCategory
) : BaseSpotifyViewModel(getAccessToken) {

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

    private var currentOffset = 0
    private var totalItems = 0

    private fun loadData(accessTokenEntity: AccessTokenEntity, categoryId: String) {
        if (currentOffset == 0 || (currentOffset < totalItems)) {
            viewState.loadingInProgress.set(true)
            addDisposable(getPlaylistsForCategory.execute(accessTokenEntity, categoryId, currentOffset)
                    .doFinally { viewState.loadingInProgress.set(false) }
                    .subscribe({
                        currentOffset = it.offset + SpotifyApi.DEFAULT_LIMIT.toInt()
                        totalItems = it.totalItems
                        playlists.value = it.items.map(PlaylistEntityMapper::mapFrom)
                    }, ::onError))
        }
    }

    fun addFavouriteCategory(category: Category) {
        addDisposable(insertCategory.execute(CategoryEntityMapper.mapBack(category)).subscribe({}, { Log.e(javaClass.name, "Insert error.") }))
    }
}