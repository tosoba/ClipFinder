package com.example.spotify.category.di

import com.example.spotify.category.data.SpotifyCategoryRepo
import com.example.spotify.category.domain.repo.ISpotifyCategoryRepo
import com.example.spotify.category.domain.usecase.DeleteCategory
import com.example.spotify.category.domain.usecase.GetPlaylistsForCategory
import com.example.spotify.category.domain.usecase.InsertCategory
import com.example.spotify.category.domain.usecase.IsCategorySaved
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyCategoryModule = module {
    single { DeleteCategory(get(), get()) }
    single { GetPlaylistsForCategory(get(), get()) }
    single { InsertCategory(get(), get()) }
    single { IsCategorySaved(get(), get()) }

    single { SpotifyCategoryRepo(get(), get(), get(), get()) } bind ISpotifyCategoryRepo::class
}
