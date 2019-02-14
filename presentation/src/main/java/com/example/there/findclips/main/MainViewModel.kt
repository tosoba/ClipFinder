package com.example.there.findclips.main

import android.graphics.Bitmap
import android.util.Log
import com.example.there.domain.usecase.spotify.*
import com.example.there.domain.usecase.videos.*
import com.example.there.findclips.base.vm.BaseVideosViewModel
import com.example.there.findclips.model.entity.*
import com.example.there.findclips.model.mapper.*
import com.example.there.findclips.util.ext.getBitmapSingle
import com.example.there.findclips.view.list.item.VideoItemView
import com.squareup.picasso.Picasso
import javax.inject.Inject


class MainViewModel @Inject constructor(
        getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
        private val searchRelatedVideos: SearchRelatedVideos,
        private val insertVideoPlaylist: InsertVideoPlaylist,
        private val addVideoToPlaylist: AddVideoToPlaylist,
        private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists,
        private val getSimilarTracks: GetSimilarTracks,
        private val getCurrentUser: GetCurrentUser,
        private val insertTrack: InsertTrack,
        private val insertSpotifyPlaylist: InsertSpotifyPlaylist,
        private val insertAlbum: InsertAlbum,
        private val deleteTrack: DeleteTrack,
        private val deleteSpotifyPlaylist: DeleteSpotifyPlaylist,
        private val deleteAlbum: DeleteAlbum,
        private val isTrackSaved: IsTrackSaved,
        private val isSpotifyPlaylistSaved: IsSpotifyPlaylistSaved,
        private val isAlbumSaved: IsAlbumSaved,
        private val deleteAllVideoSearchData: DeleteAllVideoSearchData
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

        addDisposable(searchRelatedVideos.execute(SearchRelatedVideos.Input(video.id, loadMore))
                .doFinally {
                    if (loadMore) viewState.loadingMoreVideosInProgress.set(false)
                    else viewState.initialVideosLoadingInProgress.set(false)
                    onFinally?.invoke()
                }
                .subscribe({ videos ->
                    val mapped = videos.map(VideoEntityMapper::mapFrom)
                    viewState.videos.addAll(mapped.map { VideoItemView(it, null) })
                    getChannelThumbnails(videos, onSuccess = {
                        it.forEach { (index, url) -> mapped.getOrNull(index)?.channelThumbnailUrl?.set(url) }
                    })
                }, ::onError))
    }

    fun addVideoToPlaylist(video: Video, videoPlaylist: VideoPlaylist, onSuccess: () -> Unit) {
        addDisposable(addVideoToPlaylist.execute(AddVideoToPlaylist.Input(
                playlistEntity = VideoPlaylistEntityMapper.mapBack(videoPlaylist),
                videoEntity = VideoEntityMapper.mapBack(video))
        ).subscribe({ onSuccess() }, ::onError))
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

    fun getSimilarTracks(trackId: String) {
        addDisposable(getSimilarTracks.execute(trackId)
                .subscribe({ viewState.similarTracks.value = it.map(TrackEntityMapper::mapFrom) }, ::onError))
    }

    fun getCurrentUser() = addDisposable(getCurrentUser.execute()
            .subscribe({ drawerViewState.user.set(User(it.name, it.iconUrl)) }, ::onError))

    fun toggleTrackFavouriteState(
            track: Track,
            onTrackAdded: () -> Unit,
            onTrackDeleted: () -> Unit
    ) {
        val entity = TrackEntityMapper.mapBack(track)
        addDisposable(isTrackSaved.execute(entity)
                .subscribe({ isSaved ->
                    if (isSaved) {
                        addDisposable(deleteTrack.execute(entity)
                                .subscribe({
                                    viewState.itemFavouriteState.set(false)
                                    onTrackDeleted()
                                }, { Log.e("ERROR", "Delete error") }))
                    } else {
                        addDisposable(insertTrack.execute(entity)
                                .subscribe({
                                    viewState.itemFavouriteState.set(true)
                                    onTrackAdded()
                                }, { Log.e("ERROR", "Insert error") }))
                    }
                }, {}))
    }

    fun updateTrackFavouriteState(track: Track) = addDisposable(isTrackSaved.execute(TrackEntityMapper.mapBack(track))
            .subscribe({ viewState.itemFavouriteState.set(it) }, {}))

    fun updatePlaylistFavouriteState(playlist: Playlist) = addDisposable(isSpotifyPlaylistSaved.execute(PlaylistEntityMapper.mapBack(playlist))
            .subscribe({ viewState.itemFavouriteState.set(it) }, {}))

    fun updateAlbumFavouriteState(album: Album) = addDisposable(isAlbumSaved.execute(AlbumEntityMapper.mapBack(album))
            .subscribe({ viewState.itemFavouriteState.set(it) }, {}))

    fun togglePlaylistFavouriteState(
            playlist: Playlist,
            onPlaylistAdded: () -> Unit,
            onPlaylistDeleted: () -> Unit
    ) {
        val entity = PlaylistEntityMapper.mapBack(playlist)
        addDisposable(isSpotifyPlaylistSaved.execute(entity)
                .subscribe({ isSaved ->
                    if (isSaved) {
                        addDisposable(deleteSpotifyPlaylist.execute(entity)
                                .subscribe({
                                    viewState.itemFavouriteState.set(false)
                                    onPlaylistDeleted()
                                }, { Log.e("ERROR", "Delete error") }))
                    } else {
                        addDisposable(insertSpotifyPlaylist.execute(entity)
                                .subscribe({
                                    viewState.itemFavouriteState.set(true)
                                    onPlaylistAdded()
                                }, { Log.e("ERROR", "Insert error") }))
                    }
                }, {}))
    }

    fun toggleAlbumFavouriteState(
            album: Album,
            onAlbumAdded: () -> Unit,
            onAlbumDeleted: () -> Unit
    ) {
        val entity = AlbumEntityMapper.mapBack(album)
        addDisposable(isAlbumSaved.execute(entity)
                .subscribe({ isSaved ->
                    if (isSaved) {
                        addDisposable(deleteAlbum.execute(entity)
                                .subscribe({
                                    viewState.itemFavouriteState.set(false)
                                    onAlbumDeleted()
                                }, { Log.e("ERROR", "Delete error") }))
                    } else {
                        addDisposable(insertAlbum.execute(entity)
                                .subscribe({
                                    viewState.itemFavouriteState.set(true)
                                    onAlbumAdded()
                                }, { Log.e("ERROR", "Insert error") }))
                    }
                }, {}))
    }

    fun deleteAllVideoSearchData() = addDisposable(deleteAllVideoSearchData.execute()
            .subscribe({}, { Log.e("ERROR", "Delete all video search data error.") }))

    fun getBitmapSingle(
            picasso: Picasso,
            url: String,
            onSuccess: (Bitmap) -> Unit,
            onError: () -> Unit
    ) = addDisposable(picasso.getBitmapSingle(url, onSuccess, onError))
}