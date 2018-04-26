package com.example.there.data.repos.videos

import com.example.there.data.api.yahoo.YahooScraper
import com.example.there.data.api.youtube.YoutubeApi
import com.example.there.data.entities.videos.VideoData
import com.example.there.data.repos.videos.stores.RemoteVideosDataStore
import com.example.there.domain.common.Mapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.VideosRepository
import io.reactivex.Observable

class VideosRepositoryImpl(api: YoutubeApi,
                           scraper: YahooScraper,
                           videoMapper: Mapper<VideoData, VideoEntity>) : VideosRepository {

    private val remoteVideosDataStore = RemoteVideosDataStore(api, scraper, videoMapper)

    override fun getVideos(query: String): Observable<List<VideoEntity>> = remoteVideosDataStore.getVideos(query)
}