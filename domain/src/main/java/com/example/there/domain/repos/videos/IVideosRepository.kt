package com.example.there.domain.repos.videos

import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.entities.videos.VideoPlaylistEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IVideosRepository {
    fun getVideos(query: String, pageToken: String? = null): Observable<Pair<String?, List<VideoEntity>>>
    fun getRelatedVideos(toVideoId: String, pageToken: String? = null): Observable<Pair<String?, List<VideoEntity>>>
    fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Observable<List<String>>

    fun getFavouritePlaylists(): Single<List<VideoPlaylistEntity>>
    fun getVideosFromPlaylist(playlistId: Long): Single<List<VideoEntity>>
    fun insertPlaylist(playlistEntity: VideoPlaylistEntity): Completable
    fun addVideoToPlaylist(videoEntity: VideoEntity, playlistEntity: VideoPlaylistEntity): Completable
}