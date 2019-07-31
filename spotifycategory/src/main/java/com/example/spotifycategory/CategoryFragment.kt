package com.example.spotifycategory

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.airbnb.mvrx.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.model.isEmptyAndLastLoadingFailed
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.model.spotify.clickableListItem
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.view.epoxy.itemListController
import com.example.spotifycategory.databinding.FragmentCategoryBinding
import com.example.spotifyrepo.preferences.SpotifyPreferences
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

    private val epoxyController by lazy {
        itemListController(builder, differ, viewModel,
                CategoryViewState::playlists, "Playlists",
                reloadClicked = { viewModel.loadPlaylists() }
        ) {
            it.clickableListItem { show { newSpotifyPlaylistFragment(it) } }
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
            withState(viewModel) { state -> state.playlists.isEmptyAndLastLoadingFailed() }
        }
    }

    private val disposablesComponent = DisposablesComponent()

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
            categoryToolbarGradientBackgroundView.loadBackgroundGradient(category.iconUrl, disposablesComponent)
            categoryRecyclerView.apply {
                setController(epoxyController)
                //TODO: animation
            }
            categoryToolbar.setupWithBackNavigation(appCompatActivity)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.selectSubscribe(this, CategoryViewState::isSavedAsFavourite) {
            category_favourite_fab?.setImageDrawable(ContextCompat.getDrawable(view.context,
                    if (it.value) R.drawable.delete else R.drawable.favourite))
            category_favourite_fab?.hideAndShow()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    private fun observePreferences() {
        appPreferences.countryObservable
                .skip(1)
                .distinctUntilChanged()
                .subscribe({ viewModel.loadPlaylists(true) }, Timber::e)
                .disposeWith(disposablesComponent)
    }

    companion object {
        fun newInstance(category: Category) = CategoryFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MvRx.KEY_ARG, category)
            }
        }
    }
}