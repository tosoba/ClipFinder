package com.example.spotify.account.top.di

import com.example.spotify.account.top.data.SpotifyAccountTopRepo
import com.example.spotify.account.top.domain.repo.ISpotifyAccountTopRepo
import com.example.spotify.account.top.domain.usecase.GetCurrentUsersTopArtists
import com.example.spotify.account.top.domain.usecase.GetCurrentUsersTopTracks
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyAccountTopModule = module {
    single { GetCurrentUsersTopArtists(get(), get()) }
    single { GetCurrentUsersTopTracks(get(), get()) }

    single { SpotifyAccountTopRepo(get(), get()) } bind ISpotifyAccountTopRepo::class
}
