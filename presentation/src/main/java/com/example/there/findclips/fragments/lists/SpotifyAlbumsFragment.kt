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
import com.example.there.findclips.databinding.FragmentSpotifyAlbumsBinding
import com.example.there.findclips.model.entities.Album
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.lists.GridAlbumsList
import com.example.there.findclips.view.lists.OnAlbumClickListener
import com.example.there.findclips.view.recycler.HeaderDecoration
import com.example.there.findclips.view.recycler.SeparatorDecoration


class SpotifyAlbumsFragment : BaseSpotifyListFragment<Album>() {
    override val viewState: ViewState<Album> =
            ViewState(ObservableSortedList<Album>(Album::class.java, object : ObservableSortedList.Callback<Album> {
                override fun compare(o1: Album, o2: Album): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

                override fun areItemsTheSame(item1: Album, item2: Album): Boolean = item1.id == item2.id

                override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean = newItem.id == oldItem.id
            }))

    private val onAlbumClickListener = object : OnAlbumClickListener {
        override fun onClick(item: Album) = Router.goToAlbumActivity(activity, album = item)
    }

    private val view: SpotifyAlbumsFragment.View by lazy {
        SpotifyAlbumsFragment.View(
                state = viewState,
                adapter = GridAlbumsList.Adapter(viewState.items, R.layout.grid_album_item, onAlbumClickListener),
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyAlbumsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_albums, container, false)

        return binding.apply {
            view = this@SpotifyAlbumsFragment.view
            albumsRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader)
                albumsRecyclerView.addItemDecoration(HeaderDecoration.with(context)
                        .inflate(R.layout.albums_header)
                        .parallax(1f)
                        .dropShadowDp(2)
                        .columns(listColumnCount)
                        .build())
        }.root
    }

    data class View(val state: BaseSpotifyListFragment.ViewState<Album>,
                    val adapter: GridAlbumsList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration)

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
