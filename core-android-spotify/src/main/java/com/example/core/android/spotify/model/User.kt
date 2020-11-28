package com.example.core.android.spotify.model

import com.example.core.android.R
import com.example.core.android.view.imageview.ImageViewSrc

data class User(val name: String, val iconUrl: String) {
    val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.user_placeholder, R.drawable.error_placeholder)
}
