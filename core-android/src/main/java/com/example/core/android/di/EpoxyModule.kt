package com.example.core.android.di

import android.os.Handler
import android.os.HandlerThread
import org.koin.core.qualifier.named
import org.koin.dsl.module

val epoxyModule = module {
    single(EpoxyHandlerQualifier.DIFFER) {
        Handler(HandlerThread("epoxy-diffing-thread").apply(Thread::start).looper)
    }
    single(EpoxyHandlerQualifier.BUILDER) {
        Handler(HandlerThread("epoxy-model-building-thread").apply(Thread::start).looper)
    }
}

object EpoxyHandlerQualifier {
    val DIFFER = named("differ")
    val BUILDER = named("builder")
}
