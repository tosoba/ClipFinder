package com.example.coreandroid.base.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coreandroid.base.vm.BaseViewModel

open class BasePlaylistViewModel<Track> : BaseViewModel() {
    val viewState: PlaylistViewState = PlaylistViewState()

    protected val mutableTracks: MutableLiveData<List<Track>> = MutableLiveData()
    val tracks: LiveData<List<Track>>
        get() = mutableTracks
}