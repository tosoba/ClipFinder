package com.example.there.findclips.model.entity

import com.example.there.findclips.R
import com.example.there.findclips.view.imageview.ImageViewSrc

data class User(
        val name: String,
        val iconUrl: String
) {
    val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc(iconUrl, R.drawable.user_placeholder, R.drawable.error_placeholder)
}