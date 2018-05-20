package com.example.there.findclips.fragments.favourites

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.spotify.*
import com.example.there.domain.usecases.videos.GetFavouriteVideoPlaylists
import com.example.there.domain.usecases.videos.GetFavouriteVideosFromPlaylist

class FavouritesVMFactory(private val getFavouriteAlbums: GetFavouriteAlbums,
                          private val getFavouriteArtists: GetFavouriteArtists,
                          private val getFavouriteCategories: GetFavouriteCategories,
                          private val getFavouriteSpotifyPlaylists: GetFavouriteSpotifyPlaylists,
                          private val getFavouriteTracks: GetFavouriteTracks,
                          private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists,
                          private val getFavouriteVideosFromPlaylist: GetFavouriteVideosFromPlaylist) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FavouritesViewModel(
            getFavouriteAlbums,
            getFavouriteArtists,
            getFavouriteCategories,
            getFavouriteSpotifyPlaylists,
            getFavouriteTracks,
            getFavouriteVideoPlaylists,
            getFavouriteVideosFromPlaylist
    ) as T
}