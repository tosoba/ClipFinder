package com.example.spotify.category.ui

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.model.spotify.Category
import com.example.core.android.model.spotify.clickableGridListItem
import com.example.core.android.util.ext.*
import com.example.core.android.view.epoxy.injectedItemListController
import com.example.spotify.category.R
import com.example.spotify.category.databinding.FragmentSpotifyCategoryBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_spotify_category.*
import org.koin.android.ext.android.inject

class SpotifyCategoryFragment : BaseMvRxFragment(), NavigationCapable {

    override val factory: IFragmentFactory by inject()

    private val viewModel: SpotifyCategoryViewModel by fragmentViewModel()

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        val loadPlaylists = { viewModel.loadPlaylists() }
        injectedItemListController(
            SpotifyCategoryViewState::playlists,
            getString(R.string.playlists),
            loadMore = loadPlaylists,
            reloadClicked = loadPlaylists
        ) {
            it.clickableGridListItem { show { newSpotifyPlaylistFragment(it) } }
        }
    }

    private val category: Category by args()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentSpotifyCategoryBinding.inflate(inflater, container, false).apply {
        mainContentFragment?.disablePlayButton()
        category = this@SpotifyCategoryFragment.category
        categoryFavouriteFab.setOnClickListener { viewModel.toggleCategoryFavouriteState() }
        categoryToolbarGradientBackgroundView
            .loadBackgroundGradient(this@SpotifyCategoryFragment.category.iconUrl)
            .disposeOnDestroy(this@SpotifyCategoryFragment)
        categoryRecyclerView.apply {
            setController(epoxyController)
            layoutManager = layoutManagerFor(resources.configuration.orientation)
            setItemSpacingDp(5)
        }
        categoryToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectSubscribe(this, SpotifyCategoryViewState::isSavedAsFavourite) {
            category_favourite_fab?.setImageDrawable(
                ContextCompat.getDrawable(
                    view.context,
                    if (it.value) R.drawable.delete else R.drawable.favourite
                )
            )
            category_favourite_fab?.hideAndShow()
        }
    }

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
        fun newInstance(category: Category) = SpotifyCategoryFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, category) }
        }
    }
}
