package com.example.there.domain.usecase.spotify

import com.example.core.ext.RxSchedulers
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.repo.spotify.ISpotifyLocalRepo
import com.example.there.domain.usecase.base.FlowableUseCase
import io.reactivex.Flowable

class GetFavouriteCategories(
    schedulers: RxSchedulers,
    private val repository: ISpotifyLocalRepo
) : FlowableUseCase<List<CategoryEntity>>(schedulers) {
    override val result: Flowable<List<CategoryEntity>> get() = repository.favouriteCategories
}
