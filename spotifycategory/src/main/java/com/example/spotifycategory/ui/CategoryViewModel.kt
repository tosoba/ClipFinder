package com.example.spotifycategory.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.NetworkInfo
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.model.mapData
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.coreandroid.model.spotify.Category
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.usecase.spotify.DeleteCategory
import com.example.there.domain.usecase.spotify.GetPlaylistsForCategory
import com.example.there.domain.usecase.spotify.InsertCategory
import com.example.there.domain.usecase.spotify.IsCategorySaved
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class CategoryViewModel(
    initialState: CategoryViewState,
    private val getPlaylistsForCategory: GetPlaylistsForCategory,
    private val insertCategory: InsertCategory,
    private val deleteCategory: DeleteCategory,
    private val isCategorySaved: IsCategorySaved,
    private val preferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<CategoryViewState>(initialState) {

    init {
        loadPlaylists()
        loadCategoryFavouriteState()
        handlePreferencesChanges()
        handleConnectivityChanges(context)
    }

    fun loadPlaylists(shouldClear: Boolean = false) = withState { state ->
        if (state.playlists.status is Loading) return@withState

        getPlaylistsForCategory(
            GetPlaylistsForCategory.Args(
                categoryId = state.category.id,
                offset = if (shouldClear) 0 else state.playlists.offset
            )
        ).mapData { playlistsPage ->
            playlistsPage.map(PlaylistEntity::ui)
        }.subscribeOn(Schedulers.io())
            .updateWithResourcePage(CategoryViewState::playlists) { copy(playlists = it) }
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
            .update(CategoryViewState::isSavedAsFavourite) { copy(isSavedAsFavourite = it) }
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
        ReactiveNetwork.observeNetworkConnectivity(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(ConnectivityPredicate.hasState(NetworkInfo.State.CONNECTED))
            .subscribe {
                withState { (_, _, playlists) ->
                    if (playlists.isEmptyAndLastLoadingFailedWithNetworkError()) loadPlaylists()
                }
            }
            .disposeOnClear()
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
            val preferences: SpotifyPreferences by viewModelContext.activity.inject()
            return CategoryViewModel(
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
