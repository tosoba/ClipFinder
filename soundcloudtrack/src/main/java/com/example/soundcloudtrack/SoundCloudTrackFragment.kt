package com.example.soundcloudtrack

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.*
import com.example.coreandroid.base.handler.OnTrackChangeListener
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.coreandroid.model.soundcloud.clickableListItem
import com.example.coreandroid.view.epoxy.itemListController
import kotlinx.android.synthetic.main.fragment_sound_cloud_track.view.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class SoundCloudTrackFragment : BaseMvRxFragment() {

    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))

    private val viewModel: SoundCloudTrackViewModel by fragmentViewModel()

    //TODO: connectivityComponent

    private val epoxyController by lazy {
        itemListController(builder, differ, viewModel,
                SoundCloudTrackViewState::similarTracks, "Similar tracks",
                reloadClicked = { track?.id?.let { viewModel.loadSimilarTracks(it) } }
        ) {
            it.clickableListItem {
                (parentFragment as? OnTrackChangeListener<SoundCloudTrack>)?.onTrackChanged(newTrack = it)
            }
        }
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

    override fun invalidate() = withState(viewModel) { state -> epoxyController.setData(state) }

    companion object {
        fun newInstance(track: SoundCloudTrack): SoundCloudTrackFragment = SoundCloudTrackFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, track) }
        }
    }
}
