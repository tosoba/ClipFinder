package com.example.there.findclips.videos.videoplaylist

import android.view.View
import com.example.coreandroid.view.viewflipper.PlaylistThumbnailFlipperAdapter

class VideoPlaylistView(
        val state: VideoPlaylistViewState,
        val viewFlipperAdapter: PlaylistThumbnailFlipperAdapter,
        val onPlayBtnClickListener: View.OnClickListener
)

class VideoPlaylistViewState(
        val playlist: VideoPlaylist
)