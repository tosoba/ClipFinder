package com.example.there.findclips.search

import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.widget.SearchView
import com.example.there.findclips.lists.VideosList
import com.example.there.findclips.search.spotify.SpotifySearchViewState
import com.example.there.findclips.search.videos.VideosSearchViewState

data class SearchFragmentView(
        val state: SearchViewState,
        val videosSearchViewState: VideosSearchViewState,
        val spotifySearchViewState: SpotifySearchViewState,
        val videosAdapter: VideosList.Adapter,
        val videosLayoutManager: RecyclerView.LayoutManager,
        val videosItemDecoration: RecyclerView.ItemDecoration,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onQueryTextListener: SearchView.OnQueryTextListener,
        val onTabSelectedListener: TabLayout.OnTabSelectedListener,
        val onPageChangeListener: ViewPager.OnPageChangeListener
)