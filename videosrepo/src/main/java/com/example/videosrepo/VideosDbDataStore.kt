package com.example.videosrepo

import com.example.db.RelatedVideoSearchDao
import com.example.db.VideoDao
import com.example.db.VideoPlaylistDao
import com.example.db.VideoSearchDao
import com.example.db.model.videos.RelatedVideoSearchDbModel
import com.example.db.model.videos.VideoDbModel
import com.example.db.model.videos.VideoPlaylistDbModel
import com.example.db.model.videos.VideoSearchDbModel
import com.example.there.domain.entity.videos.VideoEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.entity.videos.VideoPlaylistThumbnailsEntity
import com.example.there.domain.repo.videos.IVideosDbDataStore
import com.example.videosrepo.mapper.data
import com.example.videosrepo.mapper.db
import com.example.videosrepo.mapper.domain
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

class VideosDbDataStore(
        private val videoDao: VideoDao,
        private val videoPlaylistDao: VideoPlaylistDao,
        private val videoSearchDao: VideoSearchDao,
        private val relatedVideoSearchDao: RelatedVideoSearchDao
) : IVideosDbDataStore {

    override fun getRelatedVideosForVideoId(
            videoId: String
    ): Single<List<VideoEntity>> = videoDao.findAllRelatedToVideo(videoId)
            .map { it.map(VideoDbModel::domain) }

    override fun getNextPageTokenForVideoId(
            videoId: String
    ): Maybe<String> = relatedVideoSearchDao.getNextPageTokenForVideoId(videoId)

    override fun insertRelatedVideosForNewVideoId(
            videoId: String,
            videos: List<VideoEntity>,
            nextPageToken: String?
    ): Completable = Completable.fromAction {
        relatedVideoSearchDao.insert(RelatedVideoSearchDbModel(videoId, nextPageToken))
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
        videoDao.insertMany(*videos.map(VideoEntity::db).toTypedArray())
    }

    override fun getNextPageTokenForQuery(
            query: String
    ): Maybe<String> = videoSearchDao.getNextPageTokenForQuery(query)

    override fun insertVideosForNewQuery(
            query: String,
            videos: List<VideoEntity>,
            nextPageToken: String?
    ): Completable = Completable.fromAction {
        videoSearchDao.insert(VideoSearchDbModel(query, nextPageToken))
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
        videoDao.insertMany(*videos.map(VideoEntity::db).toTypedArray())
    }

    override fun getSavedVideosForQuery(
            query: String
    ): Single<List<VideoEntity>> = videoDao.findAllWithQuery(query)
            .map { it.map(VideoDbModel::domain) }

    override val favouritePlaylists: Flowable<List<VideoPlaylistEntity>>
        get() = videoPlaylistDao.findAll().map { it.map(VideoPlaylistDbModel::domain) }

    override fun getVideosFromPlaylist(
            playlistId: Long
    ): Flowable<List<VideoEntity>> = videoDao.findVideosFromPlaylist(playlistId)
            .map { it.map(VideoDbModel::domain) }

    override fun insertPlaylist(playlistEntity: VideoPlaylistEntity): Single<Long> = Single.fromCallable {
        videoPlaylistDao.insert(playlistEntity.data)
    }

    override fun addVideoToPlaylist(
            videoEntity: VideoEntity, playlistEntity: VideoPlaylistEntity
    ): Completable = Completable.fromCallable {
        videoEntity.playlistId = playlistEntity.id
        videoDao.insert(videoEntity.db)
    }

    override val videoPlaylistsWithThumbnails: Flowable<VideoPlaylistThumbnailsEntity>
        get() = videoPlaylistDao.findAll()
                .flatMapIterable { it }
                .filter { it.id != null }
                .flatMap { playlist ->
                    videoDao.find5VideosFromPlaylist(playlist.id!!)
                            .map { videos ->
                                VideoPlaylistThumbnailsEntity(
                                        playlist.domain,
                                        videos.map { it.thumbnailUrl }
                                )
                            }
                }
                .filter { it.thumbnailUrls.isNotEmpty() }

    override fun deleteVideo(videoEntity: VideoEntity): Completable = Completable.fromCallable {
        videoDao.delete(videoEntity.db)
    }

    override fun deleteVideoPlaylist(
            videoPlaylistEntity: VideoPlaylistEntity
    ): Completable = Completable.fromCallable {
        videoPlaylistDao.delete(videoPlaylistEntity.data)
    }

    override fun deleteAllVideoSearchData(): Completable = Completable.merge(listOf(
            Completable.fromAction { videoSearchDao.deleteAll() },
            Completable.fromAction { relatedVideoSearchDao.deleteAll() }
    )).andThen {
        Completable.fromAction { videoDao.deleteAllWithNullForeignKeys() }
    }
}