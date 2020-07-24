package com.example.core.android.model.soundcloud

import android.os.Parcelable
import android.view.View
import com.example.core.android.ImageListItemBindingModel_
import com.example.core.android.R
import com.example.core.android.util.list.IdentifiableNamedObservableListItem
import com.example.core.android.view.imageview.ImageViewSrc
import com.example.core.android.view.recyclerview.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

sealed class BaseSoundCloudPlaylist : Parcelable, NamedImageListItem {
    abstract val artworkUrl: String?
    abstract val title: String
    override val name: String get() = title
    override val foregroundDrawableId: Int get() = R.drawable.sound_cloud_foreground_ripple
    override val imageViewSrc: ImageViewSrc get() = ImageViewSrc.with(artworkUrl, R.drawable.playlist_placeholder)
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
) : BaseSoundCloudPlaylist(), IdentifiableNamedObservableListItem<Int> {
    override val name: String get() = super.name
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
) : BaseSoundCloudPlaylist(), IdentifiableNamedObservableListItem<String> {
    override val name: String get() = super.name
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
