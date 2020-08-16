package com.example.spotifyapi.model

class SpotifyErrorApiModel(override val message: String, val status: Int) : Throwable(message)
