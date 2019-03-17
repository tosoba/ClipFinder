package com.example.coreandroid.model.spotify

import android.os.Parcelable
import com.example.coreandroid.R
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.view.imageview.ImageViewSrc
import com.example.coreandroid.view.list.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
        override val id: String,
        override val name: String,
        val popularity: Int,
        val iconUrl: String
) : Parcelable, NamedImageListItem, IdentifiableNamedObservableListItem<String> {

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.artist_placeholder, R.drawable.error_placeholder)
}

@Parcelize
data class SimpleArtist(
        val id: String,
        val name: String
) : Parcelable