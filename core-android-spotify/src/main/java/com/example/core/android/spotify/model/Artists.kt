package com.example.core.android.spotify.model

import android.os.Parcelable
import android.view.View
import com.clipfinder.core.spotify.ext.firstImageUrl
import com.clipfinder.core.spotify.model.ISpotifyArtist
import com.clipfinder.core.spotify.model.ISpotifySimplifiedArtist
import com.example.core.android.ImageListItemBindingModel_
import com.example.core.android.R
import com.example.core.android.util.list.IdentifiableNamedObservableListItem
import com.example.core.android.view.imageview.ImageViewSrc
import com.example.core.android.view.recyclerview.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
    override val id: String,
    override val name: String,
    override val href: String,
    override val popularity: Int,
    override val images: List<Image>
) : Parcelable,
    ISpotifyArtist,
    NamedImageListItem,
    IdentifiableNamedObservableListItem<String> {

    constructor(other: ISpotifyArtist) : this(
        other.id, other.name, other.href, other.popularity, other.images.map(::Image)
    )

    val iconUrl: String
        get() = images.firstImageUrl()

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.artist_placeholder, R.drawable.error_placeholder)
}

fun Artist.clickableListItem(
    itemClicked: () -> Unit
): ImageListItemBindingModel_ = ImageListItemBindingModel_()
    .id(id)
    .foregroundDrawableId(R.drawable.spotify_foreground_ripple)
    .imageUrl(iconUrl)
    .errorDrawableId(R.drawable.error_placeholder)
    .fallbackDrawableId(R.drawable.artist_placeholder)
    .loadingDrawableId(R.drawable.artist_placeholder)
    .label(name)
    .itemClicked { _ -> itemClicked() }

@Parcelize
data class SimplifiedArtist(
    override val id: String,
    override val name: String,
    override val href: String
) : Parcelable,
    ISpotifySimplifiedArtist {
    constructor(other: ISpotifySimplifiedArtist) : this(other.id, other.name, other.href)
}
