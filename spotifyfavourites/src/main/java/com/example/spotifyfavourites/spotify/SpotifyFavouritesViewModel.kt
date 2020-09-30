package com.example.spotifyfavourites.spotify

import androidx.lifecycle.MutableLiveData
import com.example.core.android.base.vm.BaseViewModel
import com.example.core.android.mapper.spotify.ui
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.GetFavouriteAlbums
import com.example.there.domain.usecase.spotify.GetFavouriteArtists
import com.example.there.domain.usecase.spotify.GetFavouriteTracks
import io.reactivex.Flowable
import io.reactivex.functions.Function3

class SpotifyFavouritesViewModel(
    private val getFavouriteAlbums: GetFavouriteAlbums,
    private val getFavouriteArtists: GetFavouriteArtists,
    private val getFavouriteTracks: GetFavouriteTracks
) : BaseViewModel() {

    val viewState: MutableLiveData<SpotifyFavouritesFragmentViewState> = MutableLiveData()

    fun loadFavourites() {
        Flowable.combineLatest<List<AlbumEntity>, List<ArtistEntity>, List<TrackEntity>, SpotifyFavouritesFragmentViewState>(
            getFavouriteAlbums(),
            getFavouriteArtists(),
            getFavouriteTracks(),
            Function3 { albums, artists, tracks ->
                SpotifyFavouritesFragmentViewState(
                    ArrayList(albums.map(AlbumEntity::ui)),
                    ArrayList(artists.map(ArtistEntity::ui)),
                    ArrayList(tracks.map(TrackEntity::ui))
                )
            }
        ).subscribeAndDisposeOnCleared { viewState.value = it }
    }
}
