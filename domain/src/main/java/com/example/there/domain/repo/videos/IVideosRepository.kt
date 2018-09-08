package com.example.there.domain.repo.videos

import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import io.reactivex.Completable
import io.reactivex.Single

interface IVideosRepository {
    fun getVideos(query: String): Single<List<VideoEntity>>
    fun getMoreVideos(query: String): Single<List<VideoEntity>>

    fun getRelatedVideos(videoId: String): Single<List<VideoEntity>>
    fun getMoreRelatedVideos(videoId: String): Single<List<VideoEntity>>

    fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Single<List<Pair<Int, String>>>

    fun getFavouritePlaylists(): Single<List<VideoPlaylistEntity>>
    fun getVideosFromPlaylist(playlistId: Long): Single<List<VideoEntity>>
    fun insertPlaylist(playlistEntity: VideoPlaylistEntity): Single<Long>
    fun addVideoToPlaylist(videoEntity: VideoEntity, playlistEntity: VideoPlaylistEntity): Completable
}