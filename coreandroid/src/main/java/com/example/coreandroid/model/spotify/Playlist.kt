package com.example.coreandroid.model.spotify

import android.os.Parcelable
import android.view.View
import com.example.coreandroid.GridPlaylistItemBindingModel_
import com.example.coreandroid.ImageListItemBindingModel_
import com.example.coreandroid.R
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.view.imageview.ImageViewSrc
import com.example.coreandroid.view.recyclerview.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Playlist(
        override val id: String,
        override val name: String,
        val iconUrl: String,
        val userId: String,
        val uri: String
) : Parcelable, NamedImageListItem, IdentifiableNamedObservableListItem<String> {

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.playlist_placeholder, R.drawable.error_placeholder)
}

fun Playlist.clickableListItem(
        itemClicked: () -> Unit
): ImageListItemBindingModel_ = ImageListItemBindingModel_()
        .id(id)
        .foregroundDrawableId(R.drawable.spotify_foreground_ripple)
        .imageUrl(iconUrl)
        .errorDrawableId(R.drawable.error_placeholder)
        .fallbackDrawableId(R.drawable.playlist_placeholder)
        .loadingDrawableId(R.drawable.playlist_placeholder)
        .label(name)
        .showGradient(false)
        .itemClicked(View.OnClickListener { itemClicked() })

fun Playlist.clickableGridListItem(
        itemClicked: () -> Unit
): GridPlaylistItemBindingModel_ = GridPlaylistItemBindingModel_()
        .id(id)
        .playlist(this)
        .itemClicked(View.OnClickListener { itemClicked() })