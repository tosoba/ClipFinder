package com.example.there.findclips.spotify.list

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
import com.example.there.findclips.databinding.FragmentSpotifyArtistsBinding
import com.example.there.findclips.model.entity.spotify.Artist
import com.example.there.findclips.spotify.spotifyitem.artist.ArtistFragment
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.navHostFragment
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import kotlinx.android.synthetic.main.fragment_spotify_artists.*


class SpotifyArtistsFragment : BaseSpotifyListFragment<Artist>() {

    override val itemsRecyclerView: RecyclerView?
        get() = artists_recycler_view

    override val defaultHeaderText: String = "Artists"

    override val viewState: ViewState<Artist> = ViewState(ObservableSortedList<Artist>(Artist::class.java, Artist.unsortedListCallback))

    override val view: BaseSpotifyListFragment.View<Artist> by lazy {
        BaseSpotifyListFragment.View(
                state = viewState,
                recyclerViewItemView = RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                ObservableField(false),
                                viewState.items,
                                ObservableField(false)
                        ),
                        object : ListItemView<Artist>(viewState.items) {
                            override val itemViewBinder: ItemBinder<Artist>
                                get() = ItemBinderBase(BR.artist, R.layout.grid_artist_item)
                        },
                        ClickHandler { artist ->
                            onItemClick?.let { it(artist) }
                                    ?: run { navHostFragment?.showFragment(ArtistFragment.newInstance(artist), true) }
                        }
                )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyArtistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_artists, container, false)

        return binding.apply {
            view = this@SpotifyArtistsFragment.view
            artistsRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) artistsRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }

    companion object {
        fun newInstance(
                mainHintText: String,
                additionalHintText: String,
                items: ArrayList<Artist>?,
                shouldShowHeader: Boolean = false
        ) = SpotifyArtistsFragment().apply {
            putArguments(mainHintText, additionalHintText, items, shouldShowHeader)
        }
    }
}
