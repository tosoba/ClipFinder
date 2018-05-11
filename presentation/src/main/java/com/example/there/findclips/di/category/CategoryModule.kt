package com.example.there.findclips.di.category

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.spotify.AccessTokenUseCase
import com.example.there.domain.usecases.spotify.PlaylistsForCategoryUseCase
import com.example.there.findclips.category.CategoryVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@CategoryScope
@Module
class CategoryModule {

    @Provides
    fun playlistsForCategoryUseCase(repository: SpotifyRepository): PlaylistsForCategoryUseCase = PlaylistsForCategoryUseCase(AsyncTransformer(), repository)

    @Provides
    fun categoryVMFactory(accessTokenUseCase: AccessTokenUseCase, playlistsForCategoryUseCase: PlaylistsForCategoryUseCase): CategoryVMFactory =
            CategoryVMFactory(accessTokenUseCase, playlistsForCategoryUseCase)
}