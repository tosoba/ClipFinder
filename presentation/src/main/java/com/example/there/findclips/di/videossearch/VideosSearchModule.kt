package com.example.there.findclips.di.videossearch

import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecases.videos.GetFavouriteVideosFromPlaylist
import com.example.there.domain.usecases.videos.SearchVideos
import com.example.there.findclips.fragments.search.videos.VideosSearchVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides


@VideosSearchScope
@Module
class VideosSearchModule {

    @Provides
    fun searchVideosUseCase(repository: IVideosRepository): SearchVideos = SearchVideos(AsyncTransformer(), repository)

    @Provides
    fun getVideosFromPlaylistsUseCase(repository: IVideosRepository): GetFavouriteVideosFromPlaylist =
            GetFavouriteVideosFromPlaylist(AsyncTransformer(), repository)

    @Provides
    fun videosViewModelFactory(searchVideos: SearchVideos,
                               getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
                               getFavouriteVideosFromPlaylist: GetFavouriteVideosFromPlaylist): VideosSearchVMFactory =
            VideosSearchVMFactory(searchVideos, getChannelsThumbnailUrls, getFavouriteVideosFromPlaylist)
}