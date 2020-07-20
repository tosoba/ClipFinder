package com.example.spotifycategory.ui

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.mvrx.*
import com.example.core.android.spotify.preferences.SpotifyPreferences
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.clickableGridListItem
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.epoxy.itemListController
import com.example.coreandroid.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import com.example.spotifycategory.R
import com.example.spotifycategory.databinding.FragmentCategoryBinding
import com.wada811.lifecycledispose.disposeOnDestroy
import kotlinx.android.synthetic.main.fragment_category.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import timber.log.Timber

class CategoryFragment : BaseMvRxFragment(), NavigationCapable {

    override val factory: IFragmentFactory by inject()

    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))

    override fun invalidate() = withState(viewModel) { state -> epoxyController.setData(state) }

    private val viewModel: CategoryViewModel by fragmentViewModel()

    //TODO: test endless scroll listener
    private val epoxyController by lazy {
        itemListController(builder, differ, viewModel,
            CategoryViewState::playlists, "Playlists",
            onScrollListener = EndlessRecyclerOnScrollListener { viewModel.loadPlaylists() },
            reloadClicked = { viewModel.loadPlaylists() }
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
    ): View? {
        val binding: FragmentCategoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_category, container, false
        )
        mainContentFragment?.disablePlayButton()
        return binding.apply {
            category = this@CategoryFragment.category
            categoryFavouriteFab.setOnClickListener { viewModel.toggleCategoryFavouriteState() }
            categoryToolbarGradientBackgroundView
                .loadBackgroundGradient(category.iconUrl)
                .disposeOnDestroy(this@CategoryFragment)
            categoryRecyclerView.apply {
                setController(epoxyController)
                layoutManager = layoutManagerFor(resources.configuration.orientation)
                setItemSpacingDp(5)
            }
            categoryToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectSubscribe(this, CategoryViewState::isSavedAsFavourite) {
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

    private fun layoutManagerFor(orientation: Int): RecyclerView.LayoutManager = GridLayoutManager(
        context,
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3
    )

    companion object {
        fun newInstance(category: Category) = CategoryFragment().apply {
            arguments = Bundle().apply { putParcelable(MvRx.KEY_ARG, category) }
        }
    }
}
