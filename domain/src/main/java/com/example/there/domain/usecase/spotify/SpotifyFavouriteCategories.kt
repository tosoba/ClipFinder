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

class GetFavouriteCategories(
    schedulersProvider: UseCaseSchedulersProvider,
    private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<CategoryEntity>>(schedulersProvider) {
    override val result: Flowable<List<CategoryEntity>> get() = repository.favouriteCategories
}

class InsertCategory(
    schedulersProvider: UseCaseSchedulersProvider,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<CategoryEntity>(schedulersProvider) {
    override fun run(args: CategoryEntity): Completable = repository.insertCategory(args)
}

class IsCategorySaved(
    schedulersProvider: UseCaseSchedulersProvider,
    private val repository: ISpotifyLocalRepo
) : SingleUseCaseWithArgs<String, Boolean>(schedulersProvider) {
    override fun run(args: String): Single<Boolean> = repository.isCategorySaved(args)
}

class DeleteCategory(
    schedulersProvider: UseCaseSchedulersProvider,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<CategoryEntity>(schedulersProvider) {
    override fun run(args: CategoryEntity): Completable = repository.deleteCategory(args)
}
