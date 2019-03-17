package com.example.coreandroid.view.imageview

interface ImageViewSrc {
    val iconUrl: String?
    val loadingPlaceholder: Int
    val errorPlaceholder: Int

    companion object {
        fun with(iconUrl: String?, loadingPlaceholder: Int, errorPlaceholder: Int): ImageViewSrc = object : ImageViewSrc {
            override val loadingPlaceholder: Int
                get() = loadingPlaceholder
            override val errorPlaceholder: Int
                get() = errorPlaceholder
            override val iconUrl: String?
                get() = iconUrl
        }

        fun with(iconUrl: String?, placeholder: Int): ImageViewSrc = with(iconUrl, placeholder, placeholder)
    }
}