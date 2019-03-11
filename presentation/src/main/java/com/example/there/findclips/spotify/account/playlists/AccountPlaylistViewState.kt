package com.example.there.findclips.spotify.account.playlists

import android.databinding.ObservableField
import com.example.there.findclips.model.entity.spotify.Playlist

class AccountPlaylistViewState(
        val userLoggedIn: ObservableField<Boolean>,
        val playlistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val playlists: ArrayList<Playlist> = ArrayList()
)