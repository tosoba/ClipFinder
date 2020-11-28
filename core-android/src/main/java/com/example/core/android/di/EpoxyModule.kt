package com.example.core.android.di

import android.os.Handler
import android.os.HandlerThread
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named
import org.koin.dsl.module

val epoxyModule = module {
    single(EpoxyHandlerType.DIFFER.qualifier) {
        Handler(HandlerThread("epoxy-diffing-thread").apply(Thread::start).looper)
    }
    single(EpoxyHandlerType.BUILDER.qualifier) {
        Handler(HandlerThread("epoxy-model-building-thread").apply(Thread::start).looper)
    }
}

enum class EpoxyHandlerType {
    DIFFER, BUILDER;

    val qualifier: Qualifier
        get() = named(name)
}
