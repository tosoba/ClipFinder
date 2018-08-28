package com.example.there.domain.repos.videos.datastores

import com.example.there.domain.entities.videos.VideoEntity
import io.reactivex.Observable

interface IVideosRemoteDataStore {
    fun getVideos(query: String, pageToken: String? = null): Observable<Pair<String?, List<VideoEntity>>>

    fun getRelatedVideos(toVideoId: String, pageToken: String? = null): Observable<Pair<String?, List<VideoEntity>>>

    fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Observable<List<String>>
}