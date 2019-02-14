package com.example.there.domain.usecase.base

import io.reactivex.Scheduler

abstract class BaseRxUseCase(
        protected val subscribeOnScheduler: Scheduler,
        protected val observeOnScheduler: Scheduler
)