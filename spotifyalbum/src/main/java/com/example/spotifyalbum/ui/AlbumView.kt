package com.example.spotifyalbum.ui

import android.view.View
import com.example.coreandroid.model.spotify.Album

class AlbumView(
    val album: Album,
    val onFavouriteBtnClickListener: View.OnClickListener
)
