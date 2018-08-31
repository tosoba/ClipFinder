package com.example.there.findclips.fragments.favourites.spotify

import android.arch.lifecycle.MutableLiveData
import com.example.there.domain.entities.spotify.*
import com.example.there.domain.usecases.spotify.*
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.mappers.*
import io.reactivex.Observable
import io.reactivex.functions.Function5
import javax.inject.Inject

class SpotifyFavouritesViewModel @Inject constructor(
        private val getFavouriteAlbums: GetFavouriteAlbums,
        private val getFavouriteArtists: GetFavouriteArtists,
        private val getFavouriteCategories: GetFavouriteCategories,
        private val getFavouriteSpotifyPlaylists: GetFavouriteSpotifyPlaylists,
        private val getFavouriteTracks: GetFavouriteTracks
) : BaseViewModel() {

    val viewState = SpotifyFavouritesFragmentViewState()

    val loadedFlag: MutableLiveData<Unit> = MutableLiveData()

    fun loadFavourites() {
        addDisposable(Observable.zip<List<AlbumEntity>, List<ArtistEntity>, List<CategoryEntity>, List<PlaylistEntity>, List<TrackEntity>, Unit>(
                getFavouriteAlbums.execute(),
                getFavouriteArtists.execute(),
                getFavouriteCategories.execute(),
                getFavouriteSpotifyPlaylists.execute(),
                getFavouriteTracks.execute(),
                Function5 { albums, artists, categories, playlists, tracks ->
                    viewState.clearAll()
                    viewState.albums.addAll(albums.map(AlbumEntityMapper::mapFrom))
                    viewState.artists.addAll(artists.map(ArtistEntityMapper::mapFrom))
                    viewState.categories.addAll(categories.map(CategoryEntityMapper::mapFrom))
                    viewState.playlists.addAll(playlists.map(PlaylistEntityMapper::mapFrom))
                    viewState.tracks.addAll(tracks.map(TrackEntityMapper::mapFrom))
                }).subscribe { loadedFlag.value = Unit })
    }
}