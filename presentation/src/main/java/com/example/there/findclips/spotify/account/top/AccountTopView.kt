package com.example.there.findclips.spotify.account.top

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList

class AccountTopView(
        val state: AccountTopViewState,
        val artistsAndTracksAdapter: ArtistsAndTracksAdapter
)

class AccountTopViewState(
        val userLoggedIn: ObservableField<Boolean>,
        val tracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracks: ObservableList<Track> = ObservableArrayList(),
        val artists: ObservableList<Artist> = ObservableArrayList(),
        val tracksLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val artistsLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)