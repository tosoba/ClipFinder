package com.example.there.domain.repo.videos

import com.example.there.domain.entity.videos.VideoEntity
import io.reactivex.Single

interface IVideosRemoteDataStore {
    fun getVideos(query: String, pageToken: String? = null): Single<Pair<String?, List<VideoEntity>>>

    fun getRelatedVideos(toVideoId: String, pageToken: String? = null): Single<Pair<String?, List<VideoEntity>>>

    fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Single<List<Pair<Int, String>>>
}