package com.example.there.domain.usecase.spotify

import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class GetFavouriteCategories @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<CategoryEntity>>(subscribeOnScheduler, observeOnScheduler) {

    override val flowable: Flowable<List<CategoryEntity>>
        get() = repository.favouriteCategories
}

class InsertCategory @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<CategoryEntity>(subscribeOnScheduler, observeOnScheduler) {

    override fun createCompletable(input: CategoryEntity): Completable = repository.insertCategory(input)
}

class IsCategorySaved @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<CategoryEntity, Boolean>(subscribeOnScheduler, observeOnScheduler) {

    override fun createSingle(input: CategoryEntity): Single<Boolean> = repository.isCategorySaved(input)
}

class DeleteCategory @Inject constructor(
        @Named("subscribeOnScheduler") subscribeOnScheduler: Scheduler,
        @Named("observeOnScheduler") observeOnScheduler: Scheduler,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<CategoryEntity>(subscribeOnScheduler, observeOnScheduler) {

    override fun createCompletable(input: CategoryEntity): Completable = repository.deleteCategory(input)
}