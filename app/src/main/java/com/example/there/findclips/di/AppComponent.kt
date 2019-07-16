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
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DataModule::class,
    ActivitiesModule::class,
    FragmentsModule::class,
    ViewModelsModule::class,
    SchedulersModule::class,
    AndroidSupportInjectionModule::class
])
interface AppComponent : AndroidInjector<FindClipsApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<FindClipsApp>() {
        @BindsInstance
        abstract fun application(application: Application): Builder
    }
}