package com.example.there.findclips.fragments.lists

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
import com.example.there.findclips.databinding.FragmentSpotifyArtistsBinding
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.lists.GridArtistsList
import com.example.there.findclips.view.lists.OnArtistClickListener
import com.example.there.findclips.view.recycler.HeaderDecoration
import com.example.there.findclips.view.recycler.SeparatorDecoration


class SpotifyArtistsFragment : BaseSpotifyListFragment<Artist>() {

    override val viewState: ViewState<Artist> =
            ViewState(ObservableSortedList<Artist>(Artist::class.java, object : ObservableSortedList.Callback<Artist> {
                override fun compare(o1: Artist, o2: Artist): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

                override fun areItemsTheSame(item1: Artist, item2: Artist): Boolean = item1.id == item2.id

                override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean = newItem.id == oldItem.id
            }))

    private val onArtistClickListener = object : OnArtistClickListener {
        override fun onClick(item: Artist) = Router.goToArtistActivity(activity, artist = item)
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
            if (viewState.shouldShowHeader)
                artistsRecyclerView.addItemDecoration(HeaderDecoration.with(context)
                        .inflate(R.layout.artists_header)
                        .parallax(1f)
                        .dropShadowDp(2)
                        .columns(listColumnCount)
                        .build())
        }.root
    }

    data class View(val state: BaseSpotifyListFragment.ViewState<Artist>,
                    val adapter: GridArtistsList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration)

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
