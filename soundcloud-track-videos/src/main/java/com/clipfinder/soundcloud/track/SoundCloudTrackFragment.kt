package com.clipfinder.soundcloud.track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.core.android.model.soundcloud.SoundCloudTrack
import com.clipfinder.core.android.model.soundcloud.clickableListItem
import com.clipfinder.core.android.util.ext.newMvRxFragmentWith
import com.clipfinder.core.android.util.ext.parentFragmentViewModel
import com.clipfinder.core.android.view.epoxy.loadableCollectionController
import com.clipfinder.soundcloud.R
import com.clipfinder.soundcloud.videos.SoundCloudTrackVideosViewModel
import kotlinx.android.synthetic.main.fragment_sound_cloud_track.view.*

class SoundCloudTrackFragment : BaseMvRxFragment() {
    private val argTrack: SoundCloudTrack by args()
    private val viewModel: SoundCloudTrackViewModel by fragmentViewModel()
    private val parentViewModel: SoundCloudTrackVideosViewModel by parentFragmentViewModel()

    private val epoxyController: TypedEpoxyController<SoundCloudTrackViewState> by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        loadableCollectionController(
            SoundCloudTrackViewState::similarTracks,
            headerText = "Similar tracks",
            reloadClicked = { track?.id?.let(viewModel::loadSimilarTracks) ?: Unit },
            clearFailure = viewModel::clearTracksError
        ) { track -> track.clickableListItem { parentViewModel.updateTrack(track) } }
    }

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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_sound_cloud_track, container, false).apply {
            this.sound_cloud_track_recycler_view?.setController(epoxyController)
        }

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    companion object {
        fun new(track: SoundCloudTrack): SoundCloudTrackFragment = newMvRxFragmentWith(track)
    }
}
