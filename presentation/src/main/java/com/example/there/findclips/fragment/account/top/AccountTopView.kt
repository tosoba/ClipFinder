package com.example.there.findclips.fragment.account.top

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.view.list.adapter.ArtistsAndTracksAdapter

class AccountTopView(
        val state: AccountTopViewState,
        val artistsAndTracksAdapter: ArtistsAndTracksAdapter
)

class AccountTopViewState(
        val userLoggedIn: ObservableField<Boolean>,
        val tracksLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val artistsLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val topTracks: ObservableList<Track> = ObservableArrayList(),
        val artists: ObservableList<Artist> = ObservableArrayList()
)