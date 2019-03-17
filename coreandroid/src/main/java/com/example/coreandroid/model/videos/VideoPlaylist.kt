package com.example.coreandroid.model.videos

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoPlaylist(val id: Long? = null, val name: String) : Parcelable