package com.example.there.data.repos.videos

import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.entities.videos.VideoPlaylistEntity
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.repos.videos.datastores.IVideosDbDataStore
import com.example.there.domain.repos.videos.datastores.IVideosRemoteDataStore
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class VideosRepository(private val remoteDataStore: IVideosRemoteDataStore,
                       private val dbDataStore: IVideosDbDataStore) : IVideosRepository {

    override fun getChannelsThumbnailUrls(videos: List<VideoEntity>): Observable<List<String>> =
            remoteDataStore.getChannelsThumbnailUrls(videos)

    override fun getVideos(query: String, pageToken: String?): Observable<Pair<String?, List<VideoEntity>>> =
            remoteDataStore.getVideos(query, pageToken)

    override fun getRelatedVideos(toVideoId: String, pageToken: String?): Observable<Pair<String?, List<VideoEntity>>> =
            remoteDataStore.getVideos(toVideoId, pageToken)

    override fun getFavouritePlaylists(): Single<List<VideoPlaylistEntity>> = dbDataStore.getFavouritePlaylists()

    override fun getVideosFromPlaylist(playlistId: Long): Single<List<VideoEntity>> = dbDataStore.getVideosFromPlaylist(playlistId)

    override fun insertPlaylist(playlistEntity: VideoPlaylistEntity): Single<Long> = dbDataStore.insertPlaylist(playlistEntity)

    override fun addVideoToPlaylist(videoEntity: VideoEntity, playlistEntity: VideoPlaylistEntity): Completable =
            dbDataStore.addVideoToPlaylist(videoEntity, playlistEntity)
}