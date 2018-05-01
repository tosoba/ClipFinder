package com.example.there.findclips.dashboard

import android.support.v7.widget.RecyclerView
import com.example.there.findclips.lists.CategoriesList
import com.example.there.findclips.lists.PlaylistsList
import com.example.there.findclips.lists.TopTracksList

data class DashboardView(
        val state: DashboardViewState,
        val categoriesAdapter: CategoriesList.Adapter,
        val categoriesLayoutManager: RecyclerView.LayoutManager,
        val playlistsAdapter: PlaylistsList.Adapter,
        val playlistsLayoutManager: RecyclerView.LayoutManager,
        val topTracksAdapter: TopTracksList.Adapter,
        val topTracksLayoutManager: RecyclerView.LayoutManager
)