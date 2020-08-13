package com.example.spotifyaccount.saved.di

import com.example.spotifyaccount.saved.data.SpotifyAccountSavedRepo
import com.example.spotifyaccount.saved.domain.repo.ISpotifyAccountSavedRepo
import com.example.spotifyaccount.saved.domain.usecase.GetCurrentUsersSavedAlbums
import com.example.spotifyaccount.saved.domain.usecase.GetCurrentUsersSavedTracks
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyAccountSavedModule = module {
    single { GetCurrentUsersSavedAlbums(get(), get()) }
    single { GetCurrentUsersSavedTracks(get(), get()) }

    single { SpotifyAccountSavedRepo(get(), get()) } bind ISpotifyAccountSavedRepo::class
}
