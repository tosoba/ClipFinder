package com.clipfinder.soundcloud.playlist

import android.annotation.SuppressLint
import android.content.Context
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.android.model.soundcloud.SoundCloudPlaylist
import com.clipfinder.core.android.model.soundcloud.SoundCloudSystemPlaylist
import com.clipfinder.core.android.model.soundcloud.SoundCloudTrack
import com.clipfinder.core.android.util.ext.retryLoadCollectionOnConnected
import com.clipfinder.core.model.LoadingInProgress
import com.clipfinder.core.model.Ready
import com.clipfinder.core.model.invoke
import com.clipfinder.core.soundcloud.usecase.GetTracks
import com.clipfinder.core.soundcloud.usecase.GetTracksFromPlaylist
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import timber.log.Timber

class SoundCloudPlaylistViewModel(
    initialState: SoundCloudPlaylistState,
    private val getTracksFromPlaylist: GetTracksFromPlaylist,
    private val getTracks: GetTracks,
    context: Context
) : MvRxViewModel<SoundCloudPlaylistState>(initialState) {
    init {
        loadData()
        handleConnectivityChanges(context)
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
        getTracks(ids)
            .subscribeOn(Schedulers.io())
            .subscribe({ setState { copy(tracks = Ready(it.map(::SoundCloudTrack))) } }, Timber::e)
    }

    private fun loadTracksFromPlaylist(id: String) = withState { state ->
        if (state.tracks is LoadingInProgress) return@withState
        getTracksFromPlaylist(id)
            .subscribeOn(Schedulers.io())
            .subscribe({ setState { copy(tracks = Ready(it.map(::SoundCloudTrack))) } }, Timber::e)
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, tracks) ->
            if (tracks.retryLoadCollectionOnConnected) loadData()
        }
    }

    companion object : MvRxViewModelFactory<SoundCloudPlaylistViewModel, SoundCloudPlaylistState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SoundCloudPlaylistState
        ): SoundCloudPlaylistViewModel =
            SoundCloudPlaylistViewModel(
                state,
                viewModelContext.activity.get(),
                viewModelContext.activity.get(),
                viewModelContext.app()
            )
    }
}
