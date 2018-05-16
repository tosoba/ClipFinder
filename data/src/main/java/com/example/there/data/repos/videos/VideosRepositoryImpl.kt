package com.example.there.data.repos.videos

import com.example.there.data.apis.youtube.YoutubeApi
import com.example.there.data.repos.videos.stores.RemoteVideosDataStore
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.VideosRepository
import io.reactivex.Observable

class VideosRepositoryImpl(api: YoutubeApi) : VideosRepository {
    private val remoteVideosDataStore = RemoteVideosDataStore(api)

    override fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Observable<List<String>> =
            remoteVideosDataStore.getChannelsThumbnailUrls(videos)

    override fun getVideos(query: String, pageToken: String?): Observable<Pair<String?, List<VideoEntity>>> =
            remoteVideosDataStore.getVideos(query, pageToken)

    override fun getRelatedVideos(toVideoId: String, pageToken: String?): Observable<Pair<String?, List<VideoEntity>>> =
            remoteVideosDataStore.getVideos(toVideoId, pageToken)
}