package com.example.spotifyaccount.saved

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Track

class AccountSavedView(
        val state: AccountSavedViewState,
        val adapter: AccountSavedAdapter
)

class AccountSavedViewState(
        val userLoggedIn: ObservableField<Boolean>,
        val tracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val albumsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val tracks: ObservableList<Track> = ObservableArrayList(),
        val albums: ObservableList<Album> = ObservableArrayList(),
        val tracksLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
        val albumsLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)