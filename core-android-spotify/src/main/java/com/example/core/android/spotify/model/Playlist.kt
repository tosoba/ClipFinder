package com.example.core.android.spotify.model

import android.os.Parcelable
import android.view.View
import com.clipfinder.core.spotify.ext.firstImageUrl
import com.clipfinder.core.spotify.model.ISpotifySimplifiedPlaylist
import com.example.core.android.ImageListItemBindingModel_
import com.example.core.android.R
import com.example.core.android.spotify.GridPlaylistItemBindingModel_
import com.example.core.android.util.list.IdentifiableNamedObservableListItem
import com.example.core.android.view.imageview.ImageViewSrc
import com.example.core.android.view.recyclerview.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Playlist(
    override val id: String,
    override val name: String,
    override val uri: String,
    override val href: String,
    override val description: String?,
    override val images: List<Image>
) : Parcelable,
    ISpotifySimplifiedPlaylist,
    NamedImageListItem,
    IdentifiableNamedObservableListItem<String> {

    constructor(other: ISpotifySimplifiedPlaylist) : this(
        other.id, other.name, other.uri, other.href, other.description, other.images.map { Image(it) }
    )

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.playlist_placeholder, R.drawable.error_placeholder)

    val iconUrl: String
        get() = images.firstImageUrl()
}

fun Playlist.clickableListItem(
    itemClicked: () -> Unit
): ImageListItemBindingModel_ = ImageListItemBindingModel_()
    .id(id)
    .foregroundDrawableId(R.drawable.spotify_foreground_ripple)
    .imageUrl(iconUrl)
    .errorDrawableId(R.drawable.error_placeholder)
    .fallbackDrawableId(R.drawable.playlist_placeholder)
    .loadingDrawableId(R.drawable.playlist_placeholder)
    .label(name)
    .showGradient(false)
    .itemClicked(View.OnClickListener { itemClicked() })

fun Playlist.clickableGridListItem(
    itemClicked: () -> Unit
): GridPlaylistItemBindingModel_ = GridPlaylistItemBindingModel_()
    .id(id)
    .playlist(this)
    .itemClicked(View.OnClickListener { itemClicked() })
