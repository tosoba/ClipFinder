package com.clipfinder.core.android.base

import androidx.fragment.app.Fragment
import com.clipfinder.core.android.model.soundcloud.SoundCloudPlaylist
import com.clipfinder.core.android.model.soundcloud.SoundCloudTrack

interface IFragmentFactory {
    val newSpotifyDashboardNavHostFragment: Fragment
    val newSpotifyAccountNavHostFragment: Fragment

    fun newVideosSearchFragment(query: String): Fragment

    val newSoundCloudDashboardNavHostFragment: Fragment

    fun newSoundCloudPlaylistFragmentWithPlaylist(playlist: SoundCloudPlaylist): Fragment
    fun newSoundCloudTrackVideosFragment(track: SoundCloudTrack): Fragment
}
