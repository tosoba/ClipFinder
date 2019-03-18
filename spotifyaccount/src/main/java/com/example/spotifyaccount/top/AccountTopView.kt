package com.example.spotifyaccount.top

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.coreandroid.model.spotify.Artist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.view.recyclerview.adapter.ArtistsAndTracksAdapter

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