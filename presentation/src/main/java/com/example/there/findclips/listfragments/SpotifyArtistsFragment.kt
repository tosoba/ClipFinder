package com.example.there.findclips.listfragments

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.FragmentSpotifyArtistsBinding
import com.example.there.findclips.entities.Artist
import com.example.there.findclips.lists.GridArtistsList
import com.example.there.findclips.util.recyclerview.SeparatorDecoration
import com.example.there.findclips.util.screenOrientation
import android.support.v7.widget.RecyclerView


class SpotifyArtistsFragment : BaseSpotifyFragment<Artist>() {

    override val viewState = BaseSpotifyFragment.ViewState<Artist>()

    private val onArtistClickListener = object : GridArtistsList.OnItemClickListener {
        override fun onClick(item: Artist) {

        }
    }

    private val view: SpotifyArtistsFragment.View by lazy {
        SpotifyArtistsFragment.View(
                state = viewState,
                adapter = GridArtistsList.Adapter(viewState.items, R.layout.grid_artist_item, onArtistClickListener),
                itemDecoration = SeparatorDecoration(context!!, context!!.resources.getColor(R.color.colorAccent), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyArtistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_artists, container, false)
        binding.view = view
        binding.artistsRecyclerView.layoutManager = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        } else {
            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
        return binding.root
    }

    data class View(val state: BaseSpotifyFragment.ViewState<Artist>,
                    val adapter: GridArtistsList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration)
}
