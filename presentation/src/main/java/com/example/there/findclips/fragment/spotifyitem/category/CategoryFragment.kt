package com.example.there.findclips.fragment.spotifyitem.category

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.data.preferences.AppPreferences
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.databinding.FragmentCategoryBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.list.SpotifyPlaylistsFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.lifecycle.DisposablesComponent
import com.example.there.findclips.lifecycle.OnPropertyChangedCallbackComponent
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.util.ext.*
import com.squareup.picasso.Picasso
import javax.inject.Inject

class CategoryFragment : BaseVMFragment<CategoryViewModel>(CategoryViewModel::class.java), Injectable {

    private val category: Category by lazy { arguments!!.getParcelable<Category>(ARG_CATEGORY) }

    private val view: CategoryView by lazy {
        CategoryView(
                state = viewModel.viewState,
                category = category,
                onFavouriteBtnClickListener = View.OnClickListener {
                    if (viewModel.viewState.isSavedAsFavourite.get() == true) {
                        viewModel.deleteFavouriteCategory(category)
                        Toast.makeText(activity, "${category.name} removed from favourite categories.", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addFavouriteCategory(category)
                        Toast.makeText(activity, "${category.name} added to favourite categories.", Toast.LENGTH_SHORT).show()
                    }
                }
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { viewModel.playlists.value != null },
                mainActivity!!.connectivitySnackbarParentView!!,
                ::loadData,
                true
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        observePreferences()
        loadData()
    }

    @Inject
    lateinit var appPreferences: AppPreferences

    private fun observePreferences() = disposablesComponent.add(appPreferences.countryObservable
            .skip(1)
            .distinctUntilChanged()
            .subscribe {
                playlistsFragment?.clearItems()
                viewModel.loadData(category.id, true)
            }
    )

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isSavedAsFavourite) { _, _ ->
            binding.categoryFavouriteFab.hideAndShow()
        })
        return binding.apply {
            view = this@CategoryFragment.view
            disposablesComponent.add(Picasso.with(context).getBitmapSingle(category.iconUrl, {
                it.generateColorGradient {
                    categoryToolbarGradientBackgroundView.background = it
                    categoryToolbarGradientBackgroundView.invalidate()
                }
            }))
            categoryToolbar.setupWithBackNavigation(mainActivity)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsFragment!!.loadMore = ::loadData
    }

    private val playlistsFragment: SpotifyPlaylistsFragment?
        get() = childFragmentManager.findFragmentById(R.id.category_spotify_playlists_fragment) as? SpotifyPlaylistsFragment

    override fun setupObservers() {
        super.setupObservers()
        viewModel.playlists.observe(this, Observer { playlists ->
            playlists?.let { playlistsFragment?.updateItems(it, false) }
        })
    }

    private fun loadData() = viewModel.loadPlaylists(category)

    companion object {
        private const val ARG_CATEGORY = "ARG_CATEGORY"

        fun newInstance(category: Category) = CategoryFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_CATEGORY, category)
            }
        }
    }
}
