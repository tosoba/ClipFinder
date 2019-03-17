package com.example.there.findclips.spotify.account.playlists

import android.databinding.ObservableField

class AccountPlaylistViewState(
        val userLoggedIn: ObservableField<Boolean>,
        val playlistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val playlists: ArrayList<Playlist> = ArrayList()
)