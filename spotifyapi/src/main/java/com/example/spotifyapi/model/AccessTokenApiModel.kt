package com.example.spotifyapi.model

import com.google.gson.annotations.SerializedName

class AccessTokenApiModel(@SerializedName("access_token") val accessToken: String)