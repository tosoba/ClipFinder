package com.example.there.domain.usecase.spotify

import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.repo.spotify.ISpotifyRepository
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable
import javax.inject.Inject

class GetCategories @Inject constructor(
        schedulersProvider: UseCaseSchedulersProvider,
        private val repository: ISpotifyRepository
) : ObservableUseCase<List<CategoryEntity>>(schedulersProvider) {

    override val observable: Observable<List<CategoryEntity>>
        get() = repository.categories
}