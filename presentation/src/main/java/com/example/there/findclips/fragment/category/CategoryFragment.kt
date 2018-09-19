package com.example.there.findclips.fragment.category

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentCategoryBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragment.list.SpotifyPlaylistsFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.util.ext.accessToken
import com.example.there.findclips.util.ext.mainActivity

class CategoryFragment : BaseSpotifyVMFragment<CategoryViewModel>(CategoryViewModel::class.java), Injectable {

    private val category: Category by lazy { arguments!!.getParcelable<Category>(ARG_CATEGORY) }

    private val view: CategoryView by lazy {
        CategoryView(
                state = viewModel.viewState,
                category = category,
                onFavouriteBtnClickListener = View.OnClickListener {
                    viewModel.addFavouriteCategory(category)
                    Toast.makeText(activity, "Added to favourites.", Toast.LENGTH_SHORT).show()
                }
        )
    }

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { viewModel.playlists.value != null },
                mainActivity!!.connectivitySnackbarParentView!!,
                ::loadData
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        loadData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        return binding.apply {
            view = this@CategoryFragment.view
            mainActivity?.setSupportActionBar(categoryToolbar)
            categoryToolbar.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.arrow_back, null)
            categoryToolbar.setNavigationOnClickListener { mainActivity?.onBackPressed() }
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsFragment.loadMore = ::loadData
    }

    private val playlistsFragment: SpotifyPlaylistsFragment
        get() = childFragmentManager.findFragmentById(R.id.category_spotify_playlists_fragment) as SpotifyPlaylistsFragment

    override fun setupObservers() {
        super.setupObservers()
        viewModel.playlists.observe(this, Observer { playlists ->
            playlists?.let { playlistsFragment.updateItems(it, false) }
        })
    }

    private fun loadData() = viewModel.loadPlaylists(activity?.accessToken, category.id)

    companion object {
        private const val ARG_CATEGORY = "ARG_CATEGORY"

        fun newInstance(category: Category) = CategoryFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_CATEGORY, category)
            }
        }
    }
}
