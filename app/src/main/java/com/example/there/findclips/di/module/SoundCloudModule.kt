package com.example.there.findclips.di.module

import com.example.there.domain.usecase.soundcloud.*
import org.koin.dsl.module

val soundCloudModule = module {
    factory { DiscoverSoundCloud(get(), get()) }
    factory { GetSimilarTracks(get(), get()) }
    factory { GetTracks(get(), get()) }
    factory { GetTracksFromPlaylist(get(), get()) }
    factory { GetFavouriteSoundCloudTracks(get(), get()) }
    factory { InsertSoundCloudTrack(get(), get()) }
    factory { IsSoundCloudTrackSaved(get(), get()) }
    factory { DeleteSoundCloudTrack(get(), get()) }
}