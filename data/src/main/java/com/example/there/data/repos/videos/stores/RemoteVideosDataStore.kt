package com.example.there.data.repos.videos.stores

import com.example.there.data.api.yahoo.YahooScraper
import com.example.there.data.api.youtube.YoutubeApi
import com.example.there.data.mapper.videos.ChannelThumbnailUrlMapper
import com.example.there.data.mapper.videos.VideoMapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.VideosDataStore
import io.reactivex.Observable

class RemoteVideosDataStore(private val api: YoutubeApi,
                            private val scraper: YahooScraper) : VideosDataStore {

    override fun getVideos(query: String): Observable<List<VideoEntity>> = scraper.getVideoIds(query)
            .map {
                it.chunked(50)
                        .map {
                            api.loadVideosInfo(ids = it.joinToString(","))
                                    .map { it.videos.map(VideoMapper::mapFrom) }
                        }
            }
            .flatMapIterable { it }
            .switchMap { it }

    override fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Observable<List<String>> {
        return Observable.fromIterable(videos.chunked(50)
                .map { api.loadChannelsInfo(ids = it.joinToString(",") { it.channelId }) }
                .map { it.map { it.channels.map(ChannelThumbnailUrlMapper::mapFrom) } })
                .switchMap { it }
    }
}