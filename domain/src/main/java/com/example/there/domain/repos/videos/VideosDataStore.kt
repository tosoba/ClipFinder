package com.example.there.domain.repos.videos

import com.example.there.domain.entities.videos.VideoEntity
import io.reactivex.Observable

interface VideosDataStore {
    fun getVideos(query: String): Observable<List<VideoEntity>>

    fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Observable<List<String>>
}