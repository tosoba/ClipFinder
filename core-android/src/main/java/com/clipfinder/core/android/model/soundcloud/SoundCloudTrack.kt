package com.clipfinder.core.android.model.soundcloud

import android.os.Parcelable
import com.clipfinder.core.android.ImageListItemBindingModel_
import com.clipfinder.core.android.R
import com.clipfinder.core.android.view.imageview.ImageViewSrc
import com.clipfinder.core.android.view.recyclerview.item.NamedImageListItem
import com.clipfinder.core.soundcloud.model.ISoundCloudTrack
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoundCloudTrack(
    override val id: Int,
    override val title: String,
    override val artworkUrl: String?,
    override val description: String,
    override val duration: Int,
    override val genre: String,
    override val tags: String,
    override val streamUrl: String?,
    override val waveformUrl: String
) : Parcelable, NamedImageListItem, ISoundCloudTrack {
    constructor(
        other: ISoundCloudTrack
    ) : this(
        other.id,
        other.title,
        other.artworkUrl,
        other.description,
        other.duration,
        other.genre,
        other.tags,
        other.streamUrl,
        other.waveformUrl
    )

    override val name: String
        get() = title

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(artworkUrl, R.drawable.track_placeholder)

    override val foregroundDrawableId: Int
        get() = R.drawable.sound_cloud_foreground_ripple
}

fun SoundCloudTrack.clickableListItem(itemClicked: () -> Unit): ImageListItemBindingModel_ =
    ImageListItemBindingModel_()
        .id(id)
        .foregroundDrawableId(R.drawable.sound_cloud_foreground_ripple)
        .imageUrl(artworkUrl)
        .errorDrawableId(R.drawable.error_placeholder)
        .fallbackDrawableId(R.drawable.track_placeholder)
        .loadingDrawableId(R.drawable.track_placeholder)
        .label(name)
        .itemClicked { _ -> itemClicked() }
