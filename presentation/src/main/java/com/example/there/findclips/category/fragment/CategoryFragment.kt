package com.example.there.findclips.category.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.BaseSpotifyVMFragment
import com.example.there.findclips.databinding.FragmentCategoryBinding
import com.example.there.findclips.entities.Category
import com.example.there.findclips.entities.Playlist
import com.example.there.findclips.lists.GridPlaylistsList
import com.example.there.findclips.util.accessToken
import com.example.there.findclips.util.app
import com.example.there.findclips.util.screenOrientation
import javax.inject.Inject


class CategoryFragment : BaseSpotifyVMFragment<CategoryViewModel>() {

    @Inject
    lateinit var factory: CategoryVMFactory

    private val category: Category by lazy { arguments!!.getParcelable(ARG_CATEGORY) as Category }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            mainViewModel.loadPlaylists(activity?.accessToken, category.id)
        }
    }

    private val onPlaylistItemClickListener = object : GridPlaylistsList.OnItemClickListener {
        override fun onClick(item: Playlist) {

        }
    }

    private val view: CategoryFragmentView by lazy {
        CategoryFragmentView(state = mainViewModel.viewState,
                adapter = GridPlaylistsList.Adapter(mainViewModel.viewState.playlists, R.layout.grid_playlist_item, onPlaylistItemClickListener),
                layoutManager = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
                } else {
                    GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        binding.view = view
        return binding.root
    }

    override fun initComponent() {
        activity?.app?.createCategoryComponent()?.inject(this)
    }

    override fun releaseComponent() {
        activity?.app?.releaseCategoryComponent()
    }

    override fun initViewModel() {
        mainViewModel = ViewModelProviders.of(this, factory).get(CategoryViewModel::class.java)
    }

    companion object {
        private const val ARG_CATEGORY = "ARG_CATEGORY"

        fun newInstance(category: Category) = CategoryFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_CATEGORY, category)
            }
        }
    }
}
