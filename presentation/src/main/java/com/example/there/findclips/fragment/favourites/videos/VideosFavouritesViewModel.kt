package com.example.there.findclips.fragment.favourites.videos

import android.util.Log
import com.example.there.domain.usecase.videos.GetVideoPlaylistsWithThumbnails
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.VideoPlaylistWithThumbnails
import com.example.there.findclips.model.mapper.VideoPlaylistEntityMapper
import com.example.there.findclips.view.binding.CollageViewState
import javax.inject.Inject

class VideosFavouritesViewModel @Inject constructor(
        private val getVideoPlaylistsWithThumbnails: GetVideoPlaylistsWithThumbnails
) : BaseViewModel() {

    val state: VideosFavouritesFragmentViewState = VideosFavouritesFragmentViewState()

    fun loadVideoPlaylists() {
        addDisposable(getVideoPlaylistsWithThumbnails.execute().subscribe({ playlistWithThumbnails ->
            val playlist = VideoPlaylistEntityMapper.mapFrom(playlistWithThumbnails.playlist)
            state.playlists.removeAll { it.playlist == playlist }
            state.playlists.add(VideoPlaylistWithThumbnails(
                    playlist,
                    CollageViewState(playlistWithThumbnails.thumbnailUrls.toTypedArray()))
            )
        }, { Log.e(javaClass.name, "VideoPlaylists load error") }))
    }
}