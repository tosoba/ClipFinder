package com.example.core.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun retrofitWith(
        url: String, client: OkHttpClient = OkHttpClient(),
        converterFactory: Converter.Factory = GsonConverterFactory.create(),
        callAdapterFactories: Array<CallAdapter.Factory> = arrayOf(
                RxSealedCallAdapterFactory.create(), RxJava2CallAdapterFactory.create())
): Retrofit = Retrofit.Builder()
        .client(client)
        .run {
            callAdapterFactories.forEach {
                addCallAdapterFactory(it)
            }
            this
        }
        .addConverterFactory(converterFactory)
        .baseUrl(url)
        .build()

fun clientWithInterceptors(
        vararg interceptors: Interceptor
): OkHttpClient = OkHttpClient().newBuilder().apply {
    interceptors.forEach { addInterceptor(it) }
}.build()

fun interceptorWithHeaders(
        vararg headers: Pair<String, String>
): Interceptor = Interceptor { chain ->
    chain.proceed(chain.request()
            .newBuilder()
            .apply {
                headers.forEach { (name, value) ->
                    addHeader(name, value)
                }
            }
            .build())
}