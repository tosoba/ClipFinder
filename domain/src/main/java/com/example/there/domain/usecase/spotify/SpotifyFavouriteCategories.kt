package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Inject

class GetFavouriteCategories @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<CategoryEntity>>(schedulersProvider) {

    override val flowable: Flowable<List<CategoryEntity>>
        get() = repository.favouriteCategories
}

class InsertCategory @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<CategoryEntity>(schedulersProvider) {

    override fun createCompletable(args: CategoryEntity): Completable = repository.insertCategory(args)
}

class IsCategorySaved @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : SingleUseCaseWithArgs<CategoryEntity, Boolean>(schedulersProvider) {

    override fun createSingle(args: CategoryEntity): Single<Boolean> = repository.isCategorySaved(args)
}

class DeleteCategory @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<CategoryEntity>(schedulersProvider) {

    override fun createCompletable(args: CategoryEntity): Completable = repository.deleteCategory(args)
}