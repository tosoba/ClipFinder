package com.example.spotifyartist.di

import com.example.spotifyartist.data.SpotifyArtistRepo
import com.example.spotifyartist.domain.repo.ISpotifyArtistRepo
import com.example.spotifyartist.domain.usecase.*
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyArtistModule = module {
    single { DeleteArtist(get(), get()) }
    single { GetAlbumsFromArtist(get(), get()) }
    single { GetRelatedArtists(get(), get()) }
    single { GetTopTracksFromArtist(get(), get()) }
    single { InsertArtist(get(), get()) }
    single { IsArtistSaved(get(), get()) }

    single { SpotifyArtistRepo(get(), get(), get(), get()) } bind ISpotifyArtistRepo::class
}
