package com.example.there.findclips.fragment.list

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyPlaylistsBinding
import com.example.there.findclips.fragment.spotifyitem.playlist.PlaylistFragment
import com.example.there.findclips.model.entity.Playlist
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import kotlinx.android.synthetic.main.fragment_spotify_playlists.*


class SpotifyPlaylistsFragment : BaseSpotifyListFragment<Playlist>() {

    override val itemsRecyclerView: RecyclerView?
        get() = playlists_recycler_view

    override val defaultHeaderText: String = "Playlists"

    override val viewState: ViewState<Playlist> = ViewState(ObservableSortedList<Playlist>(Playlist::class.java, Playlist.sortedListCallback))

    override val view: BaseSpotifyListFragment.View<Playlist> = BaseSpotifyListFragment.View(
            state = viewState,
            recyclerViewItemView = RecyclerViewItemView(
                    RecyclerViewItemViewState(
                            ObservableField(false),
                            viewState.items
                    ),
                    object : ListItemView<Playlist>(viewState.items) {
                        override val itemViewBinder: ItemBinder<Playlist>
                            get() = ItemBinderBase(BR.playlist, R.layout.grid_playlist_item)
                    },
                    ClickHandler { playlist ->
                        onItemClick?.let { it(playlist) }
                                ?: run { hostFragment?.showFragment(PlaylistFragment.newInstance(playlist), true) }
                    },
                    null,
                    onScrollListener
            )
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyPlaylistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_playlists, container, false)
        return binding.apply {
            view = this@SpotifyPlaylistsFragment.view
            playlistsRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) playlistsRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }

    class View(
            val state: BaseSpotifyListFragment.ViewState<Playlist>,
            val recyclerViewItemView: RecyclerViewItemView<Playlist>
    )

    companion object {
        fun newInstance(
                mainHintText: String,
                additionalHintText: String,
                items: ArrayList<Playlist>?,
                shouldShowHeader: Boolean = false
        ) = SpotifyPlaylistsFragment().apply {
            putArguments(mainHintText, additionalHintText, items, shouldShowHeader)
        }
    }
}
