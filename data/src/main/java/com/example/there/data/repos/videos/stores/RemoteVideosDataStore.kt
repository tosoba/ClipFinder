package com.example.there.data.repos.videos.stores

import com.example.there.data.api.yahoo.YahooScraper
import com.example.there.data.api.youtube.YoutubeApi
import com.example.there.data.entities.videos.VideoData
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.VideosDataStore
import io.reactivex.Observable

class RemoteVideosDataStore(private val api: YoutubeApi,
                            private val scraper: YahooScraper,
                            private val videoMapper: Mapper<VideoData, VideoEntity>) : VideosDataStore {

    override fun getVideoIds(query: String): Observable<List<String>> = scraper.getVideoIds(query)

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
}