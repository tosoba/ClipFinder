package com.example.core.android.spotify.model.ext

enum class SpotifySearchType(val value: String) {
    ALBUM("album"), ARTIST("artist"), PLAYLIST("playlist"), TRACK("track");

    override fun toString(): String = value

    companion object {
        val ALL = values().joinToString(",") { it.value }
    }
}
