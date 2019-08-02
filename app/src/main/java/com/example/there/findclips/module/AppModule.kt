package com.example.there.findclips.module

import android.os.Handler
import android.os.HandlerThread
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.preferences.SpotifyPreferences
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.findclips.FragmentFactory
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.core.qualifier.named
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

    single(named("diffing-thread")) {
        HandlerThread("epoxy-diffing-thread").apply { start() }
    }
    single(named("building-thread")) {
        HandlerThread("epoxy-model-building-thread").apply { start() }
    }
    single(named("differ")) { Handler(get<HandlerThread>(named("diffing-thread")).looper) }
    single(named("builder")) { Handler(get<HandlerThread>(named("building-thread")).looper) }
}
