package com.example.spotifyaccount.top.di

import com.example.spotifyaccount.top.data.SpotifyAccountRepo
import com.example.spotifyaccount.top.domain.repo.ISpotifyAccountRepo
import com.example.spotifyaccount.top.domain.usecase.GetCurrentUsersTopArtists
import com.example.spotifyaccount.top.domain.usecase.GetCurrentUsersTopTracks
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyAccountTopModule = module {
    single { GetCurrentUsersTopArtists(get(), get()) }
    single { GetCurrentUsersTopTracks(get(), get()) }

    single { SpotifyAccountRepo(get(), get()) } bind ISpotifyAccountRepo::class
}
