package com.example.there.findclips.fragment.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyArtistsBinding
import com.example.there.findclips.fragment.artist.ArtistFragment
import com.example.there.findclips.model.entity.Artist
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.list.GridArtistsList
import com.example.there.findclips.view.list.OnArtistClickListener
import com.example.there.findclips.view.recycler.SeparatorDecoration
import kotlinx.android.synthetic.main.fragment_spotify_artists.*


class SpotifyArtistsFragment : BaseSpotifyListFragment<Artist>() {

    override val itemsRecyclerView: RecyclerView?
        get() = artists_recycler_view

    override val recyclerViewHeaderLayout: Int
        get() = R.layout.artists_header

    override val viewState: ViewState<Artist> = ViewState(ObservableSortedList<Artist>(Artist::class.java, Artist.sortedListCallback))

    private val onArtistClickListener = object : OnArtistClickListener {
        override fun onClick(item: Artist) {
            hostFragment?.showFragment(ArtistFragment.newInstance(artist = item), true)
        }
    }

    private val view: SpotifyArtistsFragment.View by lazy {
        SpotifyArtistsFragment.View(
                state = viewState,
                adapter = GridArtistsList.Adapter(viewState.items, R.layout.grid_artist_item, onArtistClickListener),
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyArtistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_artists, container, false)

        return binding.apply {
            view = this@SpotifyArtistsFragment.view
            artistsRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) artistsRecyclerView.addItemDecoration(headerItemDecoration(R.layout.artists_header))
        }.root
    }

    data class View(
            val state: BaseSpotifyListFragment.ViewState<Artist>,
                    val adapter: GridArtistsList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration
    )

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
