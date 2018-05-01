package com.example.there.findclips.bindings

import android.content.res.Configuration
import android.databinding.BindingAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.there.findclips.R
import com.example.there.findclips.lists.*
import com.example.there.findclips.util.ItemClickSupport
import com.example.there.findclips.util.SeparatorDecoration
import com.example.there.findclips.util.screenOrientation


@BindingAdapter("playlistsAdapter")
fun bindPlaylistsAdapter(recycler: RecyclerView, adapter: PlaylistsList.Adapter) {
    recycler.layoutManager = GridLayoutManager(recycler.context, 2, GridLayoutManager.HORIZONTAL, false)
    recycler.adapter = adapter
}

@BindingAdapter("categoriesAdapter")
fun bindCategoriesAdapter(recycler: RecyclerView, adapter: CategoriesList.Adapter) {
    recycler.layoutManager = GridLayoutManager(recycler.context, 2, GridLayoutManager.HORIZONTAL, false)
    recycler.adapter = adapter
}

@BindingAdapter("topTracksAdapter")
fun bindTopTracksAdapter(recycler: RecyclerView, adapter: TopTracksList.Adapter) {
    recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.HORIZONTAL, false)
    recycler.adapter = adapter
}

@BindingAdapter("videosAdapter")
fun bindVideosAdapter(recycler: RecyclerView, adapter: VideosList.Adapter) {
    if (recycler.context.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
        recycler.layoutManager = GridLayoutManager(recycler.context, 2, GridLayoutManager.VERTICAL, false)
    } else {
        recycler.layoutManager = LinearLayoutManager(recycler.context, LinearLayoutManager.VERTICAL, false)
    }

    recycler.addItemDecoration(SeparatorDecoration(recycler.context, recycler.context.resources.getColor(R.color.colorAccent), 2f))

    recycler.adapter = adapter
}

