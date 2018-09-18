package com.example.there.findclips.fragment.account.saved

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.model.entity.Track

class AccountSavedView(
        val state: AccountSavedViewState,
        val adapter: AccountSavedAdapter
)

data class AccountSavedViewState(
        val userLoggedIn: ObservableField<Boolean>,
        val tracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val albumsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val tracks: ObservableList<Track> = ObservableArrayList(),
        val albums: ObservableList<Album> = ObservableArrayList()
)