package com.example.spotify.category.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.mapper.spotify.domain
import com.example.core.android.mapper.spotify.ui
import com.example.core.android.model.Data
import com.example.core.android.model.LoadedSuccessfully
import com.example.core.android.model.Loading
import com.example.core.android.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.core.android.model.spotify.Category
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.spotify.category.domain.usecase.DeleteCategory
import com.example.spotify.category.domain.usecase.GetPlaylistsForCategory
import com.example.spotify.category.domain.usecase.InsertCategory
import com.example.spotify.category.domain.usecase.IsCategorySaved
import com.example.there.domain.entity.spotify.PlaylistEntity
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class SpotifyCategoryViewModel(
    initialState: SpotifyCategoryViewState,
    private val getPlaylistsForCategory: GetPlaylistsForCategory,
    private val insertCategory: InsertCategory,
    private val deleteCategory: DeleteCategory,
    private val isCategorySaved: IsCategorySaved,
    private val preferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<SpotifyCategoryViewState>(initialState) {

    init {
        loadPlaylists()
        loadCategoryFavouriteState()
        handlePreferencesChanges()
        handleConnectivityChanges(context)
    }

    fun loadPlaylists(shouldClear: Boolean = false) = withState { state ->
        if (!state.playlists.shouldLoad) return@withState

        val args = GetPlaylistsForCategory.Args(
            categoryId = state.category.id,
            offset = if (shouldClear) 0 else state.playlists.offset
        )
        getPlaylistsForCategory(args)
            .mapData { playlistsPage -> playlistsPage.map(PlaylistEntity::ui) }
            .subscribeOn(Schedulers.io())
            .updateWithPagedResource(SpotifyCategoryViewState::playlists, shouldClear = shouldClear) {
                copy(playlists = it)
            }
    }

    fun toggleCategoryFavouriteState() = withState { state ->
        if (state.isSavedAsFavourite.value) deleteFavouriteCategory(state.category)
        else addFavouriteCategory(state.category)
    }

    private fun addFavouriteCategory(category: Category) {
        insertCategory(category.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({
                setState { copy(isSavedAsFavourite = Data(true, LoadedSuccessfully)) }
            }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()
    }

    private fun deleteFavouriteCategory(category: Category) {
        deleteCategory(category.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({
                setState { copy(isSavedAsFavourite = Data(false, LoadedSuccessfully)) }
            }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()
    }

    private fun loadCategoryFavouriteState() = withState { state ->
        if (state.isSavedAsFavourite.status is Loading) return@withState

        isCategorySaved(state.category.id)
            .subscribeOn(Schedulers.io())
            .update(SpotifyCategoryViewState::isSavedAsFavourite) { copy(isSavedAsFavourite = it) }
    }

    private fun handlePreferencesChanges() {
        preferences.countryObservable
            .skip(1)
            .distinctUntilChanged()
            .subscribe({ loadPlaylists(true) }, Timber::e)
            .disposeOnClear()
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.observeNetworkConnectivity {
            withState { (_, _, playlists) ->
                if (playlists.isEmptyAndLastLoadingFailedWithNetworkError()) loadPlaylists()
            }
        }.disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifyCategoryViewModel, SpotifyCategoryViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyCategoryViewState
        ): SpotifyCategoryViewModel {
            val getPlaylistsForCategory: GetPlaylistsForCategory by viewModelContext.activity.inject()
            val insertCategory: InsertCategory by viewModelContext.activity.inject()
            val deleteCategory: DeleteCategory by viewModelContext.activity.inject()
            val isCategorySaved: IsCategorySaved by viewModelContext.activity.inject()
            val preferences: SpotifyPreferences by viewModelContext.activity.inject()
            return SpotifyCategoryViewModel(
                state,
                getPlaylistsForCategory,
                insertCategory,
                deleteCategory,
                isCategorySaved,
                preferences,
                viewModelContext.app()
            )
        }
    }
}
