package com.example.spotifycategory.di

import com.example.spotifycategory.data.SpotifyCategoryRepo
import com.example.spotifycategory.domain.repo.ISpotifyCategoryRepo
import com.example.spotifycategory.domain.usecase.DeleteCategory
import com.example.spotifycategory.domain.usecase.GetPlaylistsForCategory
import com.example.spotifycategory.domain.usecase.InsertCategory
import com.example.spotifycategory.domain.usecase.IsCategorySaved
import org.koin.dsl.bind
import org.koin.dsl.module

val spotifyCategoryModule = module {
    single { DeleteCategory(get(), get()) }
    single { GetPlaylistsForCategory(get(), get()) }
    single { InsertCategory(get(), get()) }
    single { IsCategorySaved(get(), get()) }

    single { SpotifyCategoryRepo(get(), get(), get(), get()) } bind ISpotifyCategoryRepo::class
}
