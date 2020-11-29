package com.clipfinder.core.soundcloud.di

import com.clipfinder.core.soundcloud.usecase.GetClientId
import com.clipfinder.core.soundcloud.usecase.GetFeaturedTracks
import com.clipfinder.core.soundcloud.usecase.GetMixedSelections
import org.koin.dsl.module

val soundCloudCoreModule = module {
    single { GetClientId(get(), get(), get()) }
    single { GetMixedSelections(get(), get(), get(), get()) }
    single { GetFeaturedTracks(get(), get(), get(), get()) }
}
