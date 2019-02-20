package com.example.there.findclips.main

import android.util.Log
import com.example.there.domain.usecase.spotify.*
import com.example.there.domain.usecase.videos.AddVideoToPlaylist
import com.example.there.domain.usecase.videos.DeleteAllVideoSearchData
import com.example.there.domain.usecase.videos.GetFavouriteVideoPlaylists
import com.example.there.domain.usecase.videos.InsertVideoPlaylist
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.*
import com.example.there.findclips.model.mapper.*
import javax.inject.Inject


class MainViewModel @Inject constructor(
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
        private val deleteAllVideoSearchData: DeleteAllVideoSearchData,
        private val insertVideoPlaylist: InsertVideoPlaylist,
        private val addVideoToPlaylist: AddVideoToPlaylist,
        private val getFavouriteVideoPlaylists: GetFavouriteVideoPlaylists
) : BaseViewModel() {

    val viewState = MainViewState()

    val drawerViewState = DrawerHeaderViewState()

    fun addVideoToPlaylist(video: Video, videoPlaylist: VideoPlaylist, onSuccess: () -> Unit) {
        addDisposable(addVideoToPlaylist.execute(AddVideoToPlaylist.Input(
                playlistEntity = VideoPlaylistEntityMapper.mapBack(videoPlaylist),
                videoEntity = VideoEntityMapper.mapBack(video))
        ).subscribe(onSuccess, ::onError))
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

    fun updateTrackFavouriteState(track: Track) = addDisposable(isTrackSaved.execute(TrackEntityMapper.mapBack(track))
            .subscribe { isSaved -> viewState.itemFavouriteState.set(isSaved) }
    )

    fun updatePlaylistFavouriteState(playlist: Playlist) = addDisposable(isSpotifyPlaylistSaved.execute(PlaylistEntityMapper.mapBack(playlist))
            .subscribe { isSaved -> viewState.itemFavouriteState.set(isSaved) })

    fun updateAlbumFavouriteState(album: Album) = addDisposable(isAlbumSaved.execute(AlbumEntityMapper.mapBack(album))
            .subscribe { isSaved -> viewState.itemFavouriteState.set(isSaved) })

    fun deleteAllVideoSearchData() = addDisposable(deleteAllVideoSearchData.execute()
            .subscribe())

    fun togglePlaylistFavouriteState(
            playlist: Playlist,
            onPlaylistAdded: () -> Unit,
            onPlaylistDeleted: () -> Unit
    ) {
        val entity = PlaylistEntityMapper.mapBack(playlist)
        addDisposable(isSpotifyPlaylistSaved.execute(entity)
                .subscribe { isSaved ->
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
                })
    }

    fun toggleTrackFavouriteState(
            track: Track,
            onTrackAdded: () -> Unit,
            onTrackDeleted: () -> Unit
    ) {
        val entity = TrackEntityMapper.mapBack(track)
        addDisposable(isTrackSaved.execute(entity)
                .subscribe { isSaved ->
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
                })
    }

    fun toggleAlbumFavouriteState(
            album: Album,
            onAlbumAdded: () -> Unit,
            onAlbumDeleted: () -> Unit
    ) {
        val entity = AlbumEntityMapper.mapBack(album)
        addDisposable(isAlbumSaved.execute(entity)
                .subscribe { isSaved ->
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
                })
    }
}