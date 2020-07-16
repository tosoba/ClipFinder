package com.example.spotifyalbum.di

import com.example.spotifyalbum.domain.usecase.GetTracksFromAlbum
import org.koin.dsl.module

val spotifyAlbumModule = module {
    single { GetTracksFromAlbum(get(), get()) }
}
