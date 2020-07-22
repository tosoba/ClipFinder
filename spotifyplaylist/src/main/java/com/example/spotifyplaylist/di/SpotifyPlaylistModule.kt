package com.example.spotifyplaylist.di

import com.example.spotifyplaylist.data.SpotifyPlaylistRepo
import com.example.spotifyplaylist.domain.repo.ISpotifyPlaylistRepo
import com.example.spotifyplaylist.domain.usecase.GetPlaylistTracks
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyPlaylistModule = module {
    single { GetPlaylistTracks(get(), get()) }

    single { SpotifyPlaylistRepo(get(), get()) } bind ISpotifyPlaylistRepo::class
}
