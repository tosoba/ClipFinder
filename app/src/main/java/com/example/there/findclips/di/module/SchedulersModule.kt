package com.example.there.findclips.di.module

import com.example.there.domain.UseCaseSchedulersProvider
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class SchedulersModule {

    @Provides
    fun schedulersProvider(): UseCaseSchedulersProvider = object : UseCaseSchedulersProvider {
        override val subscribeOnScheduler: Scheduler
            get() = Schedulers.io()
        override val observeOnScheduler: Scheduler
            get() = AndroidSchedulers.mainThread()
    }
}