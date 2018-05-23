package com.example.there.findclips.fragments.search.videos

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecases.videos.GetFavouriteVideosFromPlaylist
import com.example.there.domain.usecases.videos.SearchVideos

class VideosSearchVMFactory(private val searchVideos: SearchVideos,
                            private val getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
                            private val getFavouriteVideosFromPlaylist: GetFavouriteVideosFromPlaylist) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            VideosSearchViewModel(searchVideos, getChannelsThumbnailUrls, getFavouriteVideosFromPlaylist) as T
}