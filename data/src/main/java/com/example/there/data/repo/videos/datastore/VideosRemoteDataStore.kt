package com.example.there.data.repo.videos.datastore

import com.example.there.data.api.youtube.YoutubeApi
import com.example.there.data.mapper.videos.ChannelThumbnailUrlMapper
import com.example.there.data.mapper.videos.VideoMapper
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.datastore.IVideosRemoteDataStore
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideosRemoteDataStore @Inject constructor(
        private val api: YoutubeApi
) : IVideosRemoteDataStore {

    override fun getChannelsThumbnailUrls(
            videos: List<VideoEntity>
    ): Single<List<Pair<Int, String>>> = Observable.fromIterable(videos.chunked(50))
            .zipWith(
                    Observable.range(0, Int.MAX_VALUE),
                    BiFunction<List<VideoEntity>, Int, Pair<Int, List<VideoEntity>>> { vids, chunkIndex ->
                        Pair(chunkIndex, vids)
                    }
            )
            .flatMap { (chunkIndex, vids) ->
                api.loadChannelsInfo(ids = vids.joinToString(",") { it.channelId })
                        .toObservable()
                        .map { it.channels.map(ChannelThumbnailUrlMapper::mapFrom) }
                        .concatMapIterable { it }
                        .zipWith(
                                Observable.range(0, Int.MAX_VALUE),
                                BiFunction<String, Int, Pair<Int, String>> { url, urlIndex ->
                                    Pair(urlIndex + 50 * chunkIndex, url)
                                }
                        )
            }
            .toList()

    override fun getVideos(
            query: String,
            pageToken: String?
    ): Single<Pair<String?, List<VideoEntity>>> = api.searchVideos(query = query, pageToken = pageToken)
            .flatMap { searchResponse ->
                api.loadVideosInfo(ids = searchResponse.videos.joinToString(",") { it.id.id })
                        .map { it.videos.map(VideoMapper::mapFrom) }
                        .map { Pair(searchResponse.nextPageToken, it) }
            }

    override fun getRelatedVideos(
            toVideoId: String,
            pageToken: String?
    ): Single<Pair<String?, List<VideoEntity>>> = api.searchRelatedVideos(toVideoId = toVideoId, pageToken = pageToken)
            .flatMap { searchResponse ->
                api.loadVideosInfo(ids = searchResponse.videos.joinToString(",") { it.id.id })
                        .map { it.videos.map(VideoMapper::mapFrom) }
                        .map { Pair(searchResponse.nextPageToken, it) }
            }
}