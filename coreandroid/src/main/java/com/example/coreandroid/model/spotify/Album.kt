package com.example.coreandroid.model.spotify

import android.os.Parcelable
import android.view.View
import com.example.coreandroid.AlbumInfoItemBindingModel_
import com.example.coreandroid.ImageListItemBindingModel_
import com.example.coreandroid.R
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.view.imageview.ImageViewSrc
import com.example.coreandroid.view.recyclerview.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
    override val id: String,
    override val name: String,
    val artists: List<SimpleArtist>,
    val albumType: String,
    val iconUrl: String,
    val uri: String
) : Parcelable, NamedImageListItem, IdentifiableNamedObservableListItem<String> {
    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.album_placeholder, R.drawable.error_placeholder)

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple
}

fun Album.infoItem(
    itemClicked: () -> Unit
): AlbumInfoItemBindingModel_ = AlbumInfoItemBindingModel_()
    .id(id)
    .foregroundDrawableId(R.drawable.spotify_foreground_ripple)
    .imageUrl(iconUrl)
    .errorDrawableId(R.drawable.error_placeholder)
    .fallbackDrawableId(R.drawable.album_placeholder)
    .loadingDrawableId(R.drawable.album_placeholder)
    .itemClicked(View.OnClickListener { itemClicked() })

fun Album.clickableListItem(
    itemClicked: () -> Unit
): ImageListItemBindingModel_ = ImageListItemBindingModel_()
    .id(id)
    .foregroundDrawableId(R.drawable.spotify_foreground_ripple)
    .imageUrl(iconUrl)
    .errorDrawableId(R.drawable.error_placeholder)
    .fallbackDrawableId(R.drawable.album_placeholder)
    .loadingDrawableId(R.drawable.album_placeholder)
    .label(name)
    .itemClicked(View.OnClickListener { itemClicked() })
