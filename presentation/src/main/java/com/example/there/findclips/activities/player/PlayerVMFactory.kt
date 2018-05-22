package com.example.there.findclips.activities.player

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.videos.AddVideosToPlaylist
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecases.videos.SearchRelatedVideos

@Suppress("UNCHECKED_CAST")
class PlayerVMFactory(private val searchRelatedVideos: SearchRelatedVideos,
                      private val getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
                      private val addVideosToPlaylist: AddVideosToPlaylist): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            PlayerViewModel(searchRelatedVideos, getChannelsThumbnailUrls, addVideosToPlaylist) as T
}