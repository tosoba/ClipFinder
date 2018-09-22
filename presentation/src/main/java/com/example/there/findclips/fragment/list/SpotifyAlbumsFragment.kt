package com.example.there.findclips.fragment.list

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyAlbumsBinding
import com.example.there.findclips.fragment.album.AlbumFragment
import com.example.there.findclips.model.entity.Album
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import com.example.there.findclips.view.recycler.SeparatorDecoration
import kotlinx.android.synthetic.main.fragment_spotify_albums.*


class SpotifyAlbumsFragment : BaseSpotifyListFragment<Album>() {

    override val itemsRecyclerView: RecyclerView?
        get() = albums_recycler_view

    override val defaultHeaderText: String = "Albums"

    override val viewState: ViewState<Album> = ViewState(ObservableSortedList<Album>(Album::class.java, Album.sortedListCallback))

    override val view: BaseSpotifyListFragment.View<Album> by lazy {
        BaseSpotifyListFragment.View(
                state = viewState,
                recyclerViewItemView = RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                ObservableField(false),
                                viewState.items
                        ),
                        object : ListItemView<Album>(viewState.items) {
                            override val itemViewBinder: ItemBinder<Album>
                                get() = ItemBinderBase(BR.album, R.layout.grid_album_item)
                        },
                        ClickHandler {
                            hostFragment?.showFragment(AlbumFragment.newInstance(album = it), true)
                        },
                        SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f),
                        null
                )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyAlbumsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_albums, container, false)
        return binding.apply {
            view = this@SpotifyAlbumsFragment.view
            albumsRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) albumsRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }

    companion object {
        fun newInstance(
                mainHintText: String,
                additionalHintText: String,
                items: ArrayList<Album>?,
                shouldShowHeader: Boolean = false
        ) = SpotifyAlbumsFragment().apply {
            putArguments(mainHintText, additionalHintText, items, shouldShowHeader)
        }
    }
}
