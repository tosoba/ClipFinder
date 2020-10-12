package com.clipfinder.core.android.youtube.di

import com.clipfinder.core.android.youtube.repo.YoutubeRepo
import com.clipfinder.core.youtube.repo.IYoutubeRepo
import org.koin.dsl.bind
import org.koin.dsl.module

val youtubeCoreAndroidModule = module {
    single { YoutubeRepo() } bind IYoutubeRepo::class
}
