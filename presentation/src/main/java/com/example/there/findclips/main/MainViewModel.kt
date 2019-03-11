package com.example.there.findclips.main

import android.util.Log
import com.example.there.domain.entity.spotify.TrackEntity
import com.example.there.domain.entity.videos.VideoPlaylistEntity
import com.example.there.domain.usecase.base.CompletableUseCaseWithInput
import com.example.there.domain.usecase.base.SingleUseCaseWithInput
import com.example.there.domain.usecase.spotify.*
import com.example.there.domain.usecase.videos.AddVideoToPlaylist
import com.example.there.domain.usecase.videos.DeleteAllVideoSearchData
import com.example.there.domain.usecase.videos.GetFavouriteVideoPlaylists
import com.example.there.domain.usecase.videos.InsertVideoPlaylist
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.model.entity.spotify.Album
import com.example.there.findclips.model.entity.spotify.Playlist
import com.example.there.findclips.model.entity.spotify.Track
import com.example.there.findclips.model.entity.spotify.User
import com.example.there.findclips.model.entity.videos.Video
import com.example.there.findclips.model.entity.videos.VideoPlaylist
import com.example.there.findclips.model.mapper.domain
import com.example.there.findclips.model.mapper.ui
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
        addVideoToPlaylist.execute(AddVideoToPlaylist.Input(
                playlistEntity = videoPlaylist.domain,
                videoEntity = video.domain)
        ).subscribeAndDisposeOnCleared(onSuccess, ::onError)
    }

    fun getFavouriteVideoPlaylists() {
        getFavouriteVideoPlaylists.execute()
                .subscribeAndDisposeOnCleared({
                    viewState.favouriteVideoPlaylists.clear()
                    viewState.favouriteVideoPlaylists.addAll(it.map(VideoPlaylistEntity::ui))
                }, ::onError)
    }

    fun addVideoPlaylistWithVideo(playlist: VideoPlaylist, video: Video, onSuccess: () -> Unit) {
        insertVideoPlaylist.execute(playlist.domain)
                .subscribeAndDisposeOnCleared({ playlistId ->
                    addVideoToPlaylist(video, VideoPlaylist(playlistId, playlist.name), onSuccess)
                }, ::onError)
    }

    fun getSimilarTracks(trackId: String) {
        getSimilarTracks.execute(trackId)
                .subscribe({ viewState.similarTracks.value = it.map(TrackEntity::ui) }, ::onError)
                .disposeOnCleared()
    }

    fun getCurrentUser() {
        getCurrentUser.execute()
                .subscribeAndDisposeOnCleared({ drawerViewState.user.set(User(it.name, it.iconUrl)) }, ::onError)
    }

    fun updateTrackFavouriteState(track: Track) {
        isTrackSaved.execute(track.domain)
                .subscribeAndDisposeOnCleared(viewState.itemFavouriteState::set)
    }

    fun updatePlaylistFavouriteState(playlist: Playlist) {
        isSpotifyPlaylistSaved.execute(playlist.domain)
                .subscribeAndDisposeOnCleared(viewState.itemFavouriteState::set)
    }

    fun updateAlbumFavouriteState(album: Album) {
        isAlbumSaved.execute(album.domain)
                .subscribeAndDisposeOnCleared(viewState.itemFavouriteState::set)
    }

    fun deleteAllVideoSearchData() {
        deleteAllVideoSearchData.execute()
                .subscribeAndDisposeOnCleared()
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
            isSavedUseCase: SingleUseCaseWithInput<T, Boolean>,
            insertUseCase: CompletableUseCaseWithInput<T>,
            deleteUseCase: CompletableUseCaseWithInput<T>,
            onInserted: () -> Unit,
            onDeleted: () -> Unit
    ) {
        isSavedUseCase.execute(item)
                .flatMap { isSaved ->
                    if (isSaved) deleteUseCase.execute(item)
                            .toSingle { ItemInsertDeleteResult.Deleted }
                    else insertUseCase.execute(item)
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