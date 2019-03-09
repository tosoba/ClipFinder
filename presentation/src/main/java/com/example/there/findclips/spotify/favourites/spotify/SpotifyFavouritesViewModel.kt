package com.example.there.findclips.spotify.favourites.spotify

import android.arch.lifecycle.MutableLiveData
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.usecase.spotify.*
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.mapper.*
import io.reactivex.Flowable
import io.reactivex.functions.Function5
import javax.inject.Inject

class SpotifyFavouritesViewModel @Inject constructor(
        private val getFavouriteAlbums: GetFavouriteAlbums,
        private val getFavouriteArtists: GetFavouriteArtists,
        private val getFavouriteCategories: GetFavouriteCategories,
        private val getFavouriteSpotifyPlaylists: GetFavouriteSpotifyPlaylists,
        private val getFavouriteTracks: GetFavouriteTracks
) : BaseViewModel() {

    val viewState: MutableLiveData<SpotifyFavouritesFragmentViewState> = MutableLiveData()

    fun loadFavourites() {
        Flowable.combineLatest<List<AlbumEntity>, List<ArtistEntity>, List<CategoryEntity>, List<PlaylistEntity>, List<TrackEntity>, SpotifyFavouritesFragmentViewState>(
                getFavouriteAlbums.execute(),
                getFavouriteArtists.execute(),
                getFavouriteCategories.execute(),
                getFavouriteSpotifyPlaylists.execute(),
                getFavouriteTracks.execute(),
                Function5 { albums, artists, categories, playlists, tracks ->
                    SpotifyFavouritesFragmentViewState(
                            ArrayList(albums.map(AlbumEntityMapper::mapFrom)),
                            ArrayList(artists.map(ArtistEntityMapper::mapFrom)),
                            ArrayList(categories.map(CategoryEntityMapper::mapFrom)),
                            ArrayList(playlists.map(PlaylistEntityMapper::mapFrom)),
                            ArrayList(tracks.map(TrackEntityMapper::mapFrom))
                    )
                })
                .subscribeAndDisposeOnCleared { viewState.value = it }
    }
}