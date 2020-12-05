package com.clipfinder.spotify.category

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.TypedEpoxyController
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.clipfinder.spotify.category.databinding.FragmentSpotifyCategoryBinding
import com.example.core.android.spotify.model.Category
import com.example.core.android.spotify.model.clickableGridListItem
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.*
import com.example.core.android.view.epoxy.loadableCollectionController
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_spotify_category.*
import org.koin.android.ext.android.inject

class SpotifyCategoryFragment : BaseMvRxFragment() {
    private val category: Category by args()
    private val factory: ISpotifyFragmentsFactory by inject()
    private val viewModel: SpotifyCategoryViewModel by fragmentViewModel()

    private val epoxyController: TypedEpoxyController<SpotifyCategoryState> by lazy(LazyThreadSafetyMode.NONE) {
        val loadPlaylists = { viewModel.loadPlaylists() }
        loadableCollectionController(
            SpotifyCategoryState::playlists,
            headerText = getString(R.string.playlists),
            loadMore = loadPlaylists,
            clearFailure = viewModel::clearPlaylistsError,
            reloadClicked = loadPlaylists
        ) { playlist ->
            playlist.clickableGridListItem {
                show { factory.newSpotifyPlaylistFragment(playlist) }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = FragmentSpotifyCategoryBinding.inflate(inflater, container, false)
        .apply {
            mainContentFragment?.disablePlayButton()
            category = this@SpotifyCategoryFragment.category
            categoryToolbarGradientBackgroundView
                .loadBackgroundGradient(this@SpotifyCategoryFragment.category.iconUrl)
                .disposeOnDestroy(this@SpotifyCategoryFragment)
            categoryRecyclerView.apply {
                setController(epoxyController)
                layoutManager = layoutManagerFor(resources.configuration.orientation)
                setItemSpacingDp(5)
            }
            categoryToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
        }
        .root

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        category_recycler_view?.layoutManager = layoutManagerFor(newConfig.orientation)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    private fun layoutManagerFor(orientation: Int): RecyclerView.LayoutManager = GridLayoutManager(
        context,
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3
    )

    companion object {
        fun new(category: Category): SpotifyCategoryFragment = newMvRxFragmentWith(category)
    }
}
