package com.example.there.findclips.di.modules

import com.example.there.domain.repos.spotify.ISpotifyRepository
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.domain.usecases.spotify.GetArtists
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecases.videos.GetFavouriteVideoPlaylists
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonUseCasesModule {

    @Provides
    @Singleton
    fun accessTokenUseCase(repository: ISpotifyRepository): GetAccessToken = GetAccessToken(AsyncTransformer(), repository)

    @Provides
    @Singleton
    fun artistsUseCase(repository: ISpotifyRepository): GetArtists = GetArtists(AsyncTransformer(), repository)

    @Provides
    @Singleton
    fun getChannelsThumbnailUrlsUseCase(repository: IVideosRepository): GetChannelsThumbnailUrls =
            GetChannelsThumbnailUrls(AsyncTransformer(), repository)

    @Provides
    @Singleton
    fun getFavouriteVideoPlaylistsUseCase(repository: IVideosRepository): GetFavouriteVideoPlaylists =
            GetFavouriteVideoPlaylists(AsyncTransformer(), repository)
}