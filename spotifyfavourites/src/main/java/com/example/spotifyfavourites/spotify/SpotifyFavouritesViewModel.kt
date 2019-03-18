package com.example.spotifyfavourites.spotify

import android.arch.lifecycle.MutableLiveData
import com.example.coreandroid.mapper.spotify.ui
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.usecase.spotify.*
import io.reactivex.Flowable
import io.reactivex.functions.Function5
import javax.inject.Inject

class SpotifyFavouritesViewModel @Inject constructor(
        private val getFavouriteAlbums: GetFavouriteAlbums,
        private val getFavouriteArtists: GetFavouriteArtists,
        private val getFavouriteCategories: GetFavouriteCategories,
        private val getFavouriteSpotifyPlaylists: GetFavouriteSpotifyPlaylists,
        private val getFavouriteTracks: GetFavouriteTracks
) : com.example.coreandroid.base.vm.BaseViewModel() {

    val viewState: MutableLiveData<com.example.spotifyfavourites.spotify.SpotifyFavouritesFragmentViewState> = MutableLiveData()

    fun loadFavourites() {
        Flowable.combineLatest<List<AlbumEntity>, List<ArtistEntity>, List<CategoryEntity>, List<PlaylistEntity>, List<TrackEntity>, com.example.spotifyfavourites.spotify.SpotifyFavouritesFragmentViewState>(
                getFavouriteAlbums.execute(),
                getFavouriteArtists.execute(),
                getFavouriteCategories.execute(),
                getFavouriteSpotifyPlaylists.execute(),
                getFavouriteTracks.execute(),
                Function5 { albums, artists, categories, playlists, tracks ->
                    com.example.spotifyfavourites.spotify.SpotifyFavouritesFragmentViewState(
                            ArrayList(albums.map(AlbumEntity::ui)),
                            ArrayList(artists.map(ArtistEntity::ui)),
                            ArrayList(categories.map(CategoryEntity::ui)),
                            ArrayList(playlists.map(PlaylistEntity::ui)),
                            ArrayList(tracks.map(TrackEntity::ui))
                    )
                })
                .subscribeAndDisposeOnCleared { viewState.value = it }
    }
}