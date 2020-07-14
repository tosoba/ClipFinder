package com.example.spotifycategory

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
import com.airbnb.mvrx.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.model.isEmptyAndLastLoadingFailedWithNetworkError
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.clickableGridListItem
import com.example.coreandroid.preferences.SpotifyPreferences
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.epoxy.itemListController
import com.example.coreandroid.view.recyclerview.listener.EndlessRecyclerOnScrollListener
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

    private val view: CategoryView by lazy {
        CategoryView(
            category = category,
            onFavouriteBtnClickListener = View.OnClickListener {
                viewModel.toggleCategoryFavouriteState()
            }
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        reloadingConnectivityComponent({ viewModel.loadPlaylists() }) {
            withState(viewModel) { state -> state.playlists.isEmptyAndLastLoadingFailedWithNetworkError() }
        }
    }

    private val appPreferences: SpotifyPreferences by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        observePreferences()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        mainContentFragment?.disablePlayButton()
        return binding.apply {
            view = this@CategoryFragment.view
            categoryToolbarGradientBackgroundView
                .loadBackgroundGradient(category.iconUrl)
                .disposeOnDestroy(this@CategoryFragment)
            categoryRecyclerView.apply {
                setController(epoxyController)
                layoutManager = GridLayoutManager(context,
                    if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3)
                setItemSpacingDp(5)
                //TODO: animation
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
        category_recycler_view?.layoutManager = GridLayoutManager(
            context,
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 3
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    private fun observePreferences() {
        appPreferences.countryObservable
            .skip(1)
            .distinctUntilChanged()
            .subscribe({ viewModel.loadPlaylists(true) }, Timber::e)
            .disposeOnDestroy(this)
    }

    companion object {
        fun newInstance(category: Category) = CategoryFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MvRx.KEY_ARG, category)
            }
        }
    }
}
