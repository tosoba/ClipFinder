package com.example.there.findclips.module

import com.example.core.android.base.IFragmentFactory
import com.example.core.android.util.ext.isConnected
import com.example.core.ext.RxSchedulers
import com.example.core.retrofit.offlineCacheInterceptor
import com.example.core.retrofit.onlineCacheInterceptor
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
    factory { FragmentFactory } bind IFragmentFactory::class

    factory<RxSchedulers> {
        object : RxSchedulers {
            override val subscribeOnScheduler: Scheduler get() = Schedulers.io()
            override val observeOnScheduler: Scheduler get() = AndroidSchedulers.mainThread()
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
