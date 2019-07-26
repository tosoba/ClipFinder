package com.example.coreandroid.model.soundcloud

import android.os.Parcelable
import android.view.View
import com.example.coreandroid.ImageListItemBindingModel_
import com.example.coreandroid.R
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.view.imageview.ImageViewSrc
import com.example.coreandroid.view.recyclerview.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

interface ISoundCloudPlaylist : Parcelable, NamedImageListItem {
    val artworkUrl: String?
    val title: String

    override val name: String
        get() = title

    override val foregroundDrawableId: Int
        get() = R.drawable.sound_cloud_foreground_ripple

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(artworkUrl, R.drawable.playlist_placeholder)
}

@Parcelize
data class SoundCloudPlaylist(
        override val artworkUrl: String?,
        val createdAt: String,
        val duration: Int,
        override val id: Int,
        val likesCount: Int,
        val publishedAt: String?,
        override val title: String,
        val trackCount: Int,
        val userId: Int
) : ISoundCloudPlaylist, IdentifiableNamedObservableListItem<Int> {

    override val name: String
        get() = super.name
}

fun SoundCloudPlaylist.clickableListItem(
        itemClicked: () -> Unit
): ImageListItemBindingModel_ = ImageListItemBindingModel_()
        .id(id)
        .foregroundDrawableId(R.drawable.sound_cloud_foreground_ripple)
        .imageUrl(artworkUrl)
        .errorDrawableId(R.drawable.error_placeholder)
        .fallbackDrawableId(R.drawable.playlist_placeholder)
        .loadingDrawableId(R.drawable.playlist_placeholder)
        .label(name)
        .itemClicked(View.OnClickListener { itemClicked() })

@Parcelize
data class SoundCloudSystemPlaylist(
        override val artworkUrl: String?,
        val description: String,
        override val id: String,
        val shortDescription: String,
        override val title: String,
        val trackIds: List<Int>
) : ISoundCloudPlaylist, IdentifiableNamedObservableListItem<String> {

    override val name: String
        get() = super.name
}

fun SoundCloudSystemPlaylist.clickableListItem(
        itemClicked: () -> Unit
): ImageListItemBindingModel_ = ImageListItemBindingModel_()
        .id(id)
        .foregroundDrawableId(R.drawable.sound_cloud_foreground_ripple)
        .imageUrl(artworkUrl)
        .errorDrawableId(R.drawable.error_placeholder)
        .fallbackDrawableId(R.drawable.playlist_placeholder)
        .loadingDrawableId(R.drawable.playlist_placeholder)
        .label(name)
        .itemClicked(View.OnClickListener { itemClicked() })

