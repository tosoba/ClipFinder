package com.example.spotifycategory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.Playlist
import com.example.spotifyapi.SpotifyApi
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.spotify.DeleteCategory
import com.example.there.domain.usecase.spotify.GetPlaylistsForCategory
import com.example.there.domain.usecase.spotify.InsertCategory
import com.example.there.domain.usecase.spotify.IsCategorySaved
import javax.inject.Inject


class CategoryViewModel @Inject constructor(
        private val getPlaylistsForCategory: GetPlaylistsForCategory,
        private val insertCategory: InsertCategory,
        private val deleteCategory: DeleteCategory,
        private val isCategorySaved: IsCategorySaved
) : BaseViewModel() {

    val viewState: CategoryViewState = CategoryViewState()

    val playlists: MutableLiveData<List<Playlist>> = MutableLiveData()

    fun loadPlaylists(category: Category) {
        loadData(category.id)
        loadCategoryFavouriteState(category)
    }

    private var currentOffset = 0
    private var totalItems = 0

    fun loadData(categoryId: String, shouldClear: Boolean = false) {
        if (currentOffset == 0 || (currentOffset < totalItems) || shouldClear) {
            if (shouldClear) {
                currentOffset = 0
                totalItems = 0
            }

            viewState.loadingInProgress.set(true)
            getPlaylistsForCategory(GetPlaylistsForCategory.Args(categoryId, currentOffset))
                    .takeSuccessOnly()
                    .doFinally { viewState.loadingInProgress.set(false) }
                    .subscribeAndDisposeOnCleared({
                        currentOffset = it.offset + SpotifyApi.DEFAULT_LIMIT
                        totalItems = it.totalItems
                        playlists.value = it.items.map(PlaylistEntity::ui)
                    }, ::onError)
        }
    }

    fun addFavouriteCategory(
            category: Category
    ) = insertCategory(category.domain)
            .subscribeAndDisposeOnCleared({ viewState.isSavedAsFavourite.set(true) }, { Log.e(javaClass.name, "Insert error.") })


    fun deleteFavouriteCategory(
            category: Category
    ) = deleteCategory(category.domain)
            .subscribeAndDisposeOnCleared({ viewState.isSavedAsFavourite.set(false) }, { Log.e(javaClass.name, "Delete error.") })

    private fun loadCategoryFavouriteState(
            category: Category
    ) = isCategorySaved(category.domain)
            .subscribe(viewState.isSavedAsFavourite::set)
}