package com.example.spotifyalbum

import android.annotation.SuppressLint
import android.content.Context
import android.net.NetworkInfo
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.model.mapData
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.coreandroid.model.spotify.Album
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.*
import com.github.pwittchen.reactivenetwork.library.rx2.ConnectivityPredicate
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class AlbumViewModel(
    initialState: AlbumViewState,
    private val getArtists: GetArtists,
    private val getTracksFromAlbum: GetTracksFromAlbum,
    private val insertAlbum: InsertAlbum,
    private val deleteAlbum: DeleteAlbum,
    private val isAlbumSaved: IsAlbumSaved,
    context: Context
) : MvRxViewModel<AlbumViewState>(initialState) {

    init {
        val album = initialState.album
        loadAlbumsArtists(artistIds = album.artists.map { it.id })
        loadTracksFromAlbum(albumId = album.id)
        loadAlbumFavouriteState(album)

        @SuppressLint("MissingPermission")
        val disposable = ReactiveNetwork.observeNetworkConnectivity(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter(ConnectivityPredicate.hasState(NetworkInfo.State.CONNECTED))
            .subscribe {
                withState { (album, artists, tracks, _) ->
                    if (artists.isEmptyAndLastLoadingFailedWithNetworkError())
                        loadAlbumsArtists(album.artists.map { it.id })
                    if (tracks.isEmptyAndLastLoadingFailedWithNetworkError())
                        loadTracksFromAlbum(album.id)
                }
            }
            .disposeOnClear()
    }

    fun loadAlbumsArtists(artistIds: List<String>) = withState { state ->
        if (state.artists.status is Loading) return@withState

        getArtists(args = artistIds, applySchedulers = false)
            .mapData { artists -> artists.map(ArtistEntity::ui).sortedBy { it.name } }
            .subscribeOn(Schedulers.io())
            .updateWithResource(AlbumViewState::artists) { copy(artists = it) }
    }

    fun loadTracksFromAlbum(albumId: String) = withState { state ->
        if (state.tracks.status is Loading) return@withState

        getTracksFromAlbum(
            args = GetTracksFromAlbum.Args(albumId, state.tracks.offset),
            applySchedulers = false
        ).mapData { tracksPage ->
            tracksPage.map(TrackEntity::ui)
        }.subscribeOn(Schedulers.io())
            .updateWithPagedResource(AlbumViewState::tracks) { copy(tracks = it) }
    }

    fun toggleAlbumFavouriteState() = withState { state ->
        if (state.isSavedAsFavourite.value) deleteFavouriteAlbum(state.album)
        else addFavouriteAlbum(state.album)
    }

    private fun addFavouriteAlbum(album: Album) {
        insertAlbum(album.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({
                setState { copy(isSavedAsFavourite = Data(true, LoadedSuccessfully)) }
            }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()
    }

    private fun deleteFavouriteAlbum(album: Album) {
        deleteAlbum(album.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({
                setState { copy(isSavedAsFavourite = Data(false, LoadedSuccessfully)) }
            }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()
    }

    private fun loadAlbumFavouriteState(album: Album) = withState { state ->
        if (state.isSavedAsFavourite.status is Loading) return@withState

        isAlbumSaved(album.id)
            .subscribeOn(Schedulers.io())
            .update(AlbumViewState::isSavedAsFavourite) { copy(isSavedAsFavourite = it) }
    }

    companion object : MvRxViewModelFactory<AlbumViewModel, AlbumViewState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: AlbumViewState
        ): AlbumViewModel {
            val getArtists: GetArtists by viewModelContext.activity.inject()
            val getTracksFromAlbum: GetTracksFromAlbum by viewModelContext.activity.inject()
            val insertAlbum: InsertAlbum by viewModelContext.activity.inject()
            val deleteAlbum: DeleteAlbum by viewModelContext.activity.inject()
            val isAlbumSaved: IsAlbumSaved by viewModelContext.activity.inject()
            return AlbumViewModel(
                state,
                getArtists,
                getTracksFromAlbum,
                insertAlbum,
                deleteAlbum,
                isAlbumSaved,
                viewModelContext.app()
            )
        }
    }
}
