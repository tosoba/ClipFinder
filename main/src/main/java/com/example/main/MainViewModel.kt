package com.example.main

import com.clipfinder.core.spotify.usecase.GetCurrentUser
import com.clipfinder.core.spotify.usecase.GetSimilarTracks
import com.example.core.android.base.vm.BaseViewModel
import com.example.core.android.spotify.model.User

class MainViewModel(
    private val getSimilarTracks: GetSimilarTracks,
    private val getCurrentUser: GetCurrentUser
) : BaseViewModel() {
    val viewState = MainViewState()
    val drawerViewState = DrawerHeaderViewState()

    fun loadSimilarTracks(trackId: String) {
//        getSimilarTracks(trackId)
//            .takeSuccessOnly()
//            .subscribeAndDisposeOnCleared({ viewState.similarTracks.value = it.map(TrackEntity::ui) }, ::onError)
    }

    fun loadCurrentUser() {
        getCurrentUser()
            .takeSuccessOnly()
            .subscribeAndDisposeOnCleared({
                drawerViewState.user.set(
                    User(
                        it.displayName ?: "Unknown user",
                        it.images?.firstOrNull()?.url ?: DEFAULT_SPOTIFY_USER_IMAGE_URL
                    )
                )
            }, ::onError)
    }

    companion object {
        private const val DEFAULT_SPOTIFY_USER_IMAGE_URL = "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"
    }
}
