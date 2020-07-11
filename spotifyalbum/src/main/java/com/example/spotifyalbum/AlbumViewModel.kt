package com.example.spotifyalbum

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.model.mapData
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.Data
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.spotify.Album
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.*
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class AlbumViewModel(
    initialState: AlbumViewState,
    private val getArtists: GetArtists,
    private val getTracksFromAlbum: GetTracksFromAlbum,
    private val insertAlbum: InsertAlbum,
    private val deleteAlbum: DeleteAlbum,
    private val isAlbumSaved: IsAlbumSaved
) : MvRxViewModel<AlbumViewState>(initialState) {

    init {
        loadAlbumData(initialState.album)
    }

    fun loadAlbumData(album: Album) {
        loadAlbumsArtists(artistIds = album.artists.map { it.id })
        loadTracksFromAlbum(albumId = album.id)
        loadAlbumFavouriteState(album)
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
                isAlbumSaved
            )
        }
    }
}
