package com.example.there.findclips.fragments.relatedvideos

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecases.videos.SearchRelatedVideos

class RelatedVideosVMFactory(private val searchRelatedVideos: SearchRelatedVideos,
                             private val getChannelsThumbnailUrls: GetChannelsThumbnailUrls) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            RelatedVideosViewModel(searchRelatedVideos, getChannelsThumbnailUrls) as T
}