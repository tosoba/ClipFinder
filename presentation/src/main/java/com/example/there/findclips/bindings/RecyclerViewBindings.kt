package com.example.there.findclips.bindings

import android.content.res.Configuration
import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.there.domain.entities.spotify.CategoryEntity
import com.example.there.domain.entities.spotify.PlaylistEntity
import com.example.there.domain.entities.spotify.TopTrackEntity
import com.example.there.findclips.R
import com.example.there.findclips.dashboard.lists.CategoriesListAdapter
import com.example.there.findclips.dashboard.lists.PlaylistsListAdapter
import com.example.there.findclips.dashboard.lists.toptracks.TopTrackItemClickListener
import com.example.there.findclips.dashboard.lists.toptracks.TopTracksListAdapter
import com.example.there.findclips.entities.Video
import com.example.there.findclips.util.ItemClickSupport
import com.example.there.findclips.util.SeparatorDecoration
import com.example.there.findclips.util.screenOrientation
import com.example.there.findclips.videoslist.VideosListAdapter


fun <T> makeOnListChangedCallback(recycler: RecyclerView): ObservableList.OnListChangedCallback<ObservableArrayList<T>> =
        object : ObservableList.OnListChangedCallback<ObservableArrayList<T>>() {
            override fun onChanged(sender: ObservableArrayList<T>?) {
                recycler.adapter.notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
                recycler.adapter.notifyItemRangeRemoved(positionStart, itemCount)
            }

            override fun onItemRangeMoved(sender: ObservableArrayList<T>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
                recycler.adapter.notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeInserted(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
                recycler.adapter.notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(sender: ObservableArrayList<T>?, positionStart: Int, itemCount: Int) {
                recycler.adapter.notifyItemRangeChanged(positionStart, itemCount)
            }
        }

@BindingAdapter("featuredPlaylists")
fun bindFeaturedPlaylists(recycler: RecyclerView, playlists: ObservableArrayList<PlaylistEntity>) {
    recycler.layoutManager = GridLayoutManager(recycler.context, 2, GridLayoutManager.HORIZONTAL, false)
    playlists.addOnListChangedCallback(makeOnListChangedCallback<PlaylistEntity>(recycler))
    recycler.adapter = PlaylistsListAdapter(playlists)
}

@BindingAdapter("categories")
fun bindCategories(recycler: RecyclerView, categories: ObservableArrayList<CategoryEntity>) {
    recycler.layoutManager = GridLayoutManager(recycler.context, 2, GridLayoutManager.HORIZONTAL, false)
    categories.addOnListChangedCallback(makeOnListChangedCallback<CategoryEntity>(recycler))
    recycler.adapter = CategoriesListAdapter(categories)
}

@BindingAdapter("topTracks")
fun bindTopTracks(recycler: RecyclerView, tracks: ObservableArrayList<TopTrackEntity>) {
    recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.HORIZONTAL, false)
    tracks.addOnListChangedCallback(makeOnListChangedCallback<TopTrackEntity>(recycler))
    recycler.adapter = TopTracksListAdapter(tracks)
}

@BindingAdapter("onTopTrackClickListener")
fun bindOnTopTrackClickListener(recycler: RecyclerView, listener: TopTrackItemClickListener) {
    ItemClickSupport.addTo(recycler).setOnItemClickListener { recyclerView, position, _ ->
        val adapter = recyclerView.adapter as? TopTracksListAdapter
        adapter?.let {
            val track = adapter.tracks[position]
            listener.onClick(track)
        }
    }
}

@BindingAdapter("videos")
fun bindVideos(recycler: RecyclerView, videos: ObservableArrayList<Video>) {
    if (recycler.context.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
        recycler.layoutManager = GridLayoutManager(recycler.context, 2, GridLayoutManager.VERTICAL, false)
    } else {
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
    }

    recycler.addItemDecoration(SeparatorDecoration(recycler.context, recycler.context.resources.getColor(R.color.colorAccent), 2f))

    videos.addOnListChangedCallback(makeOnListChangedCallback<Video>(recycler))
    recycler.adapter = VideosListAdapter(videos)
}

