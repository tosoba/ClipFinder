package com.example.there.findclips.spotify.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseListFragment
import com.example.there.findclips.databinding.FragmentSpotifyAlbumsBinding
import com.example.there.findclips.model.entity.spotify.Album
import com.example.there.findclips.spotify.spotifyitem.album.AlbumFragment
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import kotlinx.android.synthetic.main.fragment_spotify_albums.*


class SpotifyAlbumsFragment : BaseListFragment<Album>() {

    override val itemsRecyclerView: RecyclerView?
        get() = albums_recycler_view

    override val defaultHeaderText: String = "Albums"

    override val viewState: ViewState<Album> = ViewState(ObservableSortedList<Album>(Album::class.java, Album.unsortedListCallback))

    override val listItemView: ListItemView<Album>
        get() = object : ListItemView<Album>(viewState.items) {
            override val itemViewBinder: ItemBinder<Album>
                get() = ItemBinderBase(BR.album, R.layout.grid_album_item)
        }

    override fun newInstanceOfFragmentToShowOnClick(item: Album): Fragment = AlbumFragment.newInstance(item)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyAlbumsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_albums, container, false)
        return binding.apply {
            view = this@SpotifyAlbumsFragment.view
            albumsRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) albumsRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }
}
