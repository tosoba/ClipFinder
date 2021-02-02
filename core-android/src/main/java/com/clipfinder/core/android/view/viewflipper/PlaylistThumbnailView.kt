package com.clipfinder.core.android.view.viewflipper

import android.view.View
import com.clipfinder.core.android.model.videos.VideoPlaylist

class PlaylistThumbnailView(
    val playlist: VideoPlaylist,
    val adapter: PlaylistThumbnailFlipperAdapter,
    val onRemoveBtnClickListener: View.OnClickListener
)
