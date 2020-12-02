package com.example.there.findclips.module

import com.clipfinder.core.retrofit.retrofitWith
import com.clipfinder.soundcloud.api.SoundCloudApi
import com.clipfinder.soundcloud.api.SoundCloudApiV2
import okhttp3.Interceptor
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

val apiModule = module {
    single {
        retrofitWith(
            url = soundCloudBaseUrl,
            client = get { parametersOf(emptyList<Interceptor>()) },
            callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SoundCloudApi::class.java)
    }
    single {
        retrofitWith(
            url = soundCloudBaseUrlV2,
            client = get { parametersOf(emptyList<Interceptor>()) },
            callAdapterFactories = arrayOf(RxJava2CallAdapterFactory.create())
        ).create(SoundCloudApiV2::class.java)
    }
}

private const val soundCloudBaseUrlV2: String = "https://api-v2.soundcloud.com/"
private const val soundCloudBaseUrl: String = "https://api.soundcloud.com/"
