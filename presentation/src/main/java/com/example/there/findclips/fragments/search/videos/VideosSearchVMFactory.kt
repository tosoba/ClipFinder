package com.example.there.findclips.fragments.search.videos

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecases.videos.SearchVideos

class VideosSearchVMFactory(private val searchVideos: SearchVideos,
                            private val getChannelsThumbnailUrls: GetChannelsThumbnailUrls) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VideosSearchViewModel(searchVideos, getChannelsThumbnailUrls) as T
    }
}