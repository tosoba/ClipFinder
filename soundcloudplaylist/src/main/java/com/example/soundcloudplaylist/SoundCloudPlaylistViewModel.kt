package com.example.soundcloudplaylist

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.playlist.PlaylistViewState
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.mapper.soundcloud.ui
import com.example.core.android.model.Loading
import com.example.core.android.model.PagedDataList
import com.example.core.android.model.soundcloud.BaseSoundCloudPlaylist
import com.example.core.android.model.soundcloud.SoundCloudPlaylist
import com.example.core.android.model.soundcloud.SoundCloudSystemPlaylist
import com.example.core.android.model.soundcloud.SoundCloudTrack
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.usecase.soundcloud.GetTracks
import com.example.there.domain.usecase.soundcloud.GetTracksFromPlaylist
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber

class SoundCloudPlaylistViewModel(
    initialState: PlaylistViewState<BaseSoundCloudPlaylist, SoundCloudTrack>,
    private val getTracksFromPlaylist: GetTracksFromPlaylist,
    private val getTracks: GetTracks
) : MvRxViewModel<PlaylistViewState<BaseSoundCloudPlaylist, SoundCloudTrack>>(initialState) {

    init {
        loadData()
    }

    fun loadData() = withState { state ->
        when (val playlist = state.playlist) {
            is SoundCloudPlaylist -> loadTracksFromPlaylist(playlist.id)
            is SoundCloudSystemPlaylist -> loadTracksWithIds(playlist.trackIds.map(Int::toString))
        }
    }

    private fun loadTracksWithIds(ids: List<String>) = withState { state ->
        if (state.tracks.status is Loading) return@withState

        setState { copy(tracks = state.tracks.copyWithLoadingInProgress) }
        //TODO: SoundCloud api needs to be reworked to return Resource objects
        getTracks(ids, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { setState { copy(tracks = PagedDataList(it.map(SoundCloudTrackEntity::ui))) } },
                Timber::e
            )
    }

    private fun loadTracksFromPlaylist(id: String) = withState { state ->
        if (state.tracks.status is Loading) return@withState

        getTracksFromPlaylist(id, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { setState { copy(tracks = PagedDataList(it.map(SoundCloudTrackEntity::ui))) } },
                Timber::e
            )
    }

    companion object : MvRxViewModelFactory<SoundCloudPlaylistViewModel, PlaylistViewState<BaseSoundCloudPlaylist, SoundCloudTrack>> {
        override fun create(
            viewModelContext: ViewModelContext, state: PlaylistViewState<BaseSoundCloudPlaylist, SoundCloudTrack>
        ): SoundCloudPlaylistViewModel {
            val getTracksFromPlaylist: GetTracksFromPlaylist by viewModelContext.activity.inject()
            val getTracks: GetTracks by viewModelContext.activity.inject()
            return SoundCloudPlaylistViewModel(state, getTracksFromPlaylist, getTracks)
        }
    }
}
