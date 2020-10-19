package com.example.core.android.spotify.fragment

import com.example.core.android.spotify.model.Track

interface ISpotifyTrackFragment {
    fun onNewTrack(track: Track)
}