package com.example.core.android.view.viewflipper

import android.view.View
import com.example.core.android.model.videos.VideoPlaylist

class PlaylistThumbnailView(
    val playlist: VideoPlaylist,
    val adapter: PlaylistThumbnailFlipperAdapter,
    val onRemoveBtnClickListener: View.OnClickListener
)
