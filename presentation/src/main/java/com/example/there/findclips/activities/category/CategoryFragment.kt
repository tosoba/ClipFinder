package com.example.there.findclips.activities.category

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyVMFragment
import com.example.there.findclips.base.fragment.HasBackNavigation
import com.example.there.findclips.databinding.FragmentCategoryBinding
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.fragments.lists.SpotifyPlaylistsFragment
import com.example.there.findclips.lifecycle.ConnectivityComponent
import com.example.there.findclips.model.entities.Category
import com.example.there.findclips.util.ext.accessToken
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : BaseSpotifyVMFragment<CategoryViewModel>(), Injectable, HasBackNavigation {

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
                category_root_layout,
                ::loadData
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lifecycle.addObserver(connectivityComponent)

        if (savedInstanceState == null)
            loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        return binding.apply {
            view = this@CategoryFragment.view
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
        viewModel.playlists.observe(this, Observer {
            it?.let { playlistsFragment.updateItems(it, false) }
        })
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(CategoryViewModel::class.java)
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
