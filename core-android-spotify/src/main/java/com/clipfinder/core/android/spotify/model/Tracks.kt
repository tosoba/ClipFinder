package com.clipfinder.core.android.spotify.model

import android.os.Parcelable
import com.clipfinder.core.android.ImageListItemBindingModel_
import com.clipfinder.core.android.R
import com.clipfinder.core.android.view.imageview.ImageViewSrc
import com.clipfinder.core.android.view.recyclerview.item.NamedImageListItem
import com.clipfinder.core.spotify.model.ISpotifyTrack
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    override val id: String,
    override val name: String,
    override val artists: List<SimplifiedArtist>,
    override val popularity: Int,
    override val trackNumber: Int,
    override val uri: String,
    override val durationMs: Int,
    override val album: Album,
    override val previewUrl: String?
) : Parcelable, ISpotifyTrack, NamedImageListItem {

    constructor(
        other: ISpotifyTrack
    ) : this(
        other.id,
        other.name,
        other.artists.map(::SimplifiedArtist),
        other.popularity,
        other.trackNumber,
        other.uri,
        other.durationMs,
        Album(other.album),
        other.previewUrl
    )

    val iconUrl: String
        get() = album.iconUrl

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple

    override val imageViewSrc: ImageViewSrc
        get() =
            ImageViewSrc.with(iconUrl, R.drawable.track_placeholder, R.drawable.error_placeholder)

    val query: String
        get() = "$name ${album.name}"
}

fun Track.clickableListItem(itemClicked: () -> Unit): ImageListItemBindingModel_ =
    ImageListItemBindingModel_()
        .id(id)
        .foregroundDrawableId(R.drawable.spotify_foreground_ripple)
        .imageUrl(iconUrl)
        .errorDrawableId(R.drawable.error_placeholder)
        .fallbackDrawableId(R.drawable.track_placeholder)
        .loadingDrawableId(R.drawable.track_placeholder)
        .label(name)
        .itemClicked { _ -> itemClicked() }

@Parcelize data class TopTrack(val position: Int, val track: Track) : Parcelable
