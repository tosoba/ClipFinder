package com.example.there.findclips.main

import com.example.there.domain.entity.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.*
import com.example.there.domain.usecase.videos.*
import com.example.there.findclips.base.vm.BaseVideosViewModel
import com.example.there.findclips.model.entity.*
import com.example.there.findclips.model.mapper.*
import javax.inject.Inject

class MainViewModel @Inject constructor(
        private val searchRelatedVideos: SearchRelatedVideos,
        getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
        private val insertVideoPlaylist: InsertVideoPlaylist,
        private val addVideoToPlaylist: AddVideoToPlaylist,
        private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists,
        private val getSimilarTracks: GetSimilarTracks,
        private val getCurrentUser: GetCurrentUser,
        private val insertTrack: InsertTrack,
        private val insertSpotifyPlaylist: InsertSpotifyPlaylist,
        private val insertAlbum: InsertAlbum
) : BaseVideosViewModel(getChannelsThumbnailUrls) {

    val viewState = MainViewState()

    val drawerViewState = DrawerHeaderViewState()

    private var lastSearchVideo: Video? = null

    fun searchRelatedVideosWithToLastId() {
        lastSearchVideo?.let {
            addSearchRelatedVideosDisposable(it, true)
        }
    }

    fun searchRelatedVideos(toVideo: Video, onFinally: (() -> Unit)? = null) {
        lastSearchVideo = toVideo
        viewState.videos.clear()
        addSearchRelatedVideosDisposable(toVideo, false, onFinally)
    }

    private fun addSearchRelatedVideosDisposable(video: Video, loadMore: Boolean, onFinally: (() -> Unit)? = null) {
        if (loadMore) viewState.loadingMoreVideosInProgress.set(true)
        else viewState.initialVideosLoadingInProgress.set(true)

        addDisposable(searchRelatedVideos.execute(video.id, loadMore)
                .doFinally {
                    if (loadMore) viewState.loadingMoreVideosInProgress.set(false)
                    else viewState.initialVideosLoadingInProgress.set(false)
                    onFinally?.invoke()
                }
                .subscribe({ videos ->
                    val mapped = videos.map(VideoEntityMapper::mapFrom)
                    viewState.videos.addAll(mapped)
                    getChannelThumbnails(videos, onSuccess = {
                        it.forEach { (index, url) -> mapped.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                    })
                }, ::onError))
    }

    fun addVideoToPlaylist(video: Video, videoPlaylist: VideoPlaylist, onSuccess: () -> Unit) {
        addDisposable(addVideoToPlaylist.execute(playlistEntity = VideoPlaylistEntityMapper.mapBack(videoPlaylist),
                videoEntity = VideoEntityMapper.mapBack(video))
                .subscribe({ onSuccess() }, ::onError))
    }

    fun getFavouriteVideoPlaylists() {
        addDisposable(getFavouriteVideoPlaylists.execute().subscribe({
            viewState.favouriteVideoPlaylists.clear()
            viewState.favouriteVideoPlaylists.addAll(it.map(VideoPlaylistEntityMapper::mapFrom))
        }, ::onError))
    }

    fun addVideoPlaylistWithVideo(playlist: VideoPlaylist, video: Video, onSuccess: () -> Unit) {
        addDisposable(insertVideoPlaylist.execute(VideoPlaylistEntityMapper.mapBack(playlist))
                .subscribe({ playlistId ->
                    addVideoToPlaylist(video, VideoPlaylist(playlistId, playlist.name), onSuccess)
                }, ::onError))
    }

    fun getSimilarTracks(accessTokenEntity: AccessTokenEntity, trackId: String) {
        addDisposable(getSimilarTracks.execute(accessTokenEntity, trackId)
                .subscribe({ viewState.similarTracks.value = it.map(TrackEntityMapper::mapFrom) }, ::onError))
    }

    fun getCurrentUser(accessToken: AccessTokenEntity) = addDisposable(getCurrentUser
            .execute(accessToken)
            .subscribe({ drawerViewState.user.set(User(it.name, it.iconUrl)) }, ::onError))

    fun addTrackToFavourites(
            track: Track
    ) = addDisposable(insertTrack.execute(TrackEntityMapper.mapBack(track)).subscribe())

    fun addPlaylistToFavourites(
            playlist: Playlist
    ) = addDisposable(insertSpotifyPlaylist.execute(PlaylistEntityMapper.mapBack(playlist)).subscribe())

    fun addAlbumToFavourites(
            album: Album
    ) = addDisposable(insertAlbum.execute(AlbumEntityMapper.mapBack(album)).subscribe())
}