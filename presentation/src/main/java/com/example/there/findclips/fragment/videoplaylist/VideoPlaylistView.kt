package com.example.there.findclips.fragment.videoplaylist

import android.view.View
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.view.viewflipper.PlaylistThumbnailFlipperAdapter

class VideoPlaylistView(
        val state: VideoPlaylistViewState,
        val viewFlipperAdapter: PlaylistThumbnailFlipperAdapter,
        val onPlayBtnClickListener: View.OnClickListener
)

class VideoPlaylistViewState(
        val playlist: VideoPlaylist
)