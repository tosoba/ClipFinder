package com.clipfinder.core.android.model.soundcloud

import android.os.Parcelable
import com.clipfinder.core.android.ImageListItemBindingModel_
import com.clipfinder.core.android.R
import com.clipfinder.core.android.view.imageview.ImageViewSrc
import com.clipfinder.core.android.view.recyclerview.item.NamedImageListItem
import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylist
import kotlinx.android.parcel.Parcelize

sealed class BaseSoundCloudPlaylist : Parcelable, NamedImageListItem {
    abstract val artworkUrl: String?
    abstract val title: String
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
    override val createdAt: String?,
    override val duration: Int,
    override val id: String,
    override val likesCount: Int,
    override val publishedAt: String?,
    override val title: String,
    override val trackCount: Int,
    override val userId: Int
) : ISoundCloudPlaylist, BaseSoundCloudPlaylist() {

    constructor(
        other: ISoundCloudPlaylist
    ) : this(
        other.artworkUrl,
        other.createdAt,
        other.duration,
        other.id,
        other.likesCount,
        other.publishedAt,
        other.title,
        other.trackCount,
        other.userId
    )

    override val name: String
        get() = super.name
}

fun SoundCloudPlaylist.clickableListItem(itemClicked: () -> Unit): ImageListItemBindingModel_ =
    ImageListItemBindingModel_()
        .id(id)
        .foregroundDrawableId(R.drawable.sound_cloud_foreground_ripple)
        .imageUrl(artworkUrl)
        .errorDrawableId(R.drawable.error_placeholder)
        .fallbackDrawableId(R.drawable.playlist_placeholder)
        .loadingDrawableId(R.drawable.playlist_placeholder)
        .label(name)
        .itemClicked { _ -> itemClicked() }

@Parcelize
data class SoundCloudSystemPlaylist(
    override val artworkUrl: String?,
    val description: String,
    val id: String,
    val shortDescription: String,
    override val title: String,
    val trackIds: List<Int>
) : BaseSoundCloudPlaylist() {
    override val name: String
        get() = super.name
}

fun SoundCloudSystemPlaylist.clickableListItem(
    itemClicked: () -> Unit
): ImageListItemBindingModel_ =
    ImageListItemBindingModel_()
        .id(id)
        .foregroundDrawableId(R.drawable.sound_cloud_foreground_ripple)
        .imageUrl(artworkUrl)
        .errorDrawableId(R.drawable.error_placeholder)
        .fallbackDrawableId(R.drawable.playlist_placeholder)
        .loadingDrawableId(R.drawable.playlist_placeholder)
        .label(name)
        .itemClicked { _ -> itemClicked() }
