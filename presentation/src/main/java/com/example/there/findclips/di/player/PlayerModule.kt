package com.example.there.findclips.di.player

import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.usecases.videos.*
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
    fun addVideosToPlaylistUseCase(repository: IVideosRepository): AddVideoToPlaylist = AddVideoToPlaylist(AsyncTransformer(), repository)

    @Provides
    fun insertVideoPlaylistUseCase(repository: IVideosRepository): InsertVideoPlaylist = InsertVideoPlaylist(AsyncTransformer(), repository)

    @Provides
    fun playerVMFactory(searchRelatedVideos: SearchRelatedVideos,
                        getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
                        insertVideoPlaylist: InsertVideoPlaylist,
                        addVideoToPlaylist: AddVideoToPlaylist,
                        getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists): PlayerVMFactory =
            PlayerVMFactory(searchRelatedVideos, getChannelsThumbnailUrls, insertVideoPlaylist, addVideoToPlaylist, getFavouriteVideoPlaylists)
}