package com.example.there.findclips.main

import android.util.Log
import com.example.there.domain.usecase.spotify.*
import com.example.there.domain.usecase.videos.DeleteAllVideoSearchData
import com.example.there.domain.usecase.videos.GetChannelsThumbnailUrls
import com.example.there.domain.usecase.videos.SearchRelatedVideos
import com.example.there.findclips.base.vm.BaseVideosViewModel
import com.example.there.findclips.model.entity.*
import com.example.there.findclips.model.mapper.AlbumEntityMapper
import com.example.there.findclips.model.mapper.PlaylistEntityMapper
import com.example.there.findclips.model.mapper.TrackEntityMapper
import com.example.there.findclips.model.mapper.VideoEntityMapper
import com.example.there.findclips.view.list.item.VideoItemView
import javax.inject.Inject


class MainViewModel @Inject constructor(
        getChannelsThumbnailUrls: GetChannelsThumbnailUrls,
        private val searchRelatedVideos: SearchRelatedVideos,
        private val getSimilarTracks: GetSimilarTracks,
        private val getCurrentUser: GetCurrentUser,
        private val insertTrack: InsertTrack,
        private val insertAlbum: InsertAlbum,
        private val deleteTrack: DeleteTrack,
        private val deleteAlbum: DeleteAlbum,
        private val insertSpotifyPlaylist: InsertSpotifyPlaylist,
        private val deleteSpotifyPlaylist: DeleteSpotifyPlaylist,
        private val isTrackSaved: IsTrackSaved,
        private val isSpotifyPlaylistSaved: IsSpotifyPlaylistSaved,
        private val isAlbumSaved: IsAlbumSaved,
        private val deleteAllVideoSearchData: DeleteAllVideoSearchData
) : BaseVideosViewModel(getChannelsThumbnailUrls) {

    val viewState = MainViewState()

    val drawerViewState = DrawerHeaderViewState()

    private var lastSearchVideo: Video? = null

    fun getSimilarTracks(trackId: String) {
        addDisposable(getSimilarTracks.execute(trackId)
                .subscribe({ viewState.similarTracks.value = it.map(TrackEntityMapper::mapFrom) }, ::onError))
    }

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


    fun getCurrentUser() = addDisposable(getCurrentUser.execute()
            .subscribe({ drawerViewState.user.set(User(it.name, it.iconUrl)) }, ::onError))


    fun updateTrackFavouriteState(track: Track) = addDisposable(isTrackSaved.execute(TrackEntityMapper.mapBack(track))
            .subscribe({ viewState.itemFavouriteState.set(it) }, {}))

    fun updatePlaylistFavouriteState(playlist: Playlist) = addDisposable(isSpotifyPlaylistSaved.execute(PlaylistEntityMapper.mapBack(playlist))
            .subscribe({ viewState.itemFavouriteState.set(it) }, {}))

    fun updateAlbumFavouriteState(album: Album) = addDisposable(isAlbumSaved.execute(AlbumEntityMapper.mapBack(album))
            .subscribe({ viewState.itemFavouriteState.set(it) }, {}))


    fun deleteAllVideoSearchData() = addDisposable(deleteAllVideoSearchData.execute()
            .subscribe({}, { Log.e("ERROR", "Delete all video search data error.") }))

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


}