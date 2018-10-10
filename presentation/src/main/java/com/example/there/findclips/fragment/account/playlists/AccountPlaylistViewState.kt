package com.example.there.findclips.fragment.account.playlists

import android.databinding.ObservableField
import com.example.there.findclips.model.entity.Playlist

class AccountPlaylistViewState(
        val userLoggedIn: ObservableField<Boolean>,
        val playlistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val playlists: ArrayList<Playlist> = ArrayList()
)