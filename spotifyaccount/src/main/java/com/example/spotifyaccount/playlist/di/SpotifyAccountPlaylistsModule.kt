package com.example.spotifyaccount.playlist.di

import com.example.spotifyaccount.playlist.data.SpotifyAccountPlaylistsRepo
import com.example.spotifyaccount.playlist.domain.repo.ISpotifyAccountPlaylistsRepo
import com.example.spotifyaccount.playlist.domain.usecase.GetCurrentUsersPlaylists
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyAccountPlaylistsModule = module {
    single { GetCurrentUsersPlaylists(get(), get()) }

    single { SpotifyAccountPlaylistsRepo(get(), get()) } bind ISpotifyAccountPlaylistsRepo::class
}
