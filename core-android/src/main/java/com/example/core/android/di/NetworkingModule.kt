package com.example.core.android.di

import com.example.core.android.interceptor.ConnectivityInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkingModule = module {
    single { ConnectivityInterceptor(androidContext()) }
}