package com.example.there.findclips.di.player

import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.videos.AddVideosToPlaylist
import com.example.there.domain.usecases.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecases.videos.SearchRelatedVideos
import com.example.there.findclips.activities.player.PlayerVMFactory
import com.example.there.findclips.util.rx.AsyncTransformer
import dagger.Module
import dagger.Provides

@PlayerScope
@Module
class PlayerModule {
    @Provides
    fun searchRelatedVideosUseCase(repository: IVideosRepository): SearchRelatedVideos = SearchRelatedVideos(AsyncTransformer(), repository)

    @Provides
    fun addVideosToPlaylistUseCase(repository: IVideosRepository): AddVideosToPlaylist = AddVideosToPlaylist(AsyncTransformer(), repository)

    @Provides
    fun playerVMFactory(searchRelatedVideos: SearchRelatedVideos,
                        getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
                        addVideosToPlaylist: AddVideosToPlaylist): PlayerVMFactory =
            PlayerVMFactory(searchRelatedVideos, getChannelsThumbnailUrls, addVideosToPlaylist)
}