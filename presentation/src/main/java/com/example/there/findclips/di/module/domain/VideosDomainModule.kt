package com.example.there.findclips.di.module.domain

import com.example.there.domain.repo.videos.IVideosRepository
import com.example.there.domain.usecase.videos.*
import com.example.there.findclips.util.rx.AsyncCompletableTransformer
import com.example.there.findclips.util.rx.AsyncSymmetricFlowableTransformer
import com.example.there.findclips.util.rx.AsyncSymmetricSingleTransformer
import dagger.Module
import dagger.Provides

@Module
class VideosDomainModule {

    @Provides
    fun getChannelsThumbnailUrlsUseCase(
            repository: IVideosRepository
    ): GetChannelsThumbnailUrls = GetChannelsThumbnailUrls(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun getFavouriteVideoPlaylistsUseCase(
            repository: IVideosRepository
    ): GetFavouriteVideoPlaylists = GetFavouriteVideoPlaylists(AsyncSymmetricFlowableTransformer(), repository)

    @Provides
    fun searchRelatedVideosUseCase(
            repository: IVideosRepository
    ): SearchRelatedVideos = SearchRelatedVideos(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun addVideosToPlaylistUseCase(
            repository: IVideosRepository
    ): AddVideoToPlaylist = AddVideoToPlaylist(AsyncCompletableTransformer(), repository)

    @Provides
    fun insertVideoPlaylistUseCase(
            repository: IVideosRepository
    ): InsertVideoPlaylist = InsertVideoPlaylist(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun searchVideosUseCase(repository: IVideosRepository): SearchVideos = SearchVideos(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun getVideosFromPlaylistsUseCase(
            repository: IVideosRepository
    ): GetFavouriteVideosFromPlaylist = GetFavouriteVideosFromPlaylist(AsyncSymmetricFlowableTransformer(), repository)
}