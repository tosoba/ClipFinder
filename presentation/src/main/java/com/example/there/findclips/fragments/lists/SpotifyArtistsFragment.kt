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
import com.example.there.findclips.databinding.FragmentSpotifyArtistsBinding
import com.example.there.findclips.model.entities.Artist
import com.example.there.findclips.util.screenOrientation
import com.example.there.findclips.view.lists.GridArtistsList
import com.example.there.findclips.view.lists.OnArtistClickListener
import com.example.there.findclips.view.recycler.HeaderDecoration
import com.example.there.findclips.view.recycler.SeparatorDecoration


class SpotifyArtistsFragment : BaseSpotifyFragment<Artist>() {

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
            val columnCount = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
            artistsRecyclerView.layoutManager = GridLayoutManager(context, columnCount, GridLayoutManager.VERTICAL, false)
            artistsRecyclerView.addItemDecoration(HeaderDecoration.with(context)
                    .inflate(R.layout.artists_header)
                    .parallax(1f)
                    .dropShadowDp(2)
                    .columns(columnCount)
                    .build())
        }.root
    }

    data class View(val state: BaseSpotifyFragment.ViewState<Artist>,
                    val adapter: GridArtistsList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration)

    companion object {
        fun newInstance(mainHintText: String, additionalHintText: String) = SpotifyArtistsFragment().apply {
            BaseSpotifyFragment.putArguments(this, mainHintText, additionalHintText)
        }
    }
}
