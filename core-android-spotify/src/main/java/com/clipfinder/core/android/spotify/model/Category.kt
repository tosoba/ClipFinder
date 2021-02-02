package com.clipfinder.core.android.spotify.model

import android.os.Parcelable
import com.clipfinder.core.spotify.ext.firstImageUrl
import com.clipfinder.core.spotify.model.ISpotifyCategory
import com.clipfinder.core.android.ImageListItemBindingModel_
import com.clipfinder.core.android.R
import com.clipfinder.core.android.view.imageview.ImageViewSrc
import com.clipfinder.core.android.view.recyclerview.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    override val id: String,
    override val name: String,
    override val icons: List<Image>,
    override val href: String
) : ISpotifyCategory,
    Parcelable,
    NamedImageListItem {

    constructor(other: ISpotifyCategory) : this(
        other.id,
        other.name,
        other.icons.map(::Image),
        other.href
    )

    val iconUrl: String
        get() = icons.firstImageUrl()

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.category_placeholder, R.drawable.error_placeholder)

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple
}

fun Category.clickableListItem(
    itemClicked: () -> Unit
): _root_ide_package_.com.clipfinder.core.android.ImageListItemBindingModel_ = _root_ide_package_.com.clipfinder.core.android.ImageListItemBindingModel_()
    .id(id)
    .foregroundDrawableId(R.drawable.spotify_foreground_ripple)
    .imageUrl(iconUrl)
    .errorDrawableId(R.drawable.error_placeholder)
    .fallbackDrawableId(R.drawable.category_placeholder)
    .loadingDrawableId(R.drawable.category_placeholder)
    .label(name)
    .itemClicked { _ -> itemClicked() }
