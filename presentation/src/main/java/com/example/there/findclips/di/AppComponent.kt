package com.example.there.findclips.di

import android.app.Application
import com.example.there.findclips.FindClipsApp
import com.example.there.findclips.di.modules.AppModule
import com.example.there.findclips.di.modules.DataModule
import com.example.there.findclips.di.modules.NetworkModule
import com.example.there.findclips.di.modules.domain.SpotifyDomainModule
import com.example.there.findclips.di.modules.domain.VideosDomainModule
import com.example.there.findclips.di.modules.ui.ActivitiesModule
import com.example.there.findclips.di.modules.ui.FragmentsModule
import com.example.there.findclips.di.modules.ui.ViewModelsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DataModule::class,
    SpotifyDomainModule::class,
    VideosDomainModule::class,
    ActivitiesModule::class,
    FragmentsModule::class,
    ViewModelsModule::class
])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: FindClipsApp)
}