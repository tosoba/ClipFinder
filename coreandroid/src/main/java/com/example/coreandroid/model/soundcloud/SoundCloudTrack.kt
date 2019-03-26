package com.example.coreandroid.model.soundcloud

import android.os.Parcelable
import com.example.coreandroid.R
import com.example.coreandroid.base.model.BaseTrackUiModel
import com.example.coreandroid.mapper.soundcloud.domain
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.view.imageview.ImageViewSrc
import com.example.coreandroid.view.recyclerview.item.NamedImageListItem
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoundCloudTrack(
        override val id: String,
        val title: String,
        val artworkUrl: String?,
        val description: String?,
        val duration: String,
        val genre: String?,
        val tags: String?,
        val streamUrl: String?,
        val downloadUrl: String?,
        val waveformUrl: String?
) : Parcelable,
        NamedImageListItem,
        IdentifiableNamedObservableListItem<String>,
        BaseTrackUiModel<SoundCloudTrackEntity> {

    override val name: String
        get() = title

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(artworkUrl, R.drawable.track_placeholder)

    override val foregroundDrawableId: Int
        get() = R.drawable.sound_cloud_foreground_ripple

    override val domainEntity: SoundCloudTrackEntity
        get() = domain
}