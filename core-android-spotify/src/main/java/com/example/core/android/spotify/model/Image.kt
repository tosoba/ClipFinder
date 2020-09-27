package com.example.core.android.spotify.model

import android.os.Parcelable
import com.clipfinder.core.spotify.model.IImage
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    override val height: Int?,
    override val url: String,
    override val width: Int?
) : Parcelable,
    IImage {
    constructor(other: IImage) : this(other.height, other.url, other.width)
}
