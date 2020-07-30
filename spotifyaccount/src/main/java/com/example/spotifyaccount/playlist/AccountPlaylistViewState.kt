package com.example.spotifyaccount.playlist

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import com.example.core.android.model.spotify.Playlist

class AccountPlaylistViewState(
    val userLoggedIn: LiveData<Boolean>,
    val playlistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
    val playlists: ArrayList<Playlist> = ArrayList()
)
