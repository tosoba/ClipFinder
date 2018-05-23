package com.example.there.findclips.fragments.favourites

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.there.domain.usecases.spotify.*
import com.example.there.domain.usecases.videos.GetFavouriteVideoPlaylists
import com.example.there.findclips.base.BaseViewModel
import com.example.there.findclips.model.entities.*
import com.example.there.findclips.model.mappers.*

class FavouritesViewModel(private val getFavouriteAlbums: GetFavouriteAlbums,
                          private val getFavouriteArtists: GetFavouriteArtists,
                          private val getFavouriteCategories: GetFavouriteCategories,
                          private val getFavouriteSpotifyPlaylists: GetFavouriteSpotifyPlaylists,
                          private val getFavouriteTracks: GetFavouriteTracks,
                          private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists) : BaseViewModel() {

    val albums: MutableLiveData<List<Album>> = MutableLiveData()
    val artists: MutableLiveData<List<Artist>> = MutableLiveData()
    val categories: MutableLiveData<List<Category>> = MutableLiveData()
    val spotifyPlaylists: MutableLiveData<List<Playlist>> = MutableLiveData()
    val tracks: MutableLiveData<List<Track>> = MutableLiveData()

    val videoPlaylists: MutableLiveData<List<VideoPlaylist>> = MutableLiveData()

    fun loadFavourites() {
        loadAlbums()
        loadArtists()
        loadCategories()
        loadSpotifyPlaylists()
        loadTracks()

        loadVideoPlaylists()
    }

    private fun loadAlbums() {
        addDisposable(getFavouriteAlbums.execute().subscribe({
            albums.value = it.map(AlbumEntityMapper::mapFrom)
        }, { Log.e(javaClass.name, "Albums load error") }))
    }

    private fun loadArtists() {
        addDisposable(getFavouriteArtists.execute().subscribe({
            artists.value = it.map(ArtistEntityMapper::mapFrom)
        }, { Log.e(javaClass.name, "Artists load error") }))
    }

    private fun loadCategories() {
        addDisposable(getFavouriteCategories.execute().subscribe({
            categories.value = it.map(CategoryEntityMapper::mapFrom)
        }, {
            Log.e(javaClass.name, "Categories load error")
        }))
    }

    private fun loadSpotifyPlaylists() {
        addDisposable(getFavouriteSpotifyPlaylists.execute().subscribe({
            spotifyPlaylists.value = it.map(PlaylistEntityMapper::mapFrom)
        }, {
            Log.e(javaClass.name, "SpotifyPlaylists load error")
        }))
    }

    private fun loadTracks() {
        addDisposable(getFavouriteTracks.execute().subscribe({
            tracks.value = it.map(TrackEntityMapper::mapFrom)
        }, { Log.e(javaClass.name, "Tracks load error") }))
    }

    private fun loadVideoPlaylists() {
        addDisposable(getFavouriteVideoPlaylists.execute().subscribe({
            videoPlaylists.value = it.map(VideoPlaylistEntityMapper::mapFrom)
        }, { Log.e(javaClass.name, "VideoPlaylists load error") }))
    }
}