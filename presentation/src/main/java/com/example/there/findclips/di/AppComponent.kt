package com.example.there.findclips.di

import com.example.there.findclips.di.dashboard.DashboardModule
import com.example.there.findclips.di.dashboard.DashboardSubComponent
import com.example.there.findclips.di.favourites.FavouritesModule
import com.example.there.findclips.di.favourites.FavouritesSubComponent
import com.example.there.findclips.di.modules.AppModule
import com.example.there.findclips.di.modules.DataModule
import com.example.there.findclips.di.modules.NetworkModule
import com.example.there.findclips.di.search.SearchModule
import com.example.there.findclips.di.search.SearchSubComponent
import com.example.there.findclips.di.videos.VideosModule
import com.example.there.findclips.di.videos.VideosSubComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DataModule::class
])
interface AppComponent {
    fun plus(dashboardModule: DashboardModule): DashboardSubComponent
    fun plus(favouritesModule: FavouritesModule): FavouritesSubComponent
    fun plus(searchModule: SearchModule): SearchSubComponent

    fun plus(videosModule: VideosModule): VideosSubComponent
}