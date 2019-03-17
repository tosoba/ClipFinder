package com.example.there.domain.usecase.base

import com.example.there.domain.UseCaseSchedulersProvider

abstract class BaseRxUseCase(
        protected val schedulersProvider: UseCaseSchedulersProvider
)