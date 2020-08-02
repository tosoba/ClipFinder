package com.example.spotifysearch.di

import com.example.spotifysearch.data.SpotifySearchRepoImpl
import com.example.spotifysearch.domain.repo.ISpotifySearchRepo
import com.example.spotifysearch.domain.usecase.SearchSpotify
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifySearchModule = module {
    single { SearchSpotify(get(), get()) }
    single { SpotifySearchRepoImpl(get(), get()) } bind ISpotifySearchRepo::class
}
