package com.example.soundcloudplaylist

import com.airbnb.mvrx.MvRxState
import com.example.core.android.model.Empty
import com.example.core.android.model.Loadable
import com.example.core.android.model.soundcloud.BaseSoundCloudPlaylist
import com.example.core.android.model.soundcloud.SoundCloudTrack

data class SoundCloudPlaylistState(
    val playlist: BaseSoundCloudPlaylist,
    val tracks: Loadable<List<SoundCloudTrack>>,
    val isSavedAsFavourite: Loadable<Boolean>
) : MvRxState {
    constructor(playlist: BaseSoundCloudPlaylist) : this(playlist, Empty, Empty)
}