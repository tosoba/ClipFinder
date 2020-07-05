package com.example.spotifyapi.model

import com.google.gson.annotations.SerializedName

class SpotifyAuthErrorApiModel(
    val error: String,

    @SerializedName("error_description")
    val errorDescription: String
)
