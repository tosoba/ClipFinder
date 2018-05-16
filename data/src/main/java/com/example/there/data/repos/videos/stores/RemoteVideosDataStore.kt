package com.example.there.data.repos.videos.stores

import com.example.there.data.apis.youtube.YoutubeApi
import com.example.there.data.mappers.videos.ChannelThumbnailUrlMapper
import com.example.there.data.mappers.videos.VideoMapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.repos.videos.VideosDataStore
import io.reactivex.Observable

class RemoteVideosDataStore(private val api: YoutubeApi) : VideosDataStore {

    override fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Observable<List<String>> {
        return Observable.fromIterable(videos.chunked(50)
                .map { api.loadChannelsInfo(ids = it.joinToString(",") { it.channelId }) }
                .map { it.map { it.channels.map(ChannelThumbnailUrlMapper::mapFrom) } })
                .switchMap { it }
    }

    override fun getVideos(query: String, pageToken: String?): Observable<Pair<String?, List<VideoEntity>>> =
            api.searchVideos(query = query, pageToken = pageToken)
                    .flatMap { searchResponse ->
                        api.loadVideosInfo(ids = searchResponse.videos.joinToString(",") { it.id.id })
                                .map { it.videos.map(VideoMapper::mapFrom) }
                                .map { Pair(searchResponse.nextPageToken, it) }
                    }

    override fun getRelatedVideos(toVideoId: String, pageToken: String?): Observable<Pair<String?, List<VideoEntity>>> =
            api.searchRelatedVideos(toVideoId = toVideoId, pageToken = pageToken)
                    .flatMap { searchResponse ->
                        api.loadVideosInfo(ids = searchResponse.videos.joinToString(",") { it.id.id })
                                .map { it.videos.map(VideoMapper::mapFrom) }
                                .map { Pair(searchResponse.nextPageToken, it) }
                    }
}