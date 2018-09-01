package com.example.there.findclips.fragments.lists

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyPlaylistsBinding
import com.example.there.findclips.model.entities.Playlist
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.lists.GridPlaylistsList
import com.example.there.findclips.view.lists.OnPlaylistClickListener
import com.example.there.findclips.view.recycler.HeaderDecoration


class SpotifyPlaylistsFragment : BaseSpotifyListFragment<Playlist>() {

    override val viewState: ViewState<Playlist> =
            ViewState(ObservableSortedList<Playlist>(Playlist::class.java, object : ObservableSortedList.Callback<Playlist> {
                override fun compare(o1: Playlist, o2: Playlist): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

                override fun areItemsTheSame(item1: Playlist, item2: Playlist): Boolean = item1.id == item2.id

                override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean = newItem.id == oldItem.id
            }))

    private val onPlaylistClickListener = object : OnPlaylistClickListener {
        override fun onClick(item: Playlist) = Router.goToPlaylistActivity(activity, playlist = item)
    }

    private val view: SpotifyPlaylistsFragment.View = SpotifyPlaylistsFragment.View(
            state = viewState,
            adapter = GridPlaylistsList.Adapter(viewState.items, R.layout.grid_playlist_item, onPlaylistClickListener)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyPlaylistsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_playlists, container, false)
        return binding.apply {
            view = this@SpotifyPlaylistsFragment.view
            playlistsRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader)
                playlistsRecyclerView.addItemDecoration(HeaderDecoration.with(context)
                        .inflate(R.layout.playlists_header)
                        .parallax(1f)
                        .dropShadowDp(2)
                        .columns(listColumnCount)
                        .build())
        }.root
    }

    data class View(val state: BaseSpotifyListFragment.ViewState<Playlist>,
                    val adapter: GridPlaylistsList.Adapter)

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
