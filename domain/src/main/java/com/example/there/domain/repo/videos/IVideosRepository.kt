package com.example.there.domain.repo.videos

import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.entity.videos.VideoPlaylistThumbnailsEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface IVideosRepository {
    fun getVideos(query: String): Single<List<VideoEntity>>
    fun getMoreVideos(query: String): Single<List<VideoEntity>>

    fun getRelatedVideos(videoId: String): Single<List<VideoEntity>>
    fun getMoreRelatedVideos(videoId: String): Single<List<VideoEntity>>

    fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Single<List<Pair<Int, String>>>

    val favouritePlaylists: Flowable<List<VideoPlaylistEntity>>
    fun getVideosFromPlaylist(playlistId: Long): Flowable<List<VideoEntity>>
    fun insertPlaylist(playlistEntity: VideoPlaylistEntity): Single<Long>
    fun addVideoToPlaylist(videoEntity: VideoEntity, playlistEntity: VideoPlaylistEntity): Completable

    val videoPlaylistsWithThumbnails: Flowable<VideoPlaylistThumbnailsEntity>

    fun deleteVideo(videoEntity: VideoEntity): Completable
    fun deleteVideoPlaylist(videoPlaylistEntity: VideoPlaylistEntity): Completable

    fun deleteAllVideoSearchData(): Completable
}