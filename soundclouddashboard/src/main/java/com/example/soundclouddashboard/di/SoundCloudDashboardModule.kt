package com.example.soundclouddashboard.di

import com.example.soundclouddashboard.data.SoundCloudDashboardRepo
import com.example.soundclouddashboard.domain.repo.ISoundCloudDashboardRepo
import com.example.soundclouddashboard.domain.usecase.GetClientId
import org.koin.dsl.bind
import org.koin.dsl.module

val soundCloudDashboardModule = module {
    single { GetClientId(get(), get()) }

    single { SoundCloudDashboardRepo(get()) } bind ISoundCloudDashboardRepo::class
}
