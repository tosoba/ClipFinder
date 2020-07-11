package com.example.spotifydashboard.di

import com.example.spotifydashboard.data.SpotifyDashboardRepo
import com.example.spotifydashboard.domain.repo.ISpotifyDashboardRepo
import com.example.spotifydashboard.domain.usecase.GetCategories
import com.example.spotifydashboard.domain.usecase.GetDailyViralTracks
import com.example.spotifydashboard.domain.usecase.GetFeaturedPlaylists
import com.example.spotifydashboard.domain.usecase.GetNewReleases
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyDashboardModule = module {
    single { GetCategories(get(), get()) }
    single { GetFeaturedPlaylists(get(), get()) }
    single { GetNewReleases(get(), get()) }
    single { GetDailyViralTracks(get(), get()) }

    single {
        SpotifyDashboardRepo(get(), get(), get(), get(), get())
    } bind ISpotifyDashboardRepo::class
}
