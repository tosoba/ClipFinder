package com.clipfinder.core.android.di

import android.os.Handler
import android.os.HandlerThread
import com.clipfinder.core.ext.qualifier
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
    DIFFER,
    BUILDER
}
