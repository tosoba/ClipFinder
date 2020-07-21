package com.example.soundcloudplaylist

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.mvrx.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.playlist.PlaylistView
import com.example.coreandroid.base.playlist.PlaylistViewState
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.coreandroid.model.soundcloud.BaseSoundCloudPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.coreandroid.model.soundcloud.clickableListItem
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.epoxy.itemListController
import com.example.soundcloudplaylist.databinding.FragmentSoundCloudPlaylistBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class SoundCloudPlaylistFragment : BaseMvRxFragment(), NavigationCapable {

    //TODO: async subscribe showing Toasts when toggling favourite state

    override fun invalidate() = withState(viewModel) { state -> epoxyController.setData(state) }

    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))

    private val epoxyController by lazy {
        itemListController(
            builder,
            differ,
            viewModel,
            PlaylistViewState<BaseSoundCloudPlaylist, SoundCloudTrack>::tracks,
            "Tracks",
            reloadClicked = viewModel::loadData
        ) {
            it.clickableListItem { show { newSoundCloudTrackVideosFragment(it) } }
        }
    }

    override val factory: IFragmentFactory by inject()

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
