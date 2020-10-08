package com.example.soundcloudtrackvideos.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.android.model.soundcloud.SoundCloudTrack
import com.example.core.android.model.soundcloud.clickableListItem
import com.example.core.android.util.ext.newFragmentWithMvRxArg
import com.example.core.android.util.ext.parentFragmentViewModel
import com.example.core.android.view.epoxy.injectedItemListController
import com.example.soundcloudtrackvideos.R
import com.example.soundcloudtrackvideos.SoundCloudTrackVideosViewModel
import kotlinx.android.synthetic.main.fragment_sound_cloud_track.view.*

class SoundCloudTrackFragment : BaseMvRxFragment() {

    private val viewModel: SoundCloudTrackViewModel by fragmentViewModel()
    private val parentViewModel: SoundCloudTrackVideosViewModel by parentFragmentViewModel()

    //TODO: connectivityComponent

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        injectedItemListController(
            SoundCloudTrackViewState::similarTracks,
            "Similar tracks",
            reloadClicked = { track?.id?.let { viewModel.loadSimilarTracks(it) } }
        ) { it.clickableListItem { parentViewModel.updateTrack(it) } }
    }

    private val argTrack: SoundCloudTrack by args()

    var track: SoundCloudTrack? = null
        set(value) {
            if (field == value || value == null || value == argTrack) return
            field = value
            viewModel.loadSimilarTracks(value.id)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadSimilarTracks(argTrack.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_sound_cloud_track, container, false).apply {
        this.sound_cloud_track_recycler_view?.setController(epoxyController)
    }

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    companion object {
        fun new(track: SoundCloudTrack): SoundCloudTrackFragment = newFragmentWithMvRxArg(track)
    }
}
