package com.clipfinder.core.android.spotify.fragment

import com.clipfinder.core.android.spotify.model.Track

interface ISpotifyTrackFragment {
    fun onNewTrack(track: Track)
}
