package com.example.there.findclips.fragments.favourites.spotify

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.*

class SpotifyFavouritesVMFactory(
        private val getFavouriteAlbums: GetFavouriteAlbums,
        private val getFavouriteArtists: GetFavouriteArtists,
        private val getFavouriteCategories: GetFavouriteCategories,
        private val getFavouriteSpotifyPlaylists: GetFavouriteSpotifyPlaylists,
        private val getFavouriteTracks: GetFavouriteTracks
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SpotifyFavouritesViewModel(
            getFavouriteAlbums,
            getFavouriteArtists,
            getFavouriteCategories,
            getFavouriteSpotifyPlaylists,
            getFavouriteTracks
    ) as T
}