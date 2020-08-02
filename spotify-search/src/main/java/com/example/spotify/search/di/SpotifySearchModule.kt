package com.example.spotify.search.di

import com.example.spotify.search.data.SpotifySearchRepoImpl
import com.example.spotify.search.domain.repo.ISpotifySearchRepo
import com.example.spotify.search.domain.usecase.SearchSpotify
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifySearchModule = module {
    single { SearchSpotify(get(), get()) }
    single { SpotifySearchRepoImpl(get(), get()) } bind ISpotifySearchRepo::class
}
