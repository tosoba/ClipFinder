package com.example.main

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.clipfinder.core.ext.map
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.model.Paged
import com.clipfinder.core.model.Resource
import com.clipfinder.core.spotify.usecase.GetCurrentUser
import com.clipfinder.core.spotify.usecase.GetSimilarTracks
import com.example.core.android.base.viewmodel.MvRxViewModel
import com.example.core.android.model.Empty
import com.example.core.android.model.PagedList
import com.example.core.android.spotify.model.Track
import com.example.core.android.spotify.model.User
import com.example.core.android.util.ext.offset
import io.reactivex.Single
import org.koin.android.ext.android.get

class MainViewModel(
    initialState: MainState,
    private val getSimilarTracks: GetSimilarTracks,
    private val getCurrentUser: GetCurrentUser
) : MvRxViewModel<MainState>(initialState) {
    fun loadSimilarTracks(trackId: String) = loadPaged(MainState::similarTracks, getSimilarTracks::intoState, trackId, ::PagedList) { copy(similarTracks = it) }
    fun loadCurrentUser() = load(MainState::user, getCurrentUser::intoState) { copy(user = it) }
    fun setMainContent(mainContent: MainContent) = setState { copy(mainContent = mainContent) }
    fun setPlayerState(playerState: PlayerState) = setState { copy(playerState = playerState) }
    fun onLoggedOut() = setState { copy(user = Empty, isLoggedIn = false) }
    fun onLoggedIn() = setState { copy(isLoggedIn = true) }

    companion object : MvRxViewModelFactory<MainViewModel, MainState> {
        override fun create(
            viewModelContext: ViewModelContext, state: MainState
        ): MainViewModel = MainViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get()
        )
    }
}

private fun GetCurrentUser.intoState(
    state: MainState
): Single<Resource<User>> = this(applySchedulers = false)
    .mapData {
        User(
            it.displayName ?: "Unknown user",
            it.images?.firstOrNull()?.url
                ?: "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"
        )
    }

private fun GetSimilarTracks.intoState(
    state: MainState, trackId: String
): Single<Resource<Paged<List<Track>>>> = this(
    applySchedulers = false,
    args = GetSimilarTracks.Args(trackId, state.similarTracks.offset)
).mapData { tracks -> tracks.map(::Track) }
