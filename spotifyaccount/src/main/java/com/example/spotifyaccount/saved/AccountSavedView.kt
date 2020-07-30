package com.example.spotifyaccount.saved

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.LiveData
import com.example.core.android.model.spotify.Album
import com.example.core.android.model.spotify.Track

class AccountSavedView(
    val state: AccountSavedViewState,
    val adapter: AccountSavedAdapter
)

class AccountSavedViewState(
    val userLoggedIn: LiveData<Boolean>,
    val tracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
    val albumsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
    val tracks: ObservableList<Track> = ObservableArrayList(),
    val albums: ObservableList<Album> = ObservableArrayList(),
    val tracksLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false),
    val albumsLoadingErrorOccurred: ObservableField<Boolean> = ObservableField(false)
)
