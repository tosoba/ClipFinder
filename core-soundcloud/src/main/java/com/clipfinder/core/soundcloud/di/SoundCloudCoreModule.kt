package com.clipfinder.core.soundcloud.di

import com.clipfinder.core.soundcloud.usecase.*
import org.koin.dsl.module

val soundCloudCoreModule = module {
    single { GetClientId(get(), get()) }
    single { GetMixedSelections(get(), get(), get(), get()) }
    single { GetFeaturedTracks(get(), get(), get(), get()) }
    single { GetSimilarTracks(get(), get(), get(), get()) }
    single { GetTracks(get(), get(), get(), get()) }
    single { GetTracksFromPlaylist(get(), get(), get(), get()) }
}
