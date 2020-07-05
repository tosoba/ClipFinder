package com.example.coreandroid.model.spotify

import com.example.coreandroid.R
import com.example.coreandroid.view.imageview.ImageViewSrc

data class User(
    val name: String,
    val iconUrl: String
) {
    val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.user_placeholder, R.drawable.error_placeholder)
}
