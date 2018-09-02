package com.example.there.data.repos.videos

import com.example.there.domain.entities.videos.VideoEntity
import com.example.there.domain.entities.videos.VideoPlaylistEntity
import com.example.there.domain.repos.videos.IVideosRepository
import com.example.there.domain.repos.videos.datastores.IVideosDbDataStore
import com.example.there.domain.repos.videos.datastores.IVideosRemoteDataStore
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class VideosRepository @Inject constructor(
        private val remoteDataStore: IVideosRemoteDataStore,
        private val dbDataStore: IVideosDbDataStore
) : IVideosRepository {

    override fun getChannelsThumbnailUrls(
            videos: List<VideoEntity>
    ): Single<List<Pair<Int, String>>> = remoteDataStore.getChannelsThumbnailUrls(videos)

    override fun getVideos(
            query: String
    ): Single<List<VideoEntity>> = dbDataStore.getSavedVideosForQuery(query)
            .flatMap {
                if (it.isEmpty()) {
                    remoteDataStore.getVideos(query)
                            .flatMap { (nextPageToken, videos) ->
                                dbDataStore.insertVideosForNewQuery(query, videos, nextPageToken)
                                        .andThen(Single.just(videos))
                            }
                } else {
                    Single.just(it)
                }
            }


    override fun getMoreVideos(
            query: String
    ): Single<List<VideoEntity>> = dbDataStore.getNextPageTokenForQuery(query)
            .flatMapSingle { remoteDataStore.getVideos(query, it) }
            .flatMap { (nextPageToken, videos) ->
                dbDataStore.insertVideosForQuery(query, videos, nextPageToken)
                        .andThen(Single.just(videos))
            }

    override fun getRelatedVideos(
            videoId: String
    ): Single<List<VideoEntity>> = dbDataStore.getRelatedVideosForVideoId(videoId)
            .flatMap {
                if (it.isEmpty()) {
                    remoteDataStore.getRelatedVideos(videoId)
                            .flatMap { (nextPageToken, videos) ->
                                dbDataStore.insertRelatedVideosForNewVideoId(videoId, videos, nextPageToken)
                                        .andThen(Single.just(videos))
                            }
                } else {
                    Single.just(it)
                }
            }

    override fun getMoreRelatedVideos(
            videoId: String
    ): Single<List<VideoEntity>> = dbDataStore.getNextPageTokenForVideoId(videoId)
            .flatMapSingle { remoteDataStore.getRelatedVideos(videoId, it) }
            .flatMap { (nextPageToken, videos) ->
                dbDataStore.insertRelatedVideosForVideoId(videoId, videos, nextPageToken)
                        .andThen(Single.just(videos))
            }

    override fun getFavouritePlaylists(): Single<List<VideoPlaylistEntity>> = dbDataStore.getFavouritePlaylists()

    override fun getVideosFromPlaylist(playlistId: Long): Single<List<VideoEntity>> = dbDataStore.getVideosFromPlaylist(playlistId)

    override fun insertPlaylist(playlistEntity: VideoPlaylistEntity): Single<Long> = dbDataStore.insertPlaylist(playlistEntity)

    override fun addVideoToPlaylist(videoEntity: VideoEntity, playlistEntity: VideoPlaylistEntity): Completable =
            dbDataStore.addVideoToPlaylist(videoEntity, playlistEntity)
}