package com.example.there.data.repos.videos.datastores

import com.example.there.data.db.VideoDao
import com.example.there.data.db.VideoPlaylistDao
import com.example.there.data.mappers.videos.VideoMapper
import com.example.there.data.mappers.videos.VideoPlaylistMapper
import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.entities.videos.VideoPlaylistEntity
import com.example.there.domain.repos.videos.datastores.IVideosDbDataStore
import io.reactivex.Single

class VideosDbDataStore(private val videoDao: VideoDao,
                        private val videoPlaylistDao: VideoPlaylistDao) : IVideosDbDataStore {

    override fun getFavouritePlaylists(): Single<List<VideoPlaylistEntity>> =
            videoPlaylistDao.findAll().map { it.map(VideoPlaylistMapper::mapFrom) }

    override fun getVideosFromPlaylist(playlistId: Long): Single<List<VideoEntity>> =
            videoDao.findVideosFromPlaylist(playlistId).map { it.map(VideoMapper::mapFrom) }
}