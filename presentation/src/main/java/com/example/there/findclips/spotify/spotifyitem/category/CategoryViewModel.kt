package com.example.there.findclips.spotify.spotifyitem.category

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.spotify.DeleteCategory
import com.example.there.domain.usecase.spotify.GetPlaylistsForCategory
import com.example.there.domain.usecase.spotify.InsertCategory
import com.example.there.domain.usecase.spotify.IsCategorySaved
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.spotify.Category
import com.example.there.findclips.model.entity.spotify.Playlist
import com.example.there.findclips.model.mapper.spotify.domain
import com.example.there.findclips.model.mapper.spotify.ui
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
            getPlaylistsForCategory.execute(GetPlaylistsForCategory.Input(categoryId, currentOffset))
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
    ) = insertCategory.execute(category.domain)
            .subscribeAndDisposeOnCleared({ viewState.isSavedAsFavourite.set(true) }, { Log.e(javaClass.name, "Insert error.") })


    fun deleteFavouriteCategory(
            category: Category
    ) = deleteCategory.execute(category.domain)
            .subscribeAndDisposeOnCleared({ viewState.isSavedAsFavourite.set(false) }, { Log.e(javaClass.name, "Delete error.") })

    private fun loadCategoryFavouriteState(
            category: Category
    ) = isCategorySaved.execute(category.domain)
            .subscribe(viewState.isSavedAsFavourite::set)
}