package com.example.there.findclips.activities.player

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.videos.*

@Suppress("UNCHECKED_CAST")
class PlayerVMFactory(private val searchRelatedVideos: SearchRelatedVideos,
                      private val getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
                      private val insertVideoPlaylist: InsertVideoPlaylist,
                      private val addVideoToPlaylist: AddVideoToPlaylist,
                      private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            PlayerViewModel(searchRelatedVideos, getChannelsThumbnailUrls, insertVideoPlaylist, addVideoToPlaylist, getFavouriteVideoPlaylists) as T
}