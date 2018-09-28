package com.example.there.findclips.fragment.spotifyitem.category

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.*
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.model.mapper.CategoryEntityMapper
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
import javax.inject.Inject


class CategoryViewModel @Inject constructor(
        getAccessToken: GetAccessToken,
        private val getPlaylistsForCategory: GetPlaylistsForCategory,
        private val insertCategory: InsertCategory,
        private val deleteCategory: DeleteCategory,
        private val isCategorySaved: IsCategorySaved
) : BaseSpotifyViewModel(getAccessToken) {

    val viewState: CategoryViewState = CategoryViewState()

    val playlists: MutableLiveData<List<Playlist>> = MutableLiveData()

    fun loadPlaylists(accessToken: AccessTokenEntity?, category: Category) {
        if (accessToken != null && accessToken.isValid) {
            accessTokenLiveData.value = accessToken
            loadData(accessToken, category.id)
            loadCategoryFavouriteState(category)
        } else {
            loadAccessToken {
                loadData(it, category.id)
                loadCategoryFavouriteState(category)
            }
        }
    }

    private var currentOffset = 0
    private var totalItems = 0

    fun loadData(accessTokenEntity: AccessTokenEntity, categoryId: String, shouldClear: Boolean = false) {
        if (currentOffset == 0 || (currentOffset < totalItems) || shouldClear) {
            if (shouldClear) {
                currentOffset = 0
                totalItems = 0
            }

            viewState.loadingInProgress.set(true)
            addDisposable(getPlaylistsForCategory.execute(accessTokenEntity, categoryId, currentOffset)
                    .doFinally { viewState.loadingInProgress.set(false) }
                    .subscribe({
                        currentOffset = it.offset + SpotifyApi.DEFAULT_LIMIT
                        totalItems = it.totalItems
                        playlists.value = it.items.map(PlaylistEntityMapper::mapFrom)
                    }, ::onError))
        }
    }

    fun addFavouriteCategory(
            category: Category
    ) = addDisposable(insertCategory.execute(CategoryEntityMapper.mapBack(category))
            .subscribe({ viewState.isSavedAsFavourite.set(true) }, { Log.e(javaClass.name, "Insert error.") }))


    fun deleteFavouriteCategory(
            category: Category
    ) = addDisposable(deleteCategory.execute(CategoryEntityMapper.mapBack(category))
            .subscribe({ viewState.isSavedAsFavourite.set(false) }, { Log.e(javaClass.name, "Delete error.") }))

    private fun loadCategoryFavouriteState(
            category: Category
    ) = addDisposable(isCategorySaved.execute(CategoryEntityMapper.mapBack(category))
            .subscribe({ viewState.isSavedAsFavourite.set(it) }, {}))
}