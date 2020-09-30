package com.example.spotify.account.playlist.di

import com.example.spotify.account.playlist.data.SpotifyAccountPlaylistsRepo
import com.example.spotify.account.playlist.domain.repo.ISpotifyAccountPlaylistsRepo
import com.example.spotify.account.playlist.domain.usecase.GetCurrentUsersPlaylists
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyAccountPlaylistsModule = module {
    single { GetCurrentUsersPlaylists(get(), get(), get()) }

    single { SpotifyAccountPlaylistsRepo(get()) } bind ISpotifyAccountPlaylistsRepo::class
}
