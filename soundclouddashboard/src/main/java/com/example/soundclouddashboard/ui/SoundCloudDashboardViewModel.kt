package com.example.soundclouddashboard.ui

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.example.core.android.base.vm.MvRxViewModel
import com.example.core.android.mapper.soundcloud.ui
import com.example.core.android.model.Loading
import com.example.soundclouddashboard.SoundCloudDashboardViewState
import com.example.soundclouddashboard.SoundCloudPlaylists
import com.example.there.domain.usecase.soundcloud.DiscoverSoundCloud
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

//TODO: make a BuildConfig that specifies debugMode
class SoundCloudDashboardViewModel(
    initialState: SoundCloudDashboardViewState,
    private val discoverSoundCloud: DiscoverSoundCloud
) : MvRxViewModel<SoundCloudDashboardViewState>(initialState) {

    init {
        loadPlaylists()
    }

    fun loadPlaylists() = withState { state ->
        if (state.playlists.status is Loading) return@withState

        discoverSoundCloud(applySchedulers = false)
            .map { discoverEntity ->
                SoundCloudPlaylists(
                    discoverEntity.playlists.map { it.ui }.sortedBy { it.name },
                    discoverEntity.systemPlaylists.map { it.ui }.sortedBy { it.name }
                )
            }
            .subscribeOn(Schedulers.io())
            .update(SoundCloudDashboardViewState::playlists) { copy(playlists = it) }
    }

    companion object : MvRxViewModelFactory<SoundCloudDashboardViewModel, SoundCloudDashboardViewState> {
        override fun create(
            viewModelContext: ViewModelContext, state: SoundCloudDashboardViewState
        ): SoundCloudDashboardViewModel {
            val discoverSoundCloud: DiscoverSoundCloud by viewModelContext.activity.inject()
            return SoundCloudDashboardViewModel(state, discoverSoundCloud)
        }
    }
}