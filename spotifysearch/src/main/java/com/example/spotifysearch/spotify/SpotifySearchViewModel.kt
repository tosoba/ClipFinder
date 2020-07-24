package com.example.spotifysearch.spotify

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.SpotifyDefaults
import com.example.core.model.Resource
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.mapper.spotify.ui
import com.example.core.android.model.LoadedSuccessfully
import com.example.core.android.model.Loading
import com.example.core.android.model.LoadingFailed
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.usecase.spotify.SearchSpotify
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class SpotifySearchViewModel(
    initialState: SpotifySearchViewState,
    private val searchSpotify: SearchSpotify
) : MvRxViewModel<SpotifySearchViewState>(initialState) {

    fun onNewQuery(query: String) = withState { state ->
        if (state.status is Loading) return@withState

        if (state.query != query) {
            setState { resetWithNewQuery(query) }
            searchWith(query, 0)
        } else if (state.offset == 0 || (state.offset < state.totalItems)) {
            searchWith(query, state.offset)
        }
    }

    fun searchWithLastQuery() = withState { state ->
        if (state.status is Loading) return@withState

        if (state.offset == 0 || (state.offset < state.totalItems)) {
            searchWith(state.query, state.offset)
        }
    }

    private fun searchWith(query: String, offset: Int) {
        setState { copy(status = Loading) }
        searchSpotify(args = SearchSpotify.Args(query, offset), applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({
                when (it) {
                    is Resource.Success<SearchAllEntity> -> setState {
                        val searchResult = it.data
                        copy(
                            status = LoadedSuccessfully,
                            offset = this.offset + SpotifyDefaults.LIMIT,
                            totalItems = searchResult.totalItems,
                            artists = this.artists + searchResult.artists.map(ArtistEntity::ui),
                            albums = this.albums + searchResult.albums.map(AlbumEntity::ui),
                            tracks = this.tracks + searchResult.tracks.map(TrackEntity::ui),
                            playlists = this.playlists + searchResult.playlists.map(PlaylistEntity::ui)
                        )
                    }
                    is Resource.Error<SearchAllEntity, *> -> setState {
                        copy(status = LoadingFailed(it))
                    }
                }
            }, {
                setState { copy(status = LoadingFailed(it)) }
                Timber.e(it)
            })
            .disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifySearchViewModel, SpotifySearchViewState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SpotifySearchViewState
        ): SpotifySearchViewModel {
            val searchSpotify: SearchSpotify by viewModelContext.activity.inject()
            return SpotifySearchViewModel(state, searchSpotify)
        }
    }
}
