package com.example.main

import android.util.Log
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.mapper.spotify.domain
import com.example.coreandroid.mapper.spotify.ui
import com.example.coreandroid.mapper.videos.domain
import com.example.coreandroid.mapper.videos.ui
import com.example.coreandroid.model.spotify.Album
import com.example.coreandroid.model.spotify.Playlist
import com.example.coreandroid.model.spotify.Track
import com.example.coreandroid.model.spotify.User
import com.example.coreandroid.model.videos.Video
import com.example.coreandroid.model.videos.VideoPlaylist
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.usecase.base.CompletableUseCaseWithArgs
import com.example.there.domain.usecase.base.SingleUseCaseWithArgs
import com.example.there.domain.usecase.spotify.*
import com.example.there.domain.usecase.videos.AddVideoToPlaylist
import com.example.there.domain.usecase.videos.DeleteAllVideoSearchData
import com.example.there.domain.usecase.videos.GetFavouriteVideoPlaylists
import com.example.there.domain.usecase.videos.InsertVideoPlaylist
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
        addVideoToPlaylist(AddVideoToPlaylist.Args(
                playlistEntity = videoPlaylist.domain,
                videoEntity = video.domain)
        ).subscribeAndDisposeOnCleared(onSuccess, ::onError)
    }

    fun loadFavouriteVideoPlaylists() {
        getFavouriteVideoPlaylists()
                .subscribeAndDisposeOnCleared({
                    viewState.favouriteVideoPlaylists.clear()
                    viewState.favouriteVideoPlaylists.addAll(it.map(VideoPlaylistEntity::ui))
                }, ::onError)
    }

    fun addVideoPlaylistWithVideo(playlist: VideoPlaylist, video: Video, onSuccess: () -> Unit) {
        insertVideoPlaylist(playlist.domain)
                .subscribeAndDisposeOnCleared({ playlistId ->
                    addVideoToPlaylist(video, VideoPlaylist(playlistId, playlist.name), onSuccess)
                }, ::onError)
    }

    fun loadSimilarTracks(trackId: String) {
        getSimilarTracks(trackId)
                .subscribe({ viewState.similarTracks.value = it.map(TrackEntity::ui) }, ::onError)
                .disposeOnCleared()
    }

    fun loadCurrentUser() {
        getCurrentUser()
                .subscribeAndDisposeOnCleared({ drawerViewState.user.set(User(it.name, it.iconUrl)) }, ::onError)
    }

    fun updateTrackFavouriteState(track: Track) {
        isTrackSaved(track.domain)
                .subscribeAndDisposeOnCleared(viewState.itemFavouriteState::set)
    }

    fun updatePlaylistFavouriteState(playlist: Playlist) {
        isSpotifyPlaylistSaved(playlist.domain)
                .subscribeAndDisposeOnCleared(viewState.itemFavouriteState::set)
    }

    fun updateAlbumFavouriteState(album: Album) {
        isAlbumSaved(album.domain)
                .subscribeAndDisposeOnCleared(viewState.itemFavouriteState::set)
    }

    fun clearAllVideoSearchData() {
        deleteAllVideoSearchData().subscribeAndDisposeOnCleared()
    }

    fun togglePlaylistFavouriteState(
            playlist: Playlist,
            onPlaylistAdded: () -> Unit,
            onPlaylistDeleted: () -> Unit
    ) = toggleItemFavouriteState(
            playlist.domain,
            isSpotifyPlaylistSaved,
            insertSpotifyPlaylist,
            deleteSpotifyPlaylist,
            onPlaylistAdded,
            onPlaylistDeleted
    )

    fun toggleTrackFavouriteState(
            track: Track,
            onTrackAdded: () -> Unit,
            onTrackDeleted: () -> Unit
    ) = toggleItemFavouriteState(
            track.domain,
            isTrackSaved,
            insertTrack,
            deleteTrack,
            onTrackAdded,
            onTrackDeleted
    )

    fun toggleAlbumFavouriteState(
            album: Album,
            onAlbumAdded: () -> Unit,
            onAlbumDeleted: () -> Unit
    ) = toggleItemFavouriteState(
            album.domain,
            isAlbumSaved,
            insertAlbum,
            deleteAlbum,
            onAlbumAdded,
            onAlbumDeleted
    )


    sealed class ItemInsertDeleteResult {
        object Inserted
        object Deleted
    }

    private fun <T> toggleItemFavouriteState(
            item: T,
            isSavedUseCase: SingleUseCaseWithArgs<T, Boolean>,
            insertUseCase: CompletableUseCaseWithArgs<T>,
            deleteUseCase: CompletableUseCaseWithArgs<T>,
            onInserted: () -> Unit,
            onDeleted: () -> Unit
    ) {
        isSavedUseCase(item)
                .flatMap { isSaved ->
                    if (isSaved) deleteUseCase(item)
                            .toSingle { ItemInsertDeleteResult.Deleted }
                    else insertUseCase(item)
                            .toSingle { ItemInsertDeleteResult.Inserted }
                }
                .subscribeAndDisposeOnCleared({
                    when (it) {
                        ItemInsertDeleteResult.Inserted -> {
                            viewState.itemFavouriteState.set(true)
                            onInserted()
                        }
                        ItemInsertDeleteResult.Deleted -> {
                            viewState.itemFavouriteState.set(false)
                            onDeleted()
                        }
                    }
                }, { Log.e("ERROR", "Insert/Delete item error") })
    }

}