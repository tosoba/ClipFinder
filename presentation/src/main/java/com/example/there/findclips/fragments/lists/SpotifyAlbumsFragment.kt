package com.example.there.findclips.fragments.lists

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyAlbumsBinding
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.util.screenOrientation
import com.example.there.findclips.view.lists.GridAlbumsList
import com.example.there.findclips.view.lists.OnAlbumClickListener
import com.example.there.findclips.view.recycler.HeaderDecoration
import com.example.there.findclips.view.recycler.SeparatorDecoration


class SpotifyAlbumsFragment : BaseSpotifyListFragment<Album>() {

    private val onAlbumClickListener = object : OnAlbumClickListener {
        override fun onClick(item: Album) = Router.goToAlbumAcitivity(activity, album = item)
    }

    private val view: SpotifyAlbumsFragment.View by lazy {
        SpotifyAlbumsFragment.View(
                state = viewState,
                adapter = GridAlbumsList.Adapter(viewState.items, R.layout.grid_album_item, onAlbumClickListener),
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyAlbumsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_albums, container, false)

        return binding.apply {
            view = this@SpotifyAlbumsFragment.view
            val columnCount = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
            albumsRecyclerView.layoutManager = GridLayoutManager(context, columnCount, GridLayoutManager.VERTICAL, false)
            albumsRecyclerView.addItemDecoration(HeaderDecoration.with(context)
                    .inflate(R.layout.albums_header)
                    .parallax(1f)
                    .dropShadowDp(2)
                    .columns(columnCount)
                    .build())
        }.root
    }

    data class View(val state: BaseSpotifyListFragment.ViewState<Album>,
                    val adapter: GridAlbumsList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration)

    companion object {
        fun newInstance(mainHintText: String, additionalHintText: String) = SpotifyAlbumsFragment().apply {
            BaseSpotifyListFragment.putArguments(this, mainHintText, additionalHintText)
        }
    }
}
