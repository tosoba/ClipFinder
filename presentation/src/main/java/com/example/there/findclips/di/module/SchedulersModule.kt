package com.example.there.findclips.di.module

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class SchedulersModule {
    @Provides
    @Named("observeOnScheduler")
    fun observeOnScheduler(): Scheduler = AndroidSchedulers.mainThread()

    @Provides
    @Named("subscribeOnScheduler")
    fun subscribeOnScheduler(): Scheduler = Schedulers.io()
}