package com.example.core.android.model.soundcloud

import android.os.Parcelable
import android.view.View
import com.example.core.android.ImageListItemBindingModel_
import com.example.core.android.R
import com.example.core.android.base.model.BaseTrackUiModel
import com.example.core.android.mapper.soundcloud.domain
import com.example.core.android.util.list.IdentifiableNamedObservableListItem
import com.example.core.android.view.imageview.ImageViewSrc
import com.example.core.android.view.recyclerview.item.NamedImageListItem
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoundCloudTrack(
    override val id: String,
    val title: String,
    val artworkUrl: String?,
    val description: String?,
    val duration: Int,
    val genre: String?,
    val tags: String?,
    val streamUrl: String?,
    val downloadUrl: String?,
    val waveformUrl: String?
) : Parcelable,
    NamedImageListItem,
    IdentifiableNamedObservableListItem<String>,
    BaseTrackUiModel<SoundCloudTrackEntity> {
    override val name: String get() = title
    override val imageViewSrc: ImageViewSrc get() = ImageViewSrc.with(artworkUrl, R.drawable.track_placeholder)
    override val foregroundDrawableId: Int get() = R.drawable.sound_cloud_foreground_ripple
    override val domainEntity: SoundCloudTrackEntity get() = domain
}

fun SoundCloudTrack.clickableListItem(
    itemClicked: () -> Unit
): ImageListItemBindingModel_ = ImageListItemBindingModel_()
    .id(id)
    .foregroundDrawableId(R.drawable.sound_cloud_foreground_ripple)
    .imageUrl(artworkUrl)
    .errorDrawableId(R.drawable.error_placeholder)
    .fallbackDrawableId(R.drawable.track_placeholder)
    .loadingDrawableId(R.drawable.track_placeholder)
    .label(name)
    .itemClicked(View.OnClickListener { itemClicked() })
