package com.example.there.findclips.di.module

import android.app.Application
import android.content.Context
import com.example.coreandroid.base.IFragmentFactory
import com.example.spotifyrepo.preferences.SpotifyPreferences
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.findclips.FragmentFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { SpotifyPreferences(get()) }
    factory { FragmentFactory } bind IFragmentFactory::class
    factory {
        object : UseCaseSchedulersProvider {
            override val subscribeOnScheduler: Scheduler get() = Schedulers.io()
            override val observeOnScheduler: Scheduler get() = AndroidSchedulers.mainThread()
        }
    }
}

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @Provides
    fun fragmentFactory() = FragmentFactory

    @Binds
    abstract fun bindFragmentFactory(factory: FragmentFactory): IFragmentFactory
}