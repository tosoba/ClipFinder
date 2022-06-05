package com.clipfinder.spotify.category

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.android.util.ext.offset
import com.clipfinder.core.android.util.ext.retryLoadCollectionOnConnected
import com.clipfinder.core.ext.map
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.invoke
import com.clipfinder.core.spotify.usecase.GetPlaylistsForCategory
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
            shouldClear,
            ::PagedList
        ) { copy(playlists = it) }
    }

    fun clearPlaylistsError() {
        clearErrorIn(State::playlists) { copy(playlists = it) }
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
            if (playlists.retryLoadCollectionOnConnected) loadPlaylists()
        }
    }

    private fun GetPlaylistsForCategory.intoState(
        state: State,
        shouldClear: Boolean
    ): Single<Resource<Paged<List<Playlist>>>> {
        val args =
            GetPlaylistsForCategory.Args(
                categoryId = state.category.id,
                offset = if (shouldClear) 0 else state.playlists.offset
            )
        return this(args = args)
            .mapData { playlistsPage -> playlistsPage.map(::Playlist) }
            .takeUntil(clear.toFlowable(BackpressureStrategy.LATEST))
    }

    companion object : MvRxViewModelFactory<SpotifyCategoryViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: State
        ): SpotifyCategoryViewModel =
            SpotifyCategoryViewModel(
                state,
                viewModelContext.activity.get(),
                viewModelContext.activity.get(),
                viewModelContext.app()
            )
    }
}
