package com.example.there.domain.repos.videos

import com.example.there.domain.entities.videos.VideoEntity
import io.reactivex.Observable

interface VideosDataStore {
    fun getVideoIds(query: String): Observable<List<String>>

    fun getVideos(query: String): Observable<List<VideoEntity>>
}