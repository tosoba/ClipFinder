package com.example.spotify.album.di

import com.example.spotify.album.data.SpotifyAlbumRepo
import com.example.spotify.album.domain.repo.ISpotifyAlbumRepo
import com.example.spotify.album.domain.usecase.GetTracksFromAlbum
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyAlbumModule = module {
    single { GetTracksFromAlbum(get(), get(), get()) }

    single { SpotifyAlbumRepo(get(), get()) } bind ISpotifyAlbumRepo::class
}
