package com.clipfinder.core.spotify.model

enum class SpotifySearchType(val value: String) {
    ALBUM("album"), ARTIST("artist"), PLAYLIST("playlist"), TRACK("track");

    override fun toString(): String = value

    companion object {
        val ALL: String
            get() = values().joinToString(",", transform = SpotifySearchType::value)
    }
}
