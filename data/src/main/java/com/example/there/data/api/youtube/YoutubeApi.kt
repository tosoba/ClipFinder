package com.example.there.data.api.youtube

import com.example.there.data.api.YoutubeClient
import com.example.there.data.response.ChannelsResponse
import com.example.there.data.response.VideosResponse
import com.example.there.data.response.VideosSearchResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
    @GET("videos")
    fun loadVideosInfo(
            @Query("part") part: String = "snippet,contentDetails,statistics",
            @Query("id") ids: String,
            @Query("maxResults") maxResults: String = "50",
            @Query("key") key: String = YoutubeClient.key
    ): Single<VideosResponse>

    @GET("search")
    fun searchVideos(
            @Query("part") part: String = "snippet",
            @Query("q") query: String,
            @Query("type") type: String = "video",
            @Query("maxResults") maxResults: String = "50",
            @Query("pageToken") pageToken: String? = null,
            @Query("key") key: String = YoutubeClient.key
    ): Single<VideosSearchResponse>

    @GET("search")
    fun searchRelatedVideos(
            @Query("part") part: String = "snippet",
            @Query("relatedToVideoId") toVideoId: String,
            @Query("type") type: String = "video",
            @Query("maxResults") maxResults: String = "50",
            @Query("pageToken") pageToken: String? = null,
            @Query("key") key: String = YoutubeClient.key
    ): Single<VideosSearchResponse>

    @GET("channels")
    fun loadChannelsInfo(
            @Query("part") part: String = "snippet",
            @Query("id") ids: String,
            @Query("maxResults") maxResults: String = "50",
            @Query("key") key: String = YoutubeClient.key
    ): Single<ChannelsResponse>
}