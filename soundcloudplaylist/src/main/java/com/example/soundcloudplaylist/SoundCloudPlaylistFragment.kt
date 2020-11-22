package com.example.soundcloudplaylist

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.model.soundcloud.BaseSoundCloudPlaylist
import com.example.core.android.model.soundcloud.clickableListItem
import com.example.core.android.util.ext.*
import com.example.core.android.view.epoxy.loadableCollectionController
import com.example.soundcloudplaylist.databinding.FragmentSoundCloudPlaylistBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import org.koin.android.ext.android.inject

class SoundCloudPlaylistFragment : BaseMvRxFragment() {
    private val playlist: BaseSoundCloudPlaylist by args()
    private val viewModel: SoundCloudPlaylistViewModel by fragmentViewModel()
    private val factory: IFragmentFactory by inject()

    private val epoxyController: TypedEpoxyController<SoundCloudPlaylistState> by lazy(LazyThreadSafetyMode.NONE) {
        loadableCollectionController(
            SoundCloudPlaylistState::tracks,
            headerText = "Tracks",
            reloadClicked = viewModel::loadData,
            clearFailure = viewModel::clearTracksError
        ) { track ->
            track.clickableListItem {
                show { factory.newSoundCloudTrackVideosFragment(track) }
            }
        }
    }

    private val view: SoundCloudPlaylistView by lazy(LazyThreadSafetyMode.NONE) {
        SoundCloudPlaylistView(
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
    ): View? = FragmentSoundCloudPlaylistBinding.inflate(inflater, container, false)
        .apply {
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
            mainContentFragment?.enablePlayButton {}
        }
        .root

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    companion object {
        fun new(playlist: BaseSoundCloudPlaylist): SoundCloudPlaylistFragment = newMvRxFragmentWith(playlist)
    }
}
