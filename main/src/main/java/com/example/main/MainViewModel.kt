package com.example.main

import com.example.core.android.base.vm.BaseViewModel
import com.example.core.android.model.spotify.User
import com.example.there.domain.usecase.spotify.GetCurrentUser
import com.clipfinder.core.spotify.usecase.GetSimilarTracks

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
            .subscribeAndDisposeOnCleared({ drawerViewState.user.set(User(it.name, it.iconUrl)) }, ::onError)
    }
}
