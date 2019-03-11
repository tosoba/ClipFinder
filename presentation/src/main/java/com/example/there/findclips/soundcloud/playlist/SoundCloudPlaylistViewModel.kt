package com.example.there.findclips.soundcloud.playlist

import com.example.there.domain.usecase.soundcloud.GetTracks
import com.example.there.domain.usecase.soundcloud.GetTracksFromPlaylist
import com.example.there.findclips.base.vm.BaseViewModel
import javax.inject.Inject

class SoundCloudPlaylistViewModel @Inject constructor(
        private val getTracksFromPlaylist: GetTracksFromPlaylist,
        private val getTracks: GetTracks
) : BaseViewModel() {

    fun loadTracksWithIds(ids: List<String>) {
        getTracks.execute(ids).subscribeAndDisposeOnCleared({
            it
        }, {
            it
        })
    }

    fun loadTracksFromPlaylist(id: String) {
        getTracksFromPlaylist.execute(id)
                .subscribeAndDisposeOnCleared({
                    it
                }, {
                    it
                })
    }
}