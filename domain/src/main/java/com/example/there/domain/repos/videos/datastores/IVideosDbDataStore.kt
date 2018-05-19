package com.example.there.domain.repos.videos.datastores

import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.entities.videos.VideoPlaylistEntity
import io.reactivex.Completable
import io.reactivex.Single

interface IVideosDbDataStore {
    fun getFavouritePlaylists(): Single<List<VideoPlaylistEntity>>
    fun getVideosFromPlaylist(playlistId: Long): Single<List<VideoEntity>>

    fun insertPlaylist(playlistEntity: VideoPlaylistEntity): Completable
    fun addVideoToPlaylist(videoEntity: VideoEntity, playlistEntity: VideoPlaylistEntity): Completable
}