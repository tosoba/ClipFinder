package com.example.there.domain.repo.videos

import com.example.core.model.Resource
import com.example.there.domain.entity.videos.VideoEntity
import io.reactivex.Single

interface IVideosRemoteDataStore {
    fun getRelatedVideos(toVideoId: String, pageToken: String? = null): Single<Resource<Pair<String?, List<VideoEntity>>>>
    fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Single<Resource<List<Pair<Int, String>>>>
}
