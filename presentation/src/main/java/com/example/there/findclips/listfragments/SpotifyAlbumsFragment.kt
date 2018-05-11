package com.example.there.findclips.listfragments

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.FragmentSpotifyAlbumsBinding
import com.example.there.findclips.entities.Album
import com.example.there.findclips.lists.GridAlbumsList
import com.example.there.findclips.util.recyclerview.SeparatorDecoration
import com.example.there.findclips.util.screenOrientation


class SpotifyAlbumsFragment : BaseSpotifyFragment<Album>() {

    override val viewState: BaseSpotifyFragment.ViewState<Album> = BaseSpotifyFragment.ViewState()

    private val onAlbumClickListener = object : GridAlbumsList.OnItemClickListener {
        override fun onClick(item: Album) {

        }
    }

    private val view: SpotifyAlbumsFragment.View by lazy {
        SpotifyAlbumsFragment.View(
                state = viewState,
                adapter = GridAlbumsList.Adapter(viewState.items, R.layout.grid_album_item, onAlbumClickListener),
                itemDecoration = SeparatorDecoration(context!!, context!!.resources.getColor(R.color.colorAccent), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyAlbumsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_albums, container, false)
        binding.view = view
        binding.albumsRecyclerView.layoutManager = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        } else {
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
        return binding.root
    }

    data class View(val state: BaseSpotifyFragment.ViewState<Album>,
                    val adapter: GridAlbumsList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration)
}
