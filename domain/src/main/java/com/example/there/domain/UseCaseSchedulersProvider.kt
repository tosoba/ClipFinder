package com.example.there.domain

import io.reactivex.Scheduler

interface UseCaseSchedulersProvider {
    val subscribeOnScheduler: Scheduler
    val observeOnScheduler: Scheduler
}