package com.example.there.findclips.model.entity.videos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoPlaylist(val id: Long? = null, val name: String) : Parcelable