package com.clipfinder.core.android.soundcloud.model

import android.os.Parcelable
import com.clipfinder.core.soundcloud.model.ISoundCloudPlaylistSelection
import com.clipfinder.core.android.model.soundcloud.SoundCloudPlaylist
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoundCloudPlaylistSelection(
    override val description: String?,
    override val id: String,
    override val playlists: List<SoundCloudPlaylist>,
    override val title: String
) : ISoundCloudPlaylistSelection,
    Parcelable {

    constructor(other: ISoundCloudPlaylistSelection) : this(
        other.description,
        other.id,
        other.playlists.map(::SoundCloudPlaylist),
        other.title
    )
}
