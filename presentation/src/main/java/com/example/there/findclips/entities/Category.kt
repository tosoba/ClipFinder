package com.example.there.findclips.entities

import android.annotation.SuppressLint
import io.mironov.smuggler.AutoParcelable


@SuppressLint("ParcelCreator")
data class Category(
        val id: String,
        val name: String,
        val iconUrl: String
) : AutoParcelable