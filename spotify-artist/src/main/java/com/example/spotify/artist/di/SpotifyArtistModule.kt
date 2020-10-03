package com.example.spotify.artist.di

import com.example.spotify.artist.data.SpotifyArtistRepo
import com.example.spotify.artist.domain.repo.ISpotifyArtistRepo
import com.example.spotify.artist.domain.usecase.*
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyArtistModule = module {
    single { GetAlbumsFromArtist(get(), get()) }
    single { GetRelatedArtists(get(), get()) }
    single { GetTopTracksFromArtist(get(), get()) }

    single { SpotifyArtistRepo(get(), get(), get()) } bind ISpotifyArtistRepo::class
}
