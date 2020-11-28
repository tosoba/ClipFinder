package com.example.there.findclips.module

import com.clipfinder.core.ext.RxSchedulers
import com.clipfinder.core.retrofit.offlineCacheInterceptor
import com.clipfinder.core.retrofit.onlineCacheInterceptor
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.isConnected
import com.example.there.findclips.FragmentFactory
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module

val appModule = module {
    factory { FragmentFactory } binds arrayOf(IFragmentFactory::class, ISpotifyFragmentsFactory::class)

    factory<RxSchedulers> {
        object : RxSchedulers {
            override val io: Scheduler
                get() = Schedulers.io()
            override val main: Scheduler
                get() = AndroidSchedulers.mainThread()
        }
    }

    single(named("onlineCacheInterceptor")) { onlineCacheInterceptor() }
    single(named("offlineCacheInterceptor")) {
        offlineCacheInterceptor { androidContext().isConnected }
    }

    factory { (extraInterceptors: Collection<Interceptor>?) ->
        OkHttpClient.Builder()
            .addNetworkInterceptor(get<Interceptor>(named("onlineCacheInterceptor")))
            .addInterceptor(get<Interceptor>(named("offlineCacheInterceptor")))
            .apply {
                extraInterceptors?.forEach { addInterceptor(it) }
            }
            .cache(get<Cache>())
            .build()
    }
}
