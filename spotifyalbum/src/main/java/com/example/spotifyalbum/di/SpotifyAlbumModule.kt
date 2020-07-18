package com.example.spotifyalbum.di

import com.example.spotifyalbum.data.SpotifyAlbumRepo
import com.example.spotifyalbum.domain.repo.ISpotifyAlbumRepo
import com.example.spotifyalbum.domain.usecase.GetTracksFromAlbum
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyAlbumModule = module {
    single { GetTracksFromAlbum(get(), get()) }

    single { SpotifyAlbumRepo(get(), get(), get()) } bind ISpotifyAlbumRepo::class
}
