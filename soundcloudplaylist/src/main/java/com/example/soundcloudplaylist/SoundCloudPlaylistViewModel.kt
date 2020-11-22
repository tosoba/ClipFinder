package com.example.soundcloudplaylist

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.mapper.soundcloud.ui
import com.example.core.android.model.LoadingInProgress
import com.example.core.android.model.Ready
import com.example.core.android.model.soundcloud.SoundCloudPlaylist
import com.example.core.android.model.soundcloud.SoundCloudSystemPlaylist
import com.example.there.domain.entity.soundcloud.SoundCloudTrackEntity
import com.example.there.domain.usecase.soundcloud.GetTracks
import com.example.there.domain.usecase.soundcloud.GetTracksFromPlaylist
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import timber.log.Timber

class SoundCloudPlaylistViewModel(
    initialState: SoundCloudPlaylistState,
    private val getTracksFromPlaylist: GetTracksFromPlaylist,
    private val getTracks: GetTracks
) : MvRxViewModel<SoundCloudPlaylistState>(initialState) {

    init {
        loadData()
    }

    fun loadData() = withState { state ->
        when (val playlist = state.playlist) {
            is SoundCloudPlaylist -> loadTracksFromPlaylist(playlist.id)
            is SoundCloudSystemPlaylist -> loadTracksWithIds(playlist.trackIds.map(Int::toString))
        }
    }

    fun clearTracksError() = clearErrorIn(SoundCloudPlaylistState::tracks) { copy(tracks = it) }

    private fun loadTracksWithIds(ids: List<String>) = withState { state ->
        if (state.tracks is LoadingInProgress) return@withState

        setState { copy(tracks = state.tracks.copyWithLoadingInProgress) }
        //TODO: SoundCloud api needs to be reworked to return Resource objects
        getTracks(ids, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { setState { copy(tracks = Ready(it.map(SoundCloudTrackEntity::ui))) } },
                Timber::e
            )
    }

    private fun loadTracksFromPlaylist(id: String) = withState { state ->
        if (state.tracks is LoadingInProgress) return@withState

        getTracksFromPlaylist(id, applySchedulers = false)
            .subscribeOn(Schedulers.io())
            .subscribe(
                { setState { copy(tracks = Ready(it.map(SoundCloudTrackEntity::ui))) } },
                Timber::e
            )
    }

    companion object : MvRxViewModelFactory<SoundCloudPlaylistViewModel, SoundCloudPlaylistState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SoundCloudPlaylistState
        ): SoundCloudPlaylistViewModel = SoundCloudPlaylistViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get()
        )
    }
}
