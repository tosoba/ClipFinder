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
import com.example.there.findclips.databinding.FragmentSpotifyArtistsBinding
import com.example.there.findclips.model.entity.spotify.Artist
import com.example.there.findclips.spotify.spotifyitem.artist.ArtistFragment
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import kotlinx.android.synthetic.main.fragment_spotify_artists.*


class SpotifyArtistsFragment : BaseListFragment<Artist>() {

    override val itemsRecyclerView: RecyclerView?
        get() = artists_recycler_view

    override val defaultHeaderText: String = "Artists"

    override val viewState: ViewState<Artist> = ViewState(ObservableSortedList<Artist>(Artist::class.java, Artist.unsortedListCallback))

    override val listItemView: ListItemView<Artist>
        get() = object : ListItemView<Artist>(viewState.items) {
            override val itemViewBinder: ItemBinder<Artist>
                get() = ItemBinderBase(BR.artist, R.layout.grid_artist_item)
        }

    override fun newInstanceOfFragmentToShowOnClick(item: Artist): Fragment = ArtistFragment.newInstance(item)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyArtistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_artists, container, false)

        return binding.apply {
            view = this@SpotifyArtistsFragment.view
            artistsRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) artistsRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }
}
