package com.clipfinder.core.youtube.di

import com.clipfinder.core.youtube.usecase.ClearExpiredVideosSearchCache
import com.clipfinder.core.youtube.usecase.SearchRelatedVideos
import com.clipfinder.core.youtube.usecase.SearchVideos
import org.koin.dsl.module

val youtubeCoreModule = module {
    single { SearchVideos(get()) }
    single { SearchRelatedVideos(get()) }
    single { ClearExpiredVideosSearchCache(get()) }
}
