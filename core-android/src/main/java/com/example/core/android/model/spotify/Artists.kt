package com.example.core.android.model.spotify

import android.os.Parcelable
import android.view.View
import com.example.core.android.ImageListItemBindingModel_
import com.example.core.android.R
import com.example.core.android.util.list.IdentifiableNamedObservableListItem
import com.example.core.android.view.imageview.ImageViewSrc
import com.example.core.android.view.recyclerview.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    override val id: String,
    override val name: String,
    val popularity: Int,
    val iconUrl: String
) : Parcelable, NamedImageListItem, IdentifiableNamedObservableListItem<String> {
    override val foregroundDrawableId: Int get() = R.drawable.spotify_foreground_ripple
    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.artist_placeholder, R.drawable.error_placeholder)
}

fun Artist.clickableListItem(
    itemClicked: () -> Unit
): ImageListItemBindingModel_ = ImageListItemBindingModel_()
    .id(id)
    .foregroundDrawableId(R.drawable.spotify_foreground_ripple)
    .imageUrl(iconUrl)
    .errorDrawableId(R.drawable.error_placeholder)
    .fallbackDrawableId(R.drawable.artist_placeholder)
    .loadingDrawableId(R.drawable.artist_placeholder)
    .label(name)
    .itemClicked(View.OnClickListener { itemClicked() })

@Parcelize
data class SimpleArtist(val id: String, val name: String) : Parcelable