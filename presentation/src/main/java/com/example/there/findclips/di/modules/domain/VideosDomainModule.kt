package com.example.there.findclips.di.modules.domain

import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.videos.*
import com.example.there.findclips.util.rx.AsyncSymmetricObservableTransformer
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
    fun getFavouriteVideoPlaylistsUseCase(repository: IVideosRepository): GetFavouriteVideoPlaylists =
            GetFavouriteVideoPlaylists(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun searchRelatedVideosUseCase(
            repository: IVideosRepository
    ): SearchRelatedVideos = SearchRelatedVideos(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun addVideosToPlaylistUseCase(
            repository: IVideosRepository
    ): AddVideoToPlaylist = AddVideoToPlaylist(AsyncSymmetricObservableTransformer(), repository)

    @Provides
    fun insertVideoPlaylistUseCase(
            repository: IVideosRepository
    ): InsertVideoPlaylist = InsertVideoPlaylist(AsyncSymmetricObservableTransformer(), repository)
    @Provides
    fun searchVideosUseCase(repository: IVideosRepository): SearchVideos = SearchVideos(AsyncSymmetricSingleTransformer(), repository)

    @Provides
    fun getVideosFromPlaylistsUseCase(repository: IVideosRepository): GetFavouriteVideosFromPlaylist =
            GetFavouriteVideosFromPlaylist(AsyncSymmetricObservableTransformer(), repository)
}