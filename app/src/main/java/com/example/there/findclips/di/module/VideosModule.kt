package com.example.there.findclips.di.module

import com.example.there.domain.usecase.videos.*
import org.koin.dsl.module

val videosModule = module {
    factory { DeleteAllVideoSearchData(get(), get()) }
    factory { GetChannelsThumbnailUrls(get(), get()) }
    factory { GetVideoPlaylistsWithThumbnails(get(), get()) }
    factory { SearchRelatedVideos(get(), get(), get()) }
    factory { SearchVideos(get(), get(), get()) }

    factory { GetFavouriteVideoPlaylists(get(), get()) }
    factory { InsertVideoPlaylist(get(), get()) }
    factory { DeleteVideoPlaylist(get(), get()) }

    factory { GetFavouriteVideosFromPlaylist(get(), get()) }
    factory { AddVideoToPlaylist(get(), get()) }
    factory { DeleteVideo(get(), get()) }
}