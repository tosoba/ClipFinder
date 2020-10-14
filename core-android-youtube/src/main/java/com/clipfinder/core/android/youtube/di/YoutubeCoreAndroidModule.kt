package com.clipfinder.core.android.youtube.di

import com.clipfinder.core.android.youtube.R
import com.clipfinder.core.android.youtube.repo.YoutubeRepo
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val youtubeCoreAndroidModule = module {
    single { YoutubeRepo(androidContext().getString(R.string.youtube_api_key), get()) } bind IYoutubeRepo::class
}
