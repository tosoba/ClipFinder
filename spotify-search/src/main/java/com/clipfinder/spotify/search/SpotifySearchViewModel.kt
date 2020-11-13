package com.clipfinder.spotify.search

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.spotify.model.*
import com.example.core.model.Resource
import com.clipfinder.core.spotify.model.SpotifySearchResult
import com.clipfinder.core.spotify.model.SpotifySearchType
import com.clipfinder.core.spotify.usecase.SearchSpotify
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import timber.log.Timber

class SpotifySearchViewModel(
    initialState: SpotifySearchViewState,
    private val searchSpotify: SearchSpotify
) : MvRxViewModel<SpotifySearchViewState>(initialState) {

    init {
        search(SearchSpotify.Args.Initial(initialState.query))
    }

    fun searchAlbums() = withState {
        if (!it.albums.shouldLoadMore) return@withState
        search(SearchSpotify.Args.More(it.query, it.albums.offset, SpotifySearchType.ALBUM))
    }

    fun searchArtists() = withState {
        if (!it.artists.shouldLoadMore) return@withState
        search(SearchSpotify.Args.More(it.query, it.artists.offset, SpotifySearchType.ARTIST))
    }

    fun searchPlaylists() = withState {
        if (!it.playlists.shouldLoadMore) return@withState
        search(SearchSpotify.Args.More(it.query, it.playlists.offset, SpotifySearchType.PLAYLIST))
    }

    fun searchTracks() = withState {
        if (!it.tracks.shouldLoadMore) return@withState
        search(SearchSpotify.Args.More(it.query, it.tracks.offset, SpotifySearchType.TRACK))
    }

    private fun search(args: SearchSpotify.Args) {
        setState {
            when (args) {
                is SearchSpotify.Args.Initial -> copy(
                    albums = albums.copyWithLoadingInProgress,
                    artists = artists.copyWithLoadingInProgress,
                    playlists = playlists.copyWithLoadingInProgress,
                    tracks = tracks.copyWithLoadingInProgress
                )
                is SearchSpotify.Args.More -> when (args.type) {
                    SpotifySearchType.ALBUM -> copy(albums = albums.copyWithLoadingInProgress)
                    SpotifySearchType.ARTIST -> copy(artists = artists.copyWithLoadingInProgress)
                    SpotifySearchType.PLAYLIST -> copy(playlists = playlists.copyWithLoadingInProgress)
                    SpotifySearchType.TRACK -> copy(tracks = tracks.copyWithLoadingInProgress)
                }
            }
        }

        fun setErrorState(args: SearchSpotify.Args, error: Any?) {
            setState {
                when (args) {
                    is SearchSpotify.Args.Initial -> copy(
                        albums = albums.copyWithError(error),
                        artists = artists.copyWithError(error),
                        playlists = playlists.copyWithError(error),
                        tracks = tracks.copyWithError(error)
                    )
                    is SearchSpotify.Args.More -> when (args.type) {
                        SpotifySearchType.ALBUM -> copy(albums = albums.copyWithError(error))
                        SpotifySearchType.ARTIST -> copy(artists = artists.copyWithError(error))
                        SpotifySearchType.PLAYLIST -> copy(playlists = playlists.copyWithError(error))
                        SpotifySearchType.TRACK -> copy(tracks = tracks.copyWithError(error))
                    }
                }
            }
        }

        searchSpotify(args = args, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({ resource ->
                when (resource) {
                    is Resource.Success<SpotifySearchResult> -> setState {
                        val searchResult = resource.data
                        copy(
                            albums = searchResult.albums?.let { (newItems, offset, total) ->
                                albums.copyWithNewItems(newItems.map(::Album), offset, total)
                            } ?: albums,
                            artists = searchResult.artists?.let { (newItems, offset, total) ->
                                artists.copyWithNewItems(newItems.map(::Artist), offset, total)
                            } ?: artists,
                            playlists = searchResult.playlists?.let { (newItems, offset, total) ->
                                playlists.copyWithNewItems(newItems.map(::Playlist), offset, total)
                            } ?: playlists,
                            tracks = searchResult.tracks?.let { (newItems, offset, total) ->
                                tracks.copyWithNewItems(newItems.map(::Track), offset, total)
                            } ?: tracks
                        )
                    }
                    is Resource.Error -> setErrorState(args, resource.error)
                }
            }, {
                setErrorState(args, it)
                Timber.e(it)
            })
            .disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifySearchViewModel, SpotifySearchViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifySearchViewState
        ): SpotifySearchViewModel = SpotifySearchViewModel(state, viewModelContext.activity.get())
    }
}
