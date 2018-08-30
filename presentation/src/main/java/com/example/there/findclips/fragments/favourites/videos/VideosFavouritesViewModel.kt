package com.example.there.findclips.fragments.favourites.videos

import android.util.Log
import com.example.there.domain.usecases.videos.GetFavouriteVideoPlaylists
import com.example.there.findclips.base.BaseViewModel
import com.example.there.findclips.model.mappers.VideoPlaylistEntityMapper

class VideosFavouritesViewModel(private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists) : BaseViewModel() {

    val state: VideosFavouritesFragmentViewState = VideosFavouritesFragmentViewState()

    fun loadVideoPlaylists() {
        addDisposable(getFavouriteVideoPlaylists.execute().subscribe({
            state.playlists.clear()
            state.playlists.addAll(it.map(VideoPlaylistEntityMapper::mapFrom))
        }, { Log.e(javaClass.name, "VideoPlaylists load error") }))
    }
}