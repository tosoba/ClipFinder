package com.example.youtubefavourites

import android.util.Log
import android.view.View
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.mapper.videos.domain
import com.example.coreandroid.mapper.videos.ui
import com.example.coreandroid.view.viewflipper.PlaylistThumbnailFlipperAdapter
import com.example.coreandroid.view.viewflipper.PlaylistThumbnailView
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.usecase.videos.DeleteVideoPlaylist
import com.example.there.domain.usecase.videos.GetVideoPlaylistsWithThumbnails

class VideosFavouritesViewModel(
        private val getVideoPlaylistsWithThumbnails: GetVideoPlaylistsWithThumbnails,
        private val deleteVideoPlaylist: DeleteVideoPlaylist
) : BaseViewModel() {

    val state: VideosFavouritesFragmentViewState = VideosFavouritesFragmentViewState()

    fun loadVideoPlaylists() {
        getVideoPlaylistsWithThumbnails()
                .subscribeAndDisposeOnCleared({ playlistWithThumbnails ->
                    val playlist = playlistWithThumbnails.playlist.ui
                    state.playlists.add(PlaylistThumbnailView(
                            playlist,
                            PlaylistThumbnailFlipperAdapter(playlistWithThumbnails.thumbnailUrls),
                            View.OnClickListener { _ ->
                                state.playlists.removeAll { it.playlist == playlist }
                                deleteVideoPlaylist(playlist.domain)
                            }
                    ))
                }, { Log.e(javaClass.name, "VideoPlaylists load error") })
    }

    private fun deleteVideoPlaylist(videoPlaylist: VideoPlaylistEntity) {
        deleteVideoPlaylist.invoke(videoPlaylist)
                .subscribeAndDisposeOnCleared()
    }
}