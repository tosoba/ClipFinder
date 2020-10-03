package com.example.core.android.spotify.model

import android.os.Parcelable
import android.view.View
import com.clipfinder.core.spotify.model.ISpotifyTrack
import com.example.core.android.ImageListItemBindingModel_
import com.example.core.android.R
import com.example.core.android.util.list.IdentifiableNamedObservableListItem
import com.example.core.android.util.list.IdentifiableNumberedObservableListItem
import com.example.core.android.view.imageview.ImageViewSrc
import com.example.core.android.view.recyclerview.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    override val id: String,
    override val name: String,
    override val artists: List<Artist>,
    override val popularity: Int,
    override val trackNumber: Int,
    override val uri: String,
    override val durationMs: Int,
    override val album: Album,
    override val previewUrl: String?
) : Parcelable,
    ISpotifyTrack,
    NamedImageListItem,
    IdentifiableNamedObservableListItem<String>,
    IdentifiableNumberedObservableListItem<String> {

    val iconUrl: String
        get() = album.iconUrl

    override val number: Int
        get() = trackNumber

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.track_placeholder, R.drawable.error_placeholder)

    val query: String
        get() = "$name ${album.name}"
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
data class TopTrack(
    val position: Int,
    val track: Track
) : Parcelable,
    IdentifiableNumberedObservableListItem<String> {

    override val id: String
        get() = track.id

    override val number: Int
        get() = position
}
