package com.example.youtubeapi

import com.example.youtubeapi.model.ChannelsResponse
import com.example.youtubeapi.model.VideosResponse
import com.example.youtubeapi.model.VideosSearchResponse
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
    ): Single<VideosResponse>

    @GET("search")
    fun searchVideos(
            @Query("part") part: String = "snippet",
            @Query("q") query: String,
            @Query("type") type: String = "video",
            @Query("maxResults") maxResults: String = "50",
            @Query("pageToken") pageToken: String? = null,
            @Query("key") key: String = YoutubeAuth.key
    ): Single<VideosSearchResponse>

    @GET("search")
    fun searchRelatedVideos(
            @Query("part") part: String = "snippet",
            @Query("relatedToVideoId") toVideoId: String,
            @Query("type") type: String = "video",
            @Query("maxResults") maxResults: String = "50",
            @Query("pageToken") pageToken: String? = null,
            @Query("key") key: String = YoutubeAuth.key
    ): Single<VideosSearchResponse>

    @GET("channels")
    fun loadChannelsInfo(
            @Query("part") part: String = "snippet",
            @Query("id") ids: String,
            @Query("maxResults") maxResults: String = "50",
            @Query("key") key: String = YoutubeAuth.key
    ): Single<ChannelsResponse>
}