package com.example.there.findclips.di

import android.app.Application
import com.example.there.findclips.FindClipsApp
import com.example.there.findclips.di.module.AppModule
import com.example.there.findclips.di.module.DataModule
import com.example.there.findclips.di.module.NetworkModule
import com.example.there.findclips.di.module.SchedulersModule
import com.example.there.findclips.di.module.ui.ActivitiesModule
import com.example.there.findclips.di.module.ui.FragmentsModule
import com.example.there.findclips.di.module.ui.ViewModelsModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DataModule::class,
    ActivitiesModule::class,
    FragmentsModule::class,
    ViewModelsModule::class,
    SchedulersModule::class
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