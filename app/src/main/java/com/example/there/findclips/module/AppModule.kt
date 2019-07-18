package com.example.there.findclips.module

import com.example.coreandroid.base.IFragmentFactory
import com.example.spotifyrepo.preferences.SpotifyPreferences
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.findclips.FragmentFactory
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { SpotifyPreferences(get()) }
    factory { FragmentFactory } bind IFragmentFactory::class
    factory<UseCaseSchedulersProvider> {
        object : UseCaseSchedulersProvider {
            override val subscribeOnScheduler: Scheduler get() = Schedulers.io()
            override val observeOnScheduler: Scheduler get() = AndroidSchedulers.mainThread()
        }
    }
}
