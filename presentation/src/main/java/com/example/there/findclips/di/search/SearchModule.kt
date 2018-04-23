package com.example.there.findclips.di.search

import com.example.there.domain.usecase.AccessTokenUseCase
import com.example.there.findclips.search.SearchViewModelFactory
import dagger.Module
import dagger.Provides

@SearchScope
@Module
class SearchModule {

    @Provides
    fun searchViewModelFactory(accessTokenUseCase: AccessTokenUseCase): SearchViewModelFactory = SearchViewModelFactory(accessTokenUseCase)
}