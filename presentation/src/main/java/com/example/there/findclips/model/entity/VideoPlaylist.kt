package com.example.there.findclips.model.entity

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable

@SuppressLint("ParcelCreator")
data class VideoPlaylist(val id: Long = 0, val name: String): AutoParcelable