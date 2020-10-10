package com.clipfinder.core.spotify.di

import com.clipfinder.core.spotify.usecase.GetAlbum
import com.clipfinder.core.spotify.usecase.GetArtists
import org.koin.dsl.module

val spotifyCoreModule = module {
    single { GetAlbum(get(), get(), get()) }
    single { GetArtists(get(), get(), get()) }
}