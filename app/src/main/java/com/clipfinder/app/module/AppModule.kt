package com.clipfinder.app.module

import com.clipfinder.app.FragmentFactory
import com.clipfinder.core.android.base.IFragmentFactory
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.core.ext.RxSchedulers
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.binds
import org.koin.dsl.module

val appModule = module {
    factory { FragmentFactory } binds arrayOf(IFragmentFactory::class, ISpotifyFragmentsFactory::class)

    factory<RxSchedulers> {
        object : RxSchedulers {
            override val io: Scheduler get() = Schedulers.io()
            override val main: Scheduler get() = AndroidSchedulers.mainThread()
        }
    }
}
