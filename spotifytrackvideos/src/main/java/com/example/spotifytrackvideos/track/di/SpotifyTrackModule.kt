package com.example.spotifytrackvideos.track.di

import com.example.spotifytrackvideos.track.domain.GetAlbum
import org.koin.dsl.module

val spotifyTrackModule = module {
    single { GetAlbum(get(), get()) }
}
