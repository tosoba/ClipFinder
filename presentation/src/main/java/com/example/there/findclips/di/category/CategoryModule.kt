package com.example.there.findclips.di.category

import com.example.there.domain.repos.spotify.SpotifyRepository
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetPlaylistsForCategory
import com.example.there.findclips.category.CategoryVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@CategoryScope
@Module
class CategoryModule {

    @Provides
    fun playlistsForCategoryUseCase(repository: SpotifyRepository): GetPlaylistsForCategory = GetPlaylistsForCategory(AsyncTransformer(), repository)

    @Provides
    fun categoryVMFactory(getAccessToken: GetAccessToken, getPlaylistsForCategory: GetPlaylistsForCategory): CategoryVMFactory =
            CategoryVMFactory(getAccessToken, getPlaylistsForCategory)
}