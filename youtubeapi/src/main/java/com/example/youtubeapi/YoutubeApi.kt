package com.example.youtubeapi

import com.example.core.model.NetworkResponse
import com.example.youtubeapi.model.ChannelsResponse
import com.example.youtubeapi.model.VideosResponse
import com.example.youtubeapi.model.VideosSearchResponse
import com.example.youtubeapi.model.YoutubeErrorResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
    @GET("videos")
    fun loadVideosInfo(
        @Query("part") part: String = "snippet,contentDetails,statistics",
        @Query("id") ids: String,
        @Query("maxResults") maxResults: String = "50",
        @Query("key") key: String = YoutubeAuth.key
    ): Single<NetworkResponse<VideosResponse, YoutubeErrorResponse>>

    @GET("search")
    fun searchRelatedVideos(
        @Query("part") part: String = "snippet",
        @Query("relatedToVideoId") toVideoId: String,
        @Query("type") type: String = "video",
        @Query("maxResults") maxResults: String = "50",
        @Query("pageToken") pageToken: String? = null,
        @Query("key") key: String = YoutubeAuth.key
    ): Single<NetworkResponse<VideosSearchResponse, YoutubeErrorResponse>>

    @GET("channels")
    fun loadChannelsInfo(
        @Query("part") part: String = "snippet",
        @Query("id") ids: String,
        @Query("maxResults") maxResults: String = "50",
        @Query("key") key: String = YoutubeAuth.key
    ): Single<NetworkResponse<ChannelsResponse, YoutubeErrorResponse>>
}
