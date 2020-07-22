package com.example.spotify.playlist.di

import com.example.spotify.playlist.data.SpotifyPlaylistRepo
import com.example.spotify.playlist.domain.repo.ISpotifyPlaylistRepo
import com.example.spotify.playlist.domain.usecase.GetPlaylistTracks
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyPlaylistModule = module {
    single { GetPlaylistTracks(get(), get()) }

    single { SpotifyPlaylistRepo(get(), get()) } bind ISpotifyPlaylistRepo::class
}
