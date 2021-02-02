package com.clipfinder.core.android.view.recyclerview.item

import com.clipfinder.core.android.view.imageview.ImageViewSrc

interface NamedImageListItem {
    val name: String
    val imageViewSrc: ImageViewSrc
    val foregroundDrawableId: Int
}
