package com.example.core.android.view.recyclerview.item

import com.example.core.android.view.imageview.ImageViewSrc

interface NamedImageListItem {
    val name: String
    val imageViewSrc: ImageViewSrc
    val foregroundDrawableId: Int
}
