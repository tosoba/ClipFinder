package com.example.there.domain.usecase.spotify

import com.example.core.model.Resource
import com.example.there.domain.UseCaseSchedulersProvider
import com.example.there.domain.entity.spotify.CategoryEntity
import com.example.there.domain.repo.spotify.ISpotifyRemoteDataStore
import com.example.there.domain.usecase.base.ObservableUseCase
import io.reactivex.Observable

class GetCategories(
        schedulersProvider: UseCaseSchedulersProvider,
        private val remote: ISpotifyRemoteDataStore
) : ObservableUseCase<Resource<List<CategoryEntity>>>(schedulersProvider) {
    override val result: Observable<Resource<List<CategoryEntity>>>
        get() = remote.categories
}