package com.example.spotify.album.ui

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.model.map
import com.example.core.model.mapData
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.mapper.spotify.ui
import com.example.core.android.model.Data
import com.example.core.android.model.LoadedSuccessfully
import com.example.core.android.model.Loading
import com.example.core.android.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.core.android.spotify.model.Album
import com.example.core.android.util.ext.observeNetworkConnectivity
import com.example.spotify.album.domain.usecase.GetTracksFromAlbum
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.DeleteAlbum
import com.example.there.domain.usecase.spotify.GetArtists
import com.example.there.domain.usecase.spotify.InsertAlbum
import com.example.there.domain.usecase.spotify.IsAlbumSaved
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class SpotifyAlbumViewModel(
    initialState: SpotifyAlbumViewState,
    private val getArtists: GetArtists,
    private val getTracksFromAlbum: GetTracksFromAlbum,
    context: Context
) : MvRxViewModel<SpotifyAlbumViewState>(initialState) {

    init {
        val album = initialState.album
        loadAlbumsArtists(artistIds = album.artists.map { it.id })
        loadTracksFromAlbum(albumId = album.id)
        handleConnectivityChanges(context)
    }

    fun loadAlbumsArtists(artistIds: List<String>) = withState { state ->
        if (state.artists.status is Loading) return@withState

        getArtists(args = artistIds, applySchedulers = false)
            .mapData { artists -> artists.map(ArtistEntity::ui).sortedBy { it.name } }
            .subscribeOn(Schedulers.io())
            .updateWithResource(SpotifyAlbumViewState::artists) { copy(artists = it) }
    }

    fun loadTracksFromAlbum(albumId: String) = withState { state ->
        if (!state.tracks.shouldLoad) return@withState

        val args = GetTracksFromAlbum.Args(albumId, state.tracks.offset)
        getTracksFromAlbum(args = args, applySchedulers = false)
            .mapData { tracksPage -> tracksPage.map(TrackEntity::ui) }
            .subscribeOn(Schedulers.io())
            .updateWithPagedResource(SpotifyAlbumViewState::tracks) { copy(tracks = it) }
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.observeNetworkConnectivity {
            withState { (album, artists, tracks, _) ->
                if (artists.isEmptyAndLastLoadingFailedWithNetworkError())
                    loadAlbumsArtists(album.artists.map { it.id })
                if (tracks.isEmptyAndLastLoadingFailedWithNetworkError())
                    loadTracksFromAlbum(album.id)
            }
        }.disposeOnClear()
    }

    companion object : MvRxViewModelFactory<SpotifyAlbumViewModel, SpotifyAlbumViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SpotifyAlbumViewState
        ): SpotifyAlbumViewModel {
            val getArtists: GetArtists by viewModelContext.activity.inject()
            val getTracksFromAlbum: GetTracksFromAlbum by viewModelContext.activity.inject()
            return SpotifyAlbumViewModel(
                state,
                getArtists,
                getTracksFromAlbum,
                viewModelContext.app()
            )
        }
    }
}
