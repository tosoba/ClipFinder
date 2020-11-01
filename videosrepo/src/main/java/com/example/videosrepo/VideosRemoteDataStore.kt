package com.example.videosrepo

import com.example.core.model.Resource
import com.example.core.retrofit.NetworkResponse
import com.example.core.retrofit.mapSuccess
import com.example.core.retrofit.mapToResource
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.repo.videos.IVideosRemoteDataStore
import com.example.videosrepo.mapper.domain
import com.example.videosrepo.util.urlMedium
import com.example.youtubeapi.YoutubeApi
import com.example.youtubeapi.model.VideoApiModel
import com.example.youtubeapi.model.VideosSearchResponse
import com.example.youtubeapi.model.YoutubeErrorResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction

class VideosRemoteDataStore(private val api: YoutubeApi) : IVideosRemoteDataStore {

    override fun getChannelsThumbnailUrls(
        videos: List<VideoEntity>
    ): Single<Resource<List<Pair<Int, String>>>> = Observable.fromIterable(videos.chunked(50))
        .zipWith(
            Observable.range(0, Int.MAX_VALUE),
            BiFunction<List<VideoEntity>, Int, Pair<Int, List<VideoEntity>>> { vids, chunkIndex ->
                Pair(chunkIndex, vids)
            }
        )
        .flatMap { (chunkIndex, vids) ->
            api.loadChannelsInfo(ids = vids.joinToString(",") { it.channelId })
                .toObservable()
                .mapSuccess { channels.map { it.snippet.thumbnails.urlMedium } }
                .concatMapIterable { it }
                .zipWith(
                    Observable.range(0, Int.MAX_VALUE),
                    BiFunction<String, Int, Pair<Int, String>> { url, urlIndex ->
                        Pair(urlIndex + 50 * chunkIndex, url)
                    }
                )
        }
        .toList()
        .map { Resource.Success(it) }

    override fun getRelatedVideos(
        toVideoId: String,
        pageToken: String?
    ): Single<Resource<Pair<String?, List<VideoEntity>>>> = api.searchRelatedVideos(
        toVideoId = toVideoId,
        pageToken = pageToken
    ).mapToResourceWithPageTokenAndVideos()

    private fun Single<NetworkResponse<VideosSearchResponse, YoutubeErrorResponse>>.mapToResourceWithPageTokenAndVideos(): Single<Resource<Pair<String?, List<VideoEntity>>>> {
        return flatMap<Resource<Pair<String?, List<VideoEntity>>>> { searchResponse ->
            when (searchResponse) {
                is NetworkResponse.Success<VideosSearchResponse> -> api.loadVideosInfo(
                    ids = searchResponse.body.videos.joinToString(",") { it.id.id }
                ).mapToResource {
                    Pair(searchResponse.body.nextPageToken, videos.map(VideoApiModel::domain))
                }
                is NetworkResponse.ServerError -> Single.just(Resource.Error(searchResponse.code))
                is NetworkResponse.NetworkError -> Single.just(Resource.Error(searchResponse.error))
                is NetworkResponse.DifferentError -> Single.just(Resource.Error(searchResponse.error))
            }
        }
    }
}