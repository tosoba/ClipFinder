package com.example.coreandroid.model.spotify

import android.os.Parcelable
import android.view.View
import com.example.coreandroid.ImageListItemBindingModel_
import com.example.coreandroid.R
import com.example.coreandroid.base.model.BaseTrackUiModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.util.list.IdentifiableNumberedObservableListItem
import com.example.coreandroid.view.imageview.ImageViewSrc
import com.example.coreandroid.view.recyclerview.item.NamedImageListItem
import com.example.there.domain.entity.spotify.TrackEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
        override val id: String,
        override val name: String,
        val iconUrl: String,
        val albumId: String,
        val albumName: String,
        val artists: List<SimpleArtist>,
        val popularity: Int,
        val trackNumber: Int,
        val uri: String,
        val durationMs: Int
) : Parcelable,
        NamedImageListItem,
        IdentifiableNamedObservableListItem<String>,
        IdentifiableNumberedObservableListItem<String>,
        BaseTrackUiModel<TrackEntity> {

    override val number: Int get() = trackNumber

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.track_placeholder, R.drawable.error_placeholder)

    val query: String get() = "$name $albumName"

    override val domainEntity: TrackEntity get() = domain
}

fun Track.clickableListItem(itemClicked: () -> Unit): ImageListItemBindingModel_ = ImageListItemBindingModel_()
        .id(id)
        .foregroundDrawableId(R.drawable.spotify_foreground_ripple)
        .imageUrl(iconUrl)
        .errorDrawableId(R.drawable.error_placeholder)
        .fallbackDrawableId(R.drawable.track_placeholder)
        .loadingDrawableId(R.drawable.track_placeholder)
        .label(name)
        .itemClicked(View.OnClickListener { itemClicked() })

@Parcelize
data class TopTrack(val position: Int, val track: Track) : Parcelable, IdentifiableNumberedObservableListItem<String> {

    override val id: String
        get() = track.id

    override val number: Int
        get() = position
}