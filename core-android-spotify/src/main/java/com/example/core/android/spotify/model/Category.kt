package com.example.core.android.spotify.model

import android.os.Parcelable
import android.view.View
import com.clipfinder.core.spotify.ext.firstImageUrl
import com.clipfinder.core.spotify.model.ICategory
import com.example.core.android.ImageListItemBindingModel_
import com.example.core.android.R
import com.example.core.android.util.list.IdentifiableNamedObservableListItem
import com.example.core.android.view.imageview.ImageViewSrc
import com.example.core.android.view.recyclerview.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    override val id: String,
    override val name: String,
    override val icons: List<Image>,
    override val href: String
) : ICategory,
    Parcelable,
    NamedImageListItem,
    IdentifiableNamedObservableListItem<String> {

    constructor(other: ICategory) : this(
        other.id,
        other.name,
        other.icons.map { Image(it) },
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
): ImageListItemBindingModel_ = ImageListItemBindingModel_()
    .id(id)
    .foregroundDrawableId(R.drawable.spotify_foreground_ripple)
    .imageUrl(iconUrl)
    .errorDrawableId(R.drawable.error_placeholder)
    .fallbackDrawableId(R.drawable.category_placeholder)
    .loadingDrawableId(R.drawable.category_placeholder)
    .label(name)
    .itemClicked(View.OnClickListener { itemClicked() })
