package com.example.there.findclips.module

import android.os.Handler
import android.os.HandlerThread
import com.example.core.retrofit.offlineCacheInterceptor
import com.example.core.retrofit.onlineCacheInterceptor
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.preferences.SpotifyPreferences
import com.example.coreandroid.util.ext.isConnected
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.findclips.FragmentFactory
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
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

    single {
        Cache(androidContext().cacheDir, 10 * 1000 * 1000)
    }

    single(named("onlineCacheInterceptor")) { onlineCacheInterceptor() }
    single(named("offlineCacheInterceptor")) { offlineCacheInterceptor { androidContext().isConnected } }

    //TODO: factory or single (because of different sets of interceptors between apis)? - more likely factory
    factory<OkHttpClient> { (extraInterceptors: Collection<Interceptor>?) ->
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
