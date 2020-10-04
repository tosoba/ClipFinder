package com.example.spotify.account.saved.di

import com.example.spotify.account.saved.data.SpotifyAccountSavedRepo
import com.example.spotify.account.saved.domain.repo.ISpotifyAccountSavedRepo
import com.example.spotify.account.saved.domain.usecase.GetCurrentUsersSavedAlbums
import com.example.spotify.account.saved.domain.usecase.GetCurrentUsersSavedTracks
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyAccountSavedModule = module {
    single { GetCurrentUsersSavedAlbums(get(), get(), get()) }
    single { GetCurrentUsersSavedTracks(get(), get(), get()) }

    single { SpotifyAccountSavedRepo(get()) } bind ISpotifyAccountSavedRepo::class
}
