package com.example.spotifycategory

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.model.mapData
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.spotify.Category
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.spotify.DeleteCategory
import com.example.there.domain.usecase.spotify.GetPlaylistsForCategory
import com.example.there.domain.usecase.spotify.InsertCategory
import com.example.there.domain.usecase.spotify.IsCategorySaved
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber


class CategoryViewModel(
    initialState: CategoryViewState,
    private val getPlaylistsForCategory: GetPlaylistsForCategory,
    private val insertCategory: InsertCategory,
    private val deleteCategory: DeleteCategory,
    private val isCategorySaved: IsCategorySaved
) : MvRxViewModel<CategoryViewState>(initialState) {

    init {
        loadPlaylists()
        loadCategoryFavouriteState()
    }

    fun loadPlaylists(shouldClear: Boolean = false) {
        withState { state ->
            if (state.playlists.status is Loading) return@withState

            //TODO: test if offset needs a current function - in theory it shouldn't...
            getPlaylistsForCategory(GetPlaylistsForCategory.Args(state.category.id, if (shouldClear) 0 else state.playlists.offset))
                .mapData { playlistsPage -> playlistsPage.map(PlaylistEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithResourcePage(CategoryViewState::playlists) { copy(playlists = it) }
        }
    }

    fun toggleCategoryFavouriteState() = withState { state ->
        if (state.isSavedAsFavourite.value) deleteFavouriteCategory(state.category)
        else addFavouriteCategory(state.category)
    }

    private fun addFavouriteCategory(
        category: Category
    ) = insertCategory(category.domain, applySchedulers = false)
        .subscribeOn(Schedulers.io())
        .subscribe({ setState { copy(isSavedAsFavourite = Data(true, LoadedSuccessfully)) } }, {
            setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
            Timber.e(it)
        })
        .disposeOnClear()

    private fun deleteFavouriteCategory(
        category: Category
    ) = deleteCategory(category.domain, applySchedulers = false)
        .subscribeOn(Schedulers.io())
        .subscribe({ setState { copy(isSavedAsFavourite = Data(false, LoadedSuccessfully)) } }, {
            setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
            Timber.e(it)
        })
        .disposeOnClear()

    private fun loadCategoryFavouriteState() = withState { state ->
        if (state.isSavedAsFavourite.status is Loading) return@withState

        isCategorySaved(state.category.id)
            .subscribeOn(Schedulers.io())
            .update(CategoryViewState::isSavedAsFavourite) { copy(isSavedAsFavourite = it) }
    }

    companion object : MvRxViewModelFactory<CategoryViewModel, CategoryViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: CategoryViewState
        ): CategoryViewModel {
            val getPlaylistsForCategory: GetPlaylistsForCategory by viewModelContext.activity.inject()
            val insertCategory: InsertCategory by viewModelContext.activity.inject()
            val deleteCategory: DeleteCategory by viewModelContext.activity.inject()
            val isCategorySaved: IsCategorySaved by viewModelContext.activity.inject()
            return CategoryViewModel(
                state,
                getPlaylistsForCategory,
                insertCategory,
                deleteCategory,
                isCategorySaved
            )
        }
    }
}
