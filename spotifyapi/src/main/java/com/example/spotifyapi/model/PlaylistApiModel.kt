package com.example.spotifyapi.model

import com.example.core.model.StringIdModel
import com.example.core.model.StringUrlModel
import com.google.gson.annotations.SerializedName

class PlaylistApiModel(
    val id: String,

    @SerializedName("images")
    val icons: List<StringUrlModel>,

    val name: String,

    val owner: StringIdModel,

    val uri: String
)
