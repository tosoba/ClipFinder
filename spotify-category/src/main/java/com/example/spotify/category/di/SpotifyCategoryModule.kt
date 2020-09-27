package com.example.spotify.category.di

import com.example.spotify.category.data.SpotifyCategoryRepo
import com.example.spotify.category.domain.repo.ISpotifyCategoryRepo
import com.example.spotify.category.domain.usecase.GetPlaylistsForCategory
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyCategoryModule = module {
    single { GetPlaylistsForCategory(get(), get()) }

    single { SpotifyCategoryRepo(get(), get(), get()) } bind ISpotifyCategoryRepo::class
}
