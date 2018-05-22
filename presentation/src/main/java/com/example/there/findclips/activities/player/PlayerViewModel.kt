package com.example.there.findclips.activities.player

import com.example.there.domain.usecases.videos.*
import com.example.there.findclips.base.BaseVideosViewModel
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.model.entities.VideoPlaylist
import com.example.there.findclips.model.mappers.VideoEntityMapper
import com.example.there.findclips.model.mappers.VideoPlaylistEntityMapper

class PlayerViewModel(private val searchRelatedVideos: SearchRelatedVideos,
                      getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
                      private val insertVideoPlaylist: InsertVideoPlaylist,
                      private val addVideoToPlaylist: AddVideoToPlaylist,
                      private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists) : BaseVideosViewModel(getChannelsThumbnailUrls) {

    private var lastSearchVideoId: String? = null
    private var lastSearchRelatedNextPageToken: String? = null

    val viewState = PlayerViewState()

    fun searchRelatedVideosWithToLastId() {
        if (lastSearchVideoId != null && lastSearchRelatedNextPageToken != null) {
            addSearchRelatedVideosDisposable(lastSearchVideoId!!, lastSearchRelatedNextPageToken)
        }
    }

    fun searchRelatedVideos(toVideoId: String) {
        lastSearchVideoId = toVideoId
        addSearchRelatedVideosDisposable(toVideoId, null)
    }

    private fun addSearchRelatedVideosDisposable(videoId: String, pageToken: String?) {
        addDisposable(searchRelatedVideos.execute(videoId, pageToken)
                .subscribe({
                    val (newNextPageToken, videos) = it
                    lastSearchRelatedNextPageToken = newNextPageToken
                    viewState.videos.addAll(videos.map(VideoEntityMapper::mapFrom))
                    getChannelThumbnails(videos, onSuccess = {
                        it.forEachIndexed { index, url -> viewState.videos.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                    })
                }, this::onError))
    }

    fun addVideoToPlaylist(video: Video, videoPlaylist: VideoPlaylist, onSuccess: () -> Unit) {
        addDisposable(addVideoToPlaylist.execute(playlistEntity = VideoPlaylistEntityMapper.mapBack(videoPlaylist),
                videoEntity = VideoEntityMapper.mapBack(video))
                .subscribe({ onSuccess() }, this::onError))
    }

    fun getFavouriteVideoPlaylists() {
        addDisposable(getFavouriteVideoPlaylists.execute().subscribe({
            viewState.favouriteVideoPlaylists.clear()
            viewState.favouriteVideoPlaylists.addAll(it.map(VideoPlaylistEntityMapper::mapFrom))
        }, this::onError))
    }

    fun addVideoPlaylistWithVideo(playlist: VideoPlaylist, video: Video, onSuccess: () -> Unit) {
        addDisposable(insertVideoPlaylist.execute(VideoPlaylistEntityMapper.mapBack(playlist))
                .subscribe({ playlistId ->
                    addVideoToPlaylist(video, VideoPlaylist(playlistId, playlist.name), onSuccess)
                }, this::onError))
    }
}