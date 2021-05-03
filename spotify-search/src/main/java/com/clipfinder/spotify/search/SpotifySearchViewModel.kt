package com.clipfinder.spotify.search

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.android.spotify.model.Album
import com.clipfinder.core.android.spotify.model.Artist
import com.clipfinder.core.android.spotify.model.Playlist
import com.clipfinder.core.android.spotify.model.Track
import com.clipfinder.core.android.util.ext.completed
import com.clipfinder.core.android.util.ext.offset
import com.clipfinder.core.ext.map
import com.clipfinder.core.model.Loadable
import com.clipfinder.core.model.PagedList
import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.invoke
import com.clipfinder.core.spotify.model.SpotifySearchResult
import com.clipfinder.core.spotify.model.SpotifySearchType
import com.clipfinder.core.spotify.usecase.SearchSpotify
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import timber.log.Timber
import kotlin.reflect.KProperty1

class SpotifySearchViewModel(
    initialState: SpotifySearchState,
    private val searchSpotify: SearchSpotify
) : MvRxViewModel<SpotifySearchState>(initialState) {

    init {
        search(SearchSpotify.Args.Initial(initialState.query))
    }

    fun searchAlbums() = searchIfNotCompleted(SpotifySearchState::albums)
    fun clearAlbumsError() = clearErrorIn(SpotifySearchState::albums) { copy(albums = it) }
    fun searchArtists() = searchIfNotCompleted(SpotifySearchState::artists)
    fun clearArtistsError() = clearErrorIn(SpotifySearchState::artists) { copy(artists = it) }
    fun searchPlaylists() = searchIfNotCompleted(SpotifySearchState::playlists)
    fun clearPlaylistsError() = clearErrorIn(SpotifySearchState::playlists) { copy(playlists = it) }
    fun searchTracks() = searchIfNotCompleted(SpotifySearchState::tracks)
    fun clearTracksError() = clearErrorIn(SpotifySearchState::tracks) { copy(tracks = it) }

    private fun <T> searchIfNotCompleted(
        prop: KProperty1<SpotifySearchState, Loadable<PagedList<T>>>
    ) {
        withState {
            val items = prop.get(it)
            if (items.completed) return@withState
            search(
                SearchSpotify.Args.More(it.query, items.offset, requireNotNull(searchTypes[prop]))
            )
        }
    }

    private fun search(args: SearchSpotify.Args) {
        setState {
            when (args) {
                is SearchSpotify.Args.Initial ->
                    copy(
                        albums = albums.copyWithLoadingInProgress,
                        artists = artists.copyWithLoadingInProgress,
                        playlists = playlists.copyWithLoadingInProgress,
                        tracks = tracks.copyWithLoadingInProgress
                    )
                is SearchSpotify.Args.More ->
                    when (args.type) {
                        SpotifySearchType.ALBUM -> copy(albums = albums.copyWithLoadingInProgress)
                        SpotifySearchType.ARTIST ->
                            copy(artists = artists.copyWithLoadingInProgress)
                        SpotifySearchType.PLAYLIST ->
                            copy(playlists = playlists.copyWithLoadingInProgress)
                        SpotifySearchType.TRACK -> copy(tracks = tracks.copyWithLoadingInProgress)
                    }
            }
        }

        fun setErrorState(args: SearchSpotify.Args, error: Any?) {
            setState {
                when (args) {
                    is SearchSpotify.Args.Initial ->
                        copy(
                            albums = albums.copyWithError(error),
                            artists = artists.copyWithError(error),
                            playlists = playlists.copyWithError(error),
                            tracks = tracks.copyWithError(error)
                        )
                    is SearchSpotify.Args.More ->
                        when (args.type) {
                            SpotifySearchType.ALBUM -> copy(albums = albums.copyWithError(error))
                            SpotifySearchType.ARTIST -> copy(artists = artists.copyWithError(error))
                            SpotifySearchType.PLAYLIST ->
                                copy(playlists = playlists.copyWithError(error))
                            SpotifySearchType.TRACK -> copy(tracks = tracks.copyWithError(error))
                        }
                }
            }
        }

        searchSpotify(args = args)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { resource ->
                    when (resource) {
                        is Resource.Success<SpotifySearchResult> ->
                            setState {
                                val result = resource.data
                                copy(
                                    albums =
                                        result.albums?.let {
                                            copyWithPaged(
                                                it.map(::Album),
                                                SpotifySearchState::albums,
                                                ::PagedList
                                            )
                                        }
                                            ?: albums,
                                    artists =
                                        result.artists?.let {
                                            copyWithPaged(
                                                it.map(::Artist),
                                                SpotifySearchState::artists,
                                                ::PagedList
                                            )
                                        }
                                            ?: artists,
                                    playlists =
                                        result.playlists?.let {
                                            copyWithPaged(
                                                it.map(::Playlist),
                                                SpotifySearchState::playlists,
                                                ::PagedList
                                            )
                                        }
                                            ?: playlists,
                                    tracks =
                                        result.tracks?.let {
                                            copyWithPaged(
                                                it.map(::Track),
                                                SpotifySearchState::tracks,
                                                ::PagedList
                                            )
                                        }
                                            ?: tracks
                                )
                            }
                        is Resource.Error -> setErrorState(args, resource.error)
                    }
                },
                {
                    setErrorState(args, it)
                    Timber.e(it)
                }
            )
            .disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifySearchViewModel, SpotifySearchState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifySearchState
        ): SpotifySearchViewModel = SpotifySearchViewModel(state, viewModelContext.activity.get())

        private val searchTypes:
            Map<KProperty1<SpotifySearchState, Loadable<PagedList<*>>>, SpotifySearchType> =
            mapOf(
                SpotifySearchState::albums to SpotifySearchType.ALBUM,
                SpotifySearchState::artists to SpotifySearchType.ARTIST,
                SpotifySearchState::playlists to SpotifySearchType.PLAYLIST,
                SpotifySearchState::tracks to SpotifySearchType.TRACK
            )
    }
}
