package com.clipfinder.spotify.artist

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.ext.castAs
import com.clipfinder.core.ext.map
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.usecase.GetAlbumsFromArtist
import com.clipfinder.core.spotify.usecase.GetRelatedArtists
import com.clipfinder.core.spotify.usecase.GetTopTracksFromArtist
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.model.LoadingFirst
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.model.Ready
import com.clipfinder.core.model.WithValue
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.android.util.ext.copyWithPaged
import com.clipfinder.core.android.util.ext.loadingOrCompleted
import com.clipfinder.core.android.util.ext.offset
import com.clipfinder.core.android.util.ext.retryLoadCollectionOnConnected
import com.clipfinder.core.model.invoke
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import timber.log.Timber

private typealias State = SpotifyArtistViewState

class SpotifyArtistViewModel(
    initialState: State,
    private val getAlbumsFromArtist: GetAlbumsFromArtist,
    private val getTopTracksFromArtist: GetTopTracksFromArtist,
    private val getRelatedArtists: GetRelatedArtists,
    private val preferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<State>(initialState) {

    private val clear: PublishRelay<Unit> = PublishRelay.create()

    init {
        loadData()
        handlePreferencesChanges()
        handleConnectivityChanges(context)
    }

    fun onBackPressed() = withState { state ->
        if (state.artists.size < 2) {
            setState { copy(artists = emptyList()) }
            return@withState
        }

        setState { copy(artists = artists.dropLast(1)) }
        loadData(clearAlbums = true)
    }

    fun updateArtist(artist: Artist) {
        setState { copy(artists = artists + artist) }
        loadData(clearAlbums = true)
    }

    private fun loadData(clearAlbums: Boolean = false) {
        loadAlbumsFromArtist(shouldClear = clearAlbums)
        loadTopTracksFromArtist()
        loadRelatedArtists()
    }

    fun loadAlbumsFromArtist(shouldClear: Boolean = false) {
        if (shouldClear) clear.accept(Unit)
        withState { (artists, currentAlbums) ->
            if (currentAlbums.loadingOrCompleted && !shouldClear) return@withState

            setState {
                if (shouldClear) copy(albums = LoadingFirst)
                else copy(albums = albums.copyWithLoadingInProgress)
            }

            val args = GetAlbumsFromArtist.Args(
                artistId = artists.last().id,
                offset = if (shouldClear) 0 else currentAlbums.offset
            )
            getAlbumsFromArtist(args = args)
                .takeUntil(clear.toFlowable(BackpressureStrategy.LATEST))
                .mapData { albums -> albums.map(::Album) }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    setState {
                        when (it) {
                            is Resource.Success -> copy(albums = when (albums) {
                                is WithValue -> albums.copyWithPaged(it.data)
                                else -> Ready(PagedList(it.data))
                            })
                            is Resource.Error -> {
                                it.error?.castAs<Throwable>()?.let(::log)
                                    ?: Timber.wtf("Unknown error")
                                copy(albums = albums.copyWithError(it))
                            }
                        }
                    }
                }, {
                    setState { copy(albums = albums.copyWithError(it)) }
                    log(it)
                })
                .disposeOnClear()
        }
    }

    fun loadTopTracksFromArtist() {
        loadCollection(State::topTracks, getTopTracksFromArtist::intoState) { copy(topTracks = it) }
    }

    fun loadRelatedArtists() {
        loadCollection(State::relatedArtists, getRelatedArtists::intoState) { copy(relatedArtists = it) }
    }

    fun clearAlbumsError() = clearErrorIn(State::albums) { copy(albums = it) }
    fun clearTopTracksError() = clearErrorIn(State::topTracks) { copy(topTracks = it) }
    fun clearRelatedArtistsError() = clearErrorIn(State::relatedArtists) { copy(relatedArtists = it) }

    private fun handlePreferencesChanges() {
        preferences.countryObservable
            .skip(1)
            .distinctUntilChanged()
            .subscribe { loadAlbumsFromArtist() }
            .disposeOnClear()
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, albums, topTracks, relatedArtists) ->
            if (albums.retryLoadCollectionOnConnected) loadAlbumsFromArtist()
            if (topTracks.retryLoadCollectionOnConnected) loadTopTracksFromArtist()
            if (relatedArtists.retryLoadCollectionOnConnected) loadRelatedArtists()
        }
    }

    companion object : MvRxViewModelFactory<SpotifyArtistViewModel, State> {
        override fun create(
            viewModelContext: ViewModelContext, state: State
        ): SpotifyArtistViewModel = SpotifyArtistViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

private fun GetTopTracksFromArtist.intoState(
    state: State
): Single<Resource<List<Track>>> = this(args = state.artists.last().id)
    .mapData { tracks -> tracks.map(::Track).sortedBy(Track::name) }

private fun GetRelatedArtists.intoState(
    state: State
): Single<Resource<List<Artist>>> = this(args = state.artists.last().id)
    .mapData { artists -> artists.map(::Artist).sortedBy(Artist::name) }
