package com.example.there.findclips.main

import android.util.Log
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
        addVideoToPlaylist.execute(AddVideoToPlaylist.Input(
                playlistEntity = VideoPlaylistEntityMapper.mapBack(videoPlaylist),
                videoEntity = VideoEntityMapper.mapBack(video))
        ).subscribeAndDisposeOnCleared(onSuccess, ::onError)
    }

    fun getFavouriteVideoPlaylists() {
        getFavouriteVideoPlaylists.execute()
                .subscribeAndDisposeOnCleared({
                    viewState.favouriteVideoPlaylists.clear()
                    viewState.favouriteVideoPlaylists.addAll(it.map(VideoPlaylistEntityMapper::mapFrom))
                }, ::onError)
    }

    fun addVideoPlaylistWithVideo(playlist: VideoPlaylist, video: Video, onSuccess: () -> Unit) {
        insertVideoPlaylist.execute(VideoPlaylistEntityMapper.mapBack(playlist))
                .subscribeAndDisposeOnCleared({ playlistId ->
                    addVideoToPlaylist(video, VideoPlaylist(playlistId, playlist.name), onSuccess)
                }, ::onError)
    }

    fun getSimilarTracks(trackId: String) {
        getSimilarTracks.execute(trackId)
                .subscribe({ viewState.similarTracks.value = it.map(TrackEntityMapper::mapFrom) }, ::onError)
                .disposeOnCleared()
    }

    fun getCurrentUser() {
        getCurrentUser.execute()
                .subscribeAndDisposeOnCleared({ drawerViewState.user.set(User(it.name, it.iconUrl)) }, ::onError)
    }

    fun updateTrackFavouriteState(track: Track) {
        isTrackSaved.execute(TrackEntityMapper.mapBack(track))
                .subscribeAndDisposeOnCleared(viewState.itemFavouriteState::set)
    }

    fun updatePlaylistFavouriteState(playlist: Playlist) {
        isSpotifyPlaylistSaved.execute(PlaylistEntityMapper.mapBack(playlist))
                .subscribeAndDisposeOnCleared(viewState.itemFavouriteState::set)
    }

    fun updateAlbumFavouriteState(album: Album) {
        isAlbumSaved.execute(AlbumEntityMapper.mapBack(album))
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
            PlaylistEntityMapper.mapBack(playlist),
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
            TrackEntityMapper.mapBack(track),
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
            AlbumEntityMapper.mapBack(album),
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