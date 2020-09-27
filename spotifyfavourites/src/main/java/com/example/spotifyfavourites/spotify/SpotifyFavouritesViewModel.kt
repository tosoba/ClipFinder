package com.example.spotifyfavourites.spotify

import androidx.lifecycle.MutableLiveData
import com.example.core.android.base.vm.BaseViewModel
import com.example.core.android.mapper.spotify.ui
import com.example.there.domain.entity.spotify.AlbumEntity
import com.example.there.domain.entity.spotify.ArtistEntity
import com.example.there.domain.entity.spotify.PlaylistEntity
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.usecase.spotify.GetFavouriteAlbums
import com.example.there.domain.usecase.spotify.GetFavouriteArtists
import com.example.there.domain.usecase.spotify.GetFavouriteSpotifyPlaylists
import com.example.there.domain.usecase.spotify.GetFavouriteTracks
import io.reactivex.Flowable
import io.reactivex.functions.Function4

class SpotifyFavouritesViewModel(
    private val getFavouriteAlbums: GetFavouriteAlbums,
    private val getFavouriteArtists: GetFavouriteArtists,
    private val getFavouriteSpotifyPlaylists: GetFavouriteSpotifyPlaylists,
    private val getFavouriteTracks: GetFavouriteTracks
) : BaseViewModel() {

    val viewState: MutableLiveData<SpotifyFavouritesFragmentViewState> = MutableLiveData()

    fun loadFavourites() {
        Flowable.combineLatest<List<AlbumEntity>, List<ArtistEntity>, List<PlaylistEntity>, List<TrackEntity>, SpotifyFavouritesFragmentViewState>(
            getFavouriteAlbums(),
            getFavouriteArtists(),
            getFavouriteSpotifyPlaylists(),
            getFavouriteTracks(),
            Function4 { albums, artists, playlists, tracks ->
                SpotifyFavouritesFragmentViewState(
                    ArrayList(albums.map(AlbumEntity::ui)),
                    ArrayList(artists.map(ArtistEntity::ui)),
                    ArrayList(playlists.map(PlaylistEntity::ui)),
                    ArrayList(tracks.map(TrackEntity::ui))
                )
            }
        ).subscribeAndDisposeOnCleared { viewState.value = it }
    }
}
