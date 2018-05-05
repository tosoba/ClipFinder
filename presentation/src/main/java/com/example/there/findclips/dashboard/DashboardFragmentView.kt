package com.example.there.findclips.dashboard

import android.support.v7.widget.RecyclerView
import com.example.there.findclips.lists.CategoriesList
import com.example.there.findclips.lists.PlaylistsList
import com.example.there.findclips.lists.TopTracksList

data class DashboardFragmentView(
        val state: DashboardViewState,
        val categoriesAdapter: CategoriesList.Adapter,
        val playlistsAdapter: PlaylistsList.Adapter,
        val topTracksAdapter: TopTracksList.Adapter
)