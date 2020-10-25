package com.example.spotify.category.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.model.retryLoadItemsOnNetworkAvailable
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.spotify.category.domain.usecase.GetPlaylistsForCategory
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class SpotifyCategoryViewModel(
    initialState: SpotifyCategoryViewState,
    private val getPlaylistsForCategory: GetPlaylistsForCategory,
    private val preferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<SpotifyCategoryViewState>(initialState) {

    init {
        loadPlaylists()
        handlePreferencesChanges()
        handleConnectivityChanges(context)
    }

    fun loadPlaylists(shouldClear: Boolean = false) = withState { state ->
        if (!state.playlists.shouldLoadMore) return@withState

        val args = GetPlaylistsForCategory.Args(
            categoryId = state.category.id,
            offset = if (shouldClear) 0 else state.playlists.offset
        )
        getPlaylistsForCategory(args)
            .mapData { playlistsPage -> playlistsPage.map { Playlist(it) } }
            .subscribeOn(Schedulers.io())
            .updateWithPagedResource(SpotifyCategoryViewState::playlists, shouldClear = shouldClear) {
                copy(playlists = it)
            }
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
            withState { (_, playlists) ->
                if (playlists.retryLoadItemsOnNetworkAvailable) loadPlaylists()
            }
        }.disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifyCategoryViewModel, SpotifyCategoryViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyCategoryViewState
        ): SpotifyCategoryViewModel {
            val getPlaylistsForCategory: GetPlaylistsForCategory by viewModelContext.activity.inject()
            val preferences: SpotifyPreferences by viewModelContext.activity.inject()
            return SpotifyCategoryViewModel(
                state,
                getPlaylistsForCategory,
                preferences,
                viewModelContext.app()
            )
        }
    }
}
