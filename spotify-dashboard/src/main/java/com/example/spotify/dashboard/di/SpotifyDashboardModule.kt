package com.example.spotify.dashboard.di

import com.example.spotify.dashboard.data.SpotifyDashboardRepo
import com.example.spotify.dashboard.domain.repo.ISpotifyDashboardRepo
import com.example.spotify.dashboard.domain.usecase.GetCategories
import com.example.spotify.dashboard.domain.usecase.GetDailyViralTracks
import com.example.spotify.dashboard.domain.usecase.GetFeaturedPlaylists
import com.example.spotify.dashboard.domain.usecase.GetNewReleases
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyDashboardModule = module {
    single { GetCategories(get(), get(), get()) }
    single { GetFeaturedPlaylists(get(), get(), get()) }
    single { GetNewReleases(get(), get(), get()) }
    single { GetDailyViralTracks(get(), get(), get()) }

    single { SpotifyDashboardRepo(get(), get(), get(), get()) } bind ISpotifyDashboardRepo::class
}
