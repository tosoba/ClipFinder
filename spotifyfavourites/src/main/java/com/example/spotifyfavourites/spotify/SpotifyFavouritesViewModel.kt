package com.example.spotifyfavourites.spotify

import androidx.lifecycle.MutableLiveData
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.mapper.spotify.ui
import com.example.there.domain.entity.spotify.*
import com.example.there.domain.usecase.spotify.*
import io.reactivex.Flowable
import io.reactivex.functions.Function5

class SpotifyFavouritesViewModel(
        private val getFavouriteAlbums: GetFavouriteAlbums,
        private val getFavouriteArtists: GetFavouriteArtists,
        private val getFavouriteCategories: GetFavouriteCategories,
        private val getFavouriteSpotifyPlaylists: GetFavouriteSpotifyPlaylists,
        private val getFavouriteTracks: GetFavouriteTracks
) : BaseViewModel() {

    val viewState: MutableLiveData<SpotifyFavouritesFragmentViewState> = MutableLiveData()

    fun loadFavourites() {
        Flowable.combineLatest<List<AlbumEntity>, List<ArtistEntity>, List<CategoryEntity>, List<PlaylistEntity>, List<TrackEntity>, SpotifyFavouritesFragmentViewState>(
                getFavouriteAlbums(),
                getFavouriteArtists(),
                getFavouriteCategories(),
                getFavouriteSpotifyPlaylists(),
                getFavouriteTracks(),
                Function5 { albums, artists, categories, playlists, tracks ->
                    SpotifyFavouritesFragmentViewState(
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