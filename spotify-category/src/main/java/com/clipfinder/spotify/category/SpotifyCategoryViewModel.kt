package com.clipfinder.spotify.category

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.spotify.usecase.GetPlaylistsForCategory
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.spotify.model.Playlist
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.core.android.util.ext.retryLoadItemsOnNetworkAvailable
import com.example.core.model.Paged
import com.example.core.model.Resource
import com.example.core.model.map
import com.example.core.model.mapData
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import org.koin.android.ext.android.get
import timber.log.Timber

private typealias State = SpotifyCategoryState

class SpotifyCategoryViewModel(
    initialState: State,
    private val getPlaylistsForCategory: GetPlaylistsForCategory,
    private val preferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<State>(initialState) {
    private val clear: PublishRelay<Unit> = PublishRelay.create()

    init {
        loadPlaylists()
        handlePreferencesChanges()
        handleConnectivityChanges(context)
    }

    fun loadPlaylists(shouldClear: Boolean = false) {
        loadPaged(
            State::playlists,
            { state, args -> getPlaylistsForCategory.intoState(state, args) },
            args = shouldClear
        ) { copy(playlists = it) }
    }

    fun clearPlaylistsError() {
        clearError(State::playlists) { copy(playlists = it) }
    }

    private fun handlePreferencesChanges() {
        preferences.countryObservable
            .skip(1)
            .distinctUntilChanged()
            .doOnNext { clear.accept(Unit) }
            .subscribe({ loadPlaylists(true) }, Timber::e)
            .disposeOnClear()
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, playlists) ->
            if (playlists.retryLoadItemsOnNetworkAvailable) loadPlaylists()
        }
    }

    private fun GetPlaylistsForCategory.intoState(
        state: State, shouldClear: Boolean
    ): Single<Resource<Paged<List<Playlist>>>> {
        val args = GetPlaylistsForCategory.Args(
            categoryId = state.category.id,
            offset = if (shouldClear) 0 else state.playlists.value.offset
        )
        return this(applySchedulers = false, args = args)
            .mapData { playlistsPage -> playlistsPage.map(::Playlist) }
            .takeUntil(clear.toFlowable(BackpressureStrategy.LATEST))
    }

    companion object : MvRxViewModelFactory<SpotifyCategoryViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): SpotifyCategoryViewModel = SpotifyCategoryViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}
