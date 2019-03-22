package com.example.coreandroid.view.recyclerview.item

import com.example.coreandroid.view.imageview.ImageViewSrc

interface NamedImageListItem {
    val name: String
    val imageViewSrc: ImageViewSrc
    val foregroundDrawableId: Int
}