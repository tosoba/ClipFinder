package com.example.there.data.repo.videos.datastore

import com.example.there.data.db.RelatedVideoSearchDao
import com.example.there.data.db.VideoDao
import com.example.there.data.db.VideoPlaylistDao
import com.example.there.data.db.VideoSearchDao
import com.example.there.data.entity.videos.RelatedVideoSearchDbData
import com.example.there.data.entity.videos.VideoSearchDbData
import com.example.there.data.mapper.videos.VideoDbMapper
import com.example.there.data.mapper.videos.VideoPlaylistMapper
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.entity.videos.VideoPlaylistThumbnailsEntity
import com.example.there.domain.repo.videos.datastore.IVideosDbDataStore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class VideosDbDataStore @Inject constructor(
        private val videoDao: VideoDao,
        private val videoPlaylistDao: VideoPlaylistDao,
        private val videoSearchDao: VideoSearchDao,
        private val relatedVideoSearchDao: RelatedVideoSearchDao
) : IVideosDbDataStore {

    override fun getRelatedVideosForVideoId(
            videoId: String
    ): Single<List<VideoEntity>> = videoDao.findAllRelatedToVideo(videoId)
            .map { it.map(VideoDbMapper::mapFrom) }

    override fun getNextPageTokenForVideoId(
            videoId: String
    ): Maybe<String> = relatedVideoSearchDao.getNextPageTokenForVideoId(videoId)

    override fun insertRelatedVideosForNewVideoId(
            videoId: String,
            videos: List<VideoEntity>,
            nextPageToken: String?
    ): Completable = Completable.fromAction {
        relatedVideoSearchDao.insert(RelatedVideoSearchDbData(videoId, nextPageToken))
        insertRelatedVideos(videoId, videos)
    }

    override fun insertRelatedVideosForVideoId(
            videoId: String,
            videos: List<VideoEntity>,
            nextPageToken: String?
    ): Completable = Completable.fromAction {
        relatedVideoSearchDao.updateNextPageTokenForVideo(videoId, nextPageToken)
        insertRelatedVideos(videoId, videos)
    }

    private fun insertRelatedVideos(videoId: String, videos: List<VideoEntity>) {
        videos.forEach { it.relatedVideoId = videoId }
        videoDao.insertMany(*videos.map(VideoDbMapper::mapBack).toTypedArray())
    }

    override fun getNextPageTokenForQuery(
            query: String
    ): Maybe<String> = videoSearchDao.getNextPageTokenForQuery(query)

    override fun insertVideosForNewQuery(
            query: String,
            videos: List<VideoEntity>,
            nextPageToken: String?
    ): Completable = Completable.fromAction {
        videoSearchDao.insert(VideoSearchDbData(query, nextPageToken))
        insertVideosWithQuery(query, videos)
    }

    override fun insertVideosForQuery(
            query: String,
            videos: List<VideoEntity>,
            nextPageToken: String?
    ): Completable = Completable.fromAction {
        videoSearchDao.updateNextPageTokenForQuery(query, nextPageToken)
        insertVideosWithQuery(query, videos)
    }

    private fun insertVideosWithQuery(query: String, videos: List<VideoEntity>) {
        videos.forEach { it.query = query }
        videoDao.insertMany(*videos.map(VideoDbMapper::mapBack).toTypedArray())
    }

    override fun getSavedVideosForQuery(
            query: String
    ): Single<List<VideoEntity>> = videoDao.findAllWithQuery(query)
            .map { it.map(VideoDbMapper::mapFrom) }

    override fun getFavouritePlaylists(): Flowable<List<VideoPlaylistEntity>> = videoPlaylistDao.findAll()
            .map { it.map(VideoPlaylistMapper::mapFrom) }

    override fun getVideosFromPlaylist(
            playlistId: Long
    ): Flowable<List<VideoEntity>> = videoDao.findVideosFromPlaylist(playlistId)
            .map { it.map(VideoDbMapper::mapFrom) }

    override fun insertPlaylist(playlistEntity: VideoPlaylistEntity): Single<Long> = Single.fromCallable {
        videoPlaylistDao.insert(VideoPlaylistMapper.mapBack(playlistEntity))
    }

    override fun addVideoToPlaylist(videoEntity: VideoEntity, playlistEntity: VideoPlaylistEntity): Completable = Completable.fromCallable {
        videoEntity.playlistId = playlistEntity.id
        videoDao.insert(VideoDbMapper.mapBack(videoEntity))
    }

    override fun getVideoPlaylistsWithThumbnails(): Flowable<VideoPlaylistThumbnailsEntity> = videoPlaylistDao.findAll()
            .flatMapIterable { it }
            .flatMap { playlist ->
                videoDao.find5VideosFromPlaylist(playlist.id)
                        .toFlowable()
                        .map {
                            VideoPlaylistThumbnailsEntity(
                                playlist.id,
                                playlist.name,
                                it.map { it.thumbnailUrl }
                            )
                        }
            }
}