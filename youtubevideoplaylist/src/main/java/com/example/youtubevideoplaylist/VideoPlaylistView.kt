package com.example.youtubevideoplaylist

import android.view.View
import com.example.core.android.model.videos.VideoPlaylist
import com.example.core.android.view.viewflipper.PlaylistThumbnailFlipperAdapter

class VideoPlaylistView(
    val state: VideoPlaylistViewState,
    val viewFlipperAdapter: PlaylistThumbnailFlipperAdapter,
    val onPlayBtnClickListener: View.OnClickListener
)

class VideoPlaylistViewState(
    val playlist: VideoPlaylist
)
