package com.example.there.findclips.di.category

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetPlaylistsForCategory
import com.example.there.domain.usecases.spotify.InsertCategory
import com.example.there.findclips.activities.category.CategoryVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@CategoryScope
@Module
class CategoryModule {

    @Provides
    fun insertCategoryUseCase(repository: ISpotifyRepository): InsertCategory = InsertCategory(AsyncTransformer(), repository)

    @Provides
    fun playlistsForCategoryUseCase(repository: ISpotifyRepository): GetPlaylistsForCategory = GetPlaylistsForCategory(AsyncTransformer(), repository)

    @Provides
    fun categoryVMFactory(getAccessToken: GetAccessToken,
                          getPlaylistsForCategory: GetPlaylistsForCategory,
                          insertCategory: InsertCategory): CategoryVMFactory = CategoryVMFactory(getAccessToken, getPlaylistsForCategory, insertCategory)
}