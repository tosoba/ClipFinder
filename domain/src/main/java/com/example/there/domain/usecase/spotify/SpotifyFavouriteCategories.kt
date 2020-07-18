package com.example.there.domain.usecase.spotify

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import com.example.there.domain.usecase.base.FlowableUseCase
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class GetFavouriteCategories(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<CategoryEntity>>(schedulers) {
    override val result: Flowable<List<CategoryEntity>> get() = repository.favouriteCategories
}

class InsertCategory(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<CategoryEntity>(schedulers) {
    override fun run(args: CategoryEntity): Completable = repository.insertCategory(args)
}

class IsCategorySaved(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : SingleUseCaseWithArgs<String, Boolean>(schedulers) {
    override fun run(args: String): Single<Boolean> = repository.isCategorySaved(args)
}

class DeleteCategory(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : CompletableUseCaseWithArgs<CategoryEntity>(schedulers) {
    override fun run(args: CategoryEntity): Completable = repository.deleteCategory(args)
}
