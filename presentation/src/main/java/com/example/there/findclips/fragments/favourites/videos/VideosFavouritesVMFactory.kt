package com.example.there.findclips.fragments.favourites.videos

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.videos.GetFavouriteVideoPlaylists

class VideosFavouritesVMFactory(
        private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = VideosFavouritesViewModel(getFavouriteVideoPlaylists) as T
}