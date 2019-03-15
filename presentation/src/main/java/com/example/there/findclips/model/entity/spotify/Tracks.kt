package com.example.there.findclips.model.entity.spotify

import android.os.Parcelable
import com.example.there.findclips.R
import com.example.there.findclips.util.list.IdentifiableNamedObservableListItem
import com.example.there.findclips.util.list.IdentifiableNumberedObservableListItem
import com.example.there.findclips.view.imageview.ImageViewSrc
import com.example.there.findclips.view.list.item.NamedImageListItem
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
) : Parcelable, NamedImageListItem, IdentifiableNamedObservableListItem<String>, IdentifiableNumberedObservableListItem<String> {

    override val number: Int
        get() = trackNumber

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.track_placeholder, R.drawable.error_placeholder)

    val query: String
        get() = "$name $albumName"
}

@Parcelize
data class TopTrack(val position: Int, val track: Track) : Parcelable, IdentifiableNumberedObservableListItem<String> {

    override val id: String
        get() = track.id

    override val number: Int
        get() = position
}