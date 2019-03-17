package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class GetFavouriteCategories @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : FlowableUseCase<List<CategoryEntity>>(schedulersProvider) {

    override val flowable: Flowable<List<CategoryEntity>>
        get() = repository.favouriteCategories
}

class InsertCategory @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<CategoryEntity>(schedulersProvider) {

    override fun createCompletable(input: CategoryEntity): Completable = repository.insertCategory(input)
}

class IsCategorySaved @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : SingleUseCaseWithInput<CategoryEntity, Boolean>(schedulersProvider) {

    override fun createSingle(input: CategoryEntity): Single<Boolean> = repository.isCategorySaved(input)
}

class DeleteCategory @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : CompletableUseCaseWithInput<CategoryEntity>(schedulersProvider) {

    override fun createCompletable(input: CategoryEntity): Completable = repository.deleteCategory(input)
}