package com.example.soundcloudplaylist

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.mvrx.*
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.playlist.PlaylistView
import com.example.core.android.base.playlist.PlaylistViewState
import com.example.core.android.lifecycle.ConnectivityComponent
import com.example.core.android.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.core.android.model.soundcloud.BaseSoundCloudPlaylist
import com.example.core.android.model.soundcloud.SoundCloudTrack
import com.example.core.android.model.soundcloud.clickableListItem
import com.example.core.android.util.ext.*
import com.example.core.android.view.epoxy.injectedItemListController
import com.example.soundcloudplaylist.databinding.FragmentSoundCloudPlaylistBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import org.koin.android.ext.android.inject

class SoundCloudPlaylistFragment : BaseMvRxFragment() {

    //TODO: async subscribe showing Toasts when toggling favourite state

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        injectedItemListController(
            PlaylistViewState<BaseSoundCloudPlaylist, SoundCloudTrack>::tracks,
            "Tracks",
            reloadClicked = viewModel::loadData
        ) {
            it.clickableListItem { show { factory.newSoundCloudTrackVideosFragment(it) } }
        }
    }

    private val factory: IFragmentFactory by inject()

    private val viewModel: SoundCloudPlaylistViewModel by fragmentViewModel()

    private val connectivityComponent: ConnectivityComponent by lazy {
        reloadingConnectivityComponent(viewModel::loadData) {
            withState(viewModel) { state ->
                state.tracks.isEmptyAndLastLoadingFailedWithNetworkError()
            }
        }
    }

    private val playlist: BaseSoundCloudPlaylist by args()

    private val view: PlaylistView<BaseSoundCloudPlaylist> by lazy {
        PlaylistView(
            playlist = playlist,
            onFavouriteBtnClickListener = View.OnClickListener {}
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentSoundCloudPlaylistBinding>(
        inflater, R.layout.fragment_sound_cloud_playlist, container, false
    ).apply {
        view = this@SoundCloudPlaylistFragment.view
        playlist.artworkUrl?.let { url ->
            soundCloudPlaylistToolbarGradientBackgroundView
                .loadBackgroundGradient(url)
                .disposeOnDestroy(this@SoundCloudPlaylistFragment)
        }
        soundCloudPlaylistRecyclerView.apply {
            setController(epoxyController)
            layoutManager = GridLayoutManager(
                context,
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3
            )
            setItemSpacingDp(5)
        }
        soundCloudPlaylistToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
        mainContentFragment?.enablePlayButton { }
    }.root

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    companion object {
        fun newInstance(playlist: BaseSoundCloudPlaylist) = SoundCloudPlaylistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MvRx.KEY_ARG, playlist)
            }
        }
    }
}
