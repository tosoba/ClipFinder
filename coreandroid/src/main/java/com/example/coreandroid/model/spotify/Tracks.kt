package com.example.coreandroid.model.spotify

import android.os.Parcelable
import com.example.coreandroid.R
import com.example.coreandroid.util.list.IdentifiableNamedObservableListItem
import com.example.coreandroid.util.list.IdentifiableNumberedObservableListItem
import com.example.coreandroid.view.imageview.ImageViewSrc
import com.example.coreandroid.view.list.item.NamedImageListItem
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
) : Parcelable, NamedImageListItem,
        IdentifiableNamedObservableListItem<String>, IdentifiableNumberedObservableListItem<String> {

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