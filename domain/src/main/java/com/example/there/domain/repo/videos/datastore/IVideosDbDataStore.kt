package com.example.there.domain.repo.videos.datastore

import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.entity.videos.VideoPlaylistThumbnailsEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface IVideosDbDataStore {
    fun getFavouritePlaylists(): Flowable<List<VideoPlaylistEntity>>
    fun getVideosFromPlaylist(playlistId: Long): Flowable<List<VideoEntity>>

    fun getSavedVideosForQuery(query: String): Single<List<VideoEntity>>
    fun getNextPageTokenForQuery(query: String): Maybe<String>
    fun insertVideosForNewQuery(query: String, videos: List<VideoEntity>, nextPageToken: String?): Completable
    fun insertVideosForQuery(query: String, videos: List<VideoEntity>, nextPageToken: String?): Completable

    fun getRelatedVideosForVideoId(videoId: String): Single<List<VideoEntity>>
    fun getNextPageTokenForVideoId(videoId: String): Maybe<String>
    fun insertRelatedVideosForNewVideoId(videoId: String, videos: List<VideoEntity>, nextPageToken: String?): Completable
    fun insertRelatedVideosForVideoId(videoId: String, videos: List<VideoEntity>, nextPageToken: String?): Completable

    fun insertPlaylist(playlistEntity: VideoPlaylistEntity): Single<Long>
    fun addVideoToPlaylist(videoEntity: VideoEntity, playlistEntity: VideoPlaylistEntity): Completable

    fun getVideoPlaylistsWithThumbnails(): Flowable<VideoPlaylistThumbnailsEntity>
}