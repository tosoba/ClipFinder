package com.clipfinder.core.spotify.di

import com.clipfinder.core.spotify.usecase.GetAlbum
import org.koin.dsl.module

val spotifyCoreModule = module {
    single { GetAlbum(get(), get()) }
}