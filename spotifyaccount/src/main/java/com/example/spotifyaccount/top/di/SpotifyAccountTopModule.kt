package com.example.spotifyaccount.top.di

import com.example.spotifyaccount.top.data.SpotifyAccountTopRepo
import com.example.spotifyaccount.top.domain.repo.ISpotifyAccountTopRepo
import com.example.spotifyaccount.top.domain.usecase.GetCurrentUsersTopArtists
import com.example.spotifyaccount.top.domain.usecase.GetCurrentUsersTopTracks
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyAccountTopModule = module {
    single { GetCurrentUsersTopArtists(get(), get()) }
    single { GetCurrentUsersTopTracks(get(), get()) }

    single { SpotifyAccountTopRepo(get(), get()) } bind ISpotifyAccountTopRepo::class
}
