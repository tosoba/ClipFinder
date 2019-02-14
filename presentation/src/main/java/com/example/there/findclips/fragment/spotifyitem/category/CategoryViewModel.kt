package com.example.there.findclips.fragment.spotifyitem.category

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.there.data.api.spotify.SpotifyApi
import com.example.there.domain.usecase.spotify.DeleteCategory
import com.example.there.domain.usecase.spotify.GetPlaylistsForCategory
import com.example.there.domain.usecase.spotify.InsertCategory
import com.example.there.domain.usecase.spotify.IsCategorySaved
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.model.mapper.CategoryEntityMapper
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
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
            addDisposable(getPlaylistsForCategory.execute(GetPlaylistsForCategory.Input(categoryId, currentOffset))
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