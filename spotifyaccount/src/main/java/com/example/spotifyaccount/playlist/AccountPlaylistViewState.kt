package com.example.spotifyaccount.playlist

import androidx.databinding.ObservableField
import com.example.coreandroid.model.spotify.Playlist

class AccountPlaylistViewState(
        val userLoggedIn: ObservableField<Boolean>,
        val playlistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val playlists: ArrayList<Playlist> = ArrayList()
)