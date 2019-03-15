package com.example.there.findclips.model.entity.spotify

import android.os.Parcelable
import com.example.there.findclips.R
import com.example.there.findclips.util.list.IdentifiableNamedObservableListItem
import com.example.there.findclips.view.imageview.ImageViewSrc
import com.example.there.findclips.view.list.item.NamedImageListItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
        override val id: String,
        override val name: String,
        val iconUrl: String
) : Parcelable, NamedImageListItem, IdentifiableNamedObservableListItem<String> {

    override val imageViewSrc: ImageViewSrc
        get() = ImageViewSrc.with(iconUrl, R.drawable.category_placeholder, R.drawable.error_placeholder)

    override val foregroundDrawableId: Int
        get() = R.drawable.spotify_foreground_ripple
}