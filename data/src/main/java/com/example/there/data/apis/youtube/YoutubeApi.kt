package com.example.there.data.apis.youtube

import com.example.there.data.responses.ChannelsResponse
import com.example.there.data.responses.VideosResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
    @GET("videos")
    fun loadVideosInfo(@Query("part") part: String = "snippet,contentDetails,statistics",
                       @Query("id") ids: String,
                       @Query("maxResults") maxResults: String = "50",
                       @Query("key") key: String = "AIzaSyDunN4g06F7QjOJfEkQPJ8Ahhp1bvBY-Bs"): Observable<VideosResponse>

    @GET("channels")
    fun loadChannelsInfo(@Query("part") part: String = "snippet",
                         @Query("id") ids: String,
                         @Query("maxResults") maxResults: String = "50",
                         @Query("key") key: String = "AIzaSyDunN4g06F7QjOJfEkQPJ8Ahhp1bvBY-Bs"): Observable<ChannelsResponse>
}