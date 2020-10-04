package com.example.spotify.artist.di

import com.example.spotify.artist.data.SpotifyArtistRepo
import com.example.spotify.artist.domain.repo.ISpotifyArtistRepo
import com.example.spotify.artist.domain.usecase.GetAlbumsFromArtist
import com.example.spotify.artist.domain.usecase.GetRelatedArtists
import com.example.spotify.artist.domain.usecase.GetTopTracksFromArtist
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyArtistModule = module {
    single { GetAlbumsFromArtist(get(), get(), get()) }
    single { GetRelatedArtists(get(), get(), get()) }
    single { GetTopTracksFromArtist(get(), get(), get()) }

    single { SpotifyArtistRepo(get(), get()) } bind ISpotifyArtistRepo::class
}
