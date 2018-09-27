package com.example.there.findclips.fragment.videoplaylist

import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.view.viewflipper.PlaylistThumbnailFlipperAdapter

class VideoPlaylistView(
        val state: VideoPlaylistViewState,
        val viewFlipperAdapter: PlaylistThumbnailFlipperAdapter
)

class VideoPlaylistViewState(
        val playlist: VideoPlaylist
)