package com.example.there.data.repos.videos.stores

import com.example.there.data.api.yahoo.YahooScraper
import com.example.there.data.api.youtube.YoutubeApi
import com.example.there.data.entities.videos.ChannelData
import com.example.there.data.entities.videos.VideoData
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.VideosDataStore
import io.reactivex.Observable

class RemoteVideosDataStore(private val api: YoutubeApi,
                            private val scraper: YahooScraper,
                            private val videoMapper: Mapper<VideoData, VideoEntity>,
                            private val channelThumbnailUrlMapper: Mapper<ChannelData, String>) : VideosDataStore {

    override fun getVideos(query: String): Observable<List<VideoEntity>> = scraper.getVideoIds(query)
            .map {
                it.chunked(50)
                        .map {
                            api.loadVideosInfo(ids = it.joinToString(","))
                                    .map { it.videos.map(videoMapper::mapFrom) }
                        }
            }
            .flatMapIterable { it }
            .switchMap { it }

    override fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Observable<List<String>> {
        return Observable.fromIterable(videos.chunked(50)
                .map { api.loadChannelsInfo(ids = it.joinToString(",") { it.channelId }) }
                .map { it.map { it.channels.map(channelThumbnailUrlMapper::mapFrom) } })
                .switchMap { it }
    }
}