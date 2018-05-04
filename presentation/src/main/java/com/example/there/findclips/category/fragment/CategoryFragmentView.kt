package com.example.there.findclips.category.fragment

import android.support.v7.widget.RecyclerView
import com.example.there.findclips.lists.GridPlaylistsList

data class CategoryFragmentView(
        val state: CategoryFragmentViewState,
        val adapter: GridPlaylistsList.Adapter,
        val layoutManager: RecyclerView.LayoutManager
)