package com.example.spotifyartist

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.model.mapData
import com.example.coreandroid.base.vm.MvRxViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.model.*
import com.example.coreandroid.model.spotify.Artist
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.*
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class ArtistViewModel(
        initialState: ArtistViewState,
        private val getAlbumsFromArtist: GetAlbumsFromArtist,
        private val getTopTracksFromArtist: GetTopTracksFromArtist,
        private val getRelatedArtists: GetRelatedArtists,
        private val insertArtist: InsertArtist,
        private val deleteArtist: DeleteArtist,
        private val isArtistSaved: IsArtistSaved
) : MvRxViewModel<ArtistViewState>(initialState) {

    init {
        loadData(initialState.artists.value.last())
    }

    fun onBackPressed() = withState { state ->
        if (state.artists.value.size < 2) {
            setState { copy(artists = DataList(emptyList())) }
            return@withState
        }

        val previousArtist = state.artists.value.elementAt(state.artists.value.size - 2)
        setState { copy(artists = DataList(artists.value.take(artists.value.size - 1))) }
        loadData(previousArtist)
    }


    fun updateArtist(artist: Artist) {
        setState { copy(artists = artists.copyWithNewItems(artist)) }
        loadData(artist)
    }

    fun loadMissingData() = withState { state ->
        state.artists.value.lastOrNull()?.id?.let {
            if (state.albums.status is LoadingFailed<*>)
                loadAlbumsFromArtist(it)
            if (state.topTracks.status is LoadingFailed<*>)
                loadTopTracksFromArtist(it)
            if (state.relatedArtists.status is LoadingFailed<*>)
                loadRelatedArtists(it)
        }
    }

    private fun loadData(artist: Artist) {
        loadAlbumsFromArtist(artist.id)
        loadTopTracksFromArtist(artist.id)
        loadRelatedArtists(artist.id)
        loadArtistFavouriteState()
    }

    fun loadAlbumsFromArtist(artistId: String) = withState { state ->
        if (state.albums.status is Loading) return@withState

        getAlbumsFromArtist(args = artistId, applySchedulers = false)
                .mapData { albums -> albums.map(AlbumEntity::ui) }
                .subscribeOn(Schedulers.io())
                .updateWithResource(ArtistViewState::albums) { copy(albums = it) }
    }


    fun loadTopTracksFromArtist(artistId: String) = withState { state ->
        if (state.topTracks.status is Loading) return@withState

        getTopTracksFromArtist(args = artistId, applySchedulers = false)
                .mapData { tracks -> tracks.map(TrackEntity::ui).sortedBy { it.name } }
                .subscribeOn(Schedulers.io())
                .updateWithResource(ArtistViewState::topTracks) { copy(topTracks = it) }
    }

    fun loadRelatedArtists(artistId: String) = withState { state ->
        if (state.relatedArtists.status is Loading) return@withState

        getRelatedArtists(args = artistId, applySchedulers = false)
                .mapData { artists -> artists.map(ArtistEntity::ui).sortedBy { it.name } }
                .subscribeOn(Schedulers.io())
                .updateWithResource(ArtistViewState::relatedArtists) { copy(relatedArtists = it) }
    }

    fun toggleArtistFavouriteState() = withState { state ->
        state.artists.value.lastOrNull()?.let {
            if (state.isSavedAsFavourite.value) deleteFavouriteArtist(it)
            else addFavouriteArtist(it)
        }
    }

    private fun addFavouriteArtist(artist: Artist) = insertArtist(artist.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({ setState { copy(isSavedAsFavourite = Data(true, LoadedSuccessfully)) } }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()

    private fun deleteFavouriteArtist(artist: Artist) = deleteArtist(artist.domain, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe({ setState { copy(isSavedAsFavourite = Data(false, LoadedSuccessfully)) } }, {
                setState { copy(isSavedAsFavourite = isSavedAsFavourite.copyWithError(it)) }
                Timber.e(it)
            })
            .disposeOnClear()

    private fun loadArtistFavouriteState() = withState { state ->
        if (state.isSavedAsFavourite.status is Loading) return@withState

        isArtistSaved(args = state.artists.value.last().id, applySchedulers = false)
                .subscribeOn(Schedulers.io())
                .update(ArtistViewState::isSavedAsFavourite) { copy(isSavedAsFavourite = it) }
    }

    companion object : MvRxViewModelFactory<ArtistViewModel, ArtistViewState> {
        override fun create(viewModelContext: ViewModelContext, state: ArtistViewState): ArtistViewModel? {
            val getAlbumsFromArtist: GetAlbumsFromArtist by viewModelContext.activity.inject()
            val getTopTracksFromArtist: GetTopTracksFromArtist by viewModelContext.activity.inject()
            val getRelatedArtists: GetRelatedArtists by viewModelContext.activity.inject()
            val insertArtist: InsertArtist by viewModelContext.activity.inject()
            val deleteArtist: DeleteArtist by viewModelContext.activity.inject()
            val isArtistSaved: IsArtistSaved by viewModelContext.activity.inject()
            return ArtistViewModel(state, getAlbumsFromArtist, getTopTracksFromArtist, getRelatedArtists, insertArtist, deleteArtist, isArtistSaved)
        }
    }
}
