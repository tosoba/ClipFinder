package com.example.there.findclips.main

import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.there.findclips.model.entity.Video
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.view.list.impl.RelatedVideosList
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class MainView(
        val state: MainViewState,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onPageChangeListener: ViewPager.OnPageChangeListener,
        val offScreenPageLimit: Int,
        val fadeOnClickListener: View.OnClickListener,
        val slideListener: SlidingUpPanelLayout.PanelSlideListener,
        val initialSlidePanelState: SlidingUpPanelLayout.PanelState,
        val relatedVideosAdapter: RelatedVideosList.Adapter,
        val onRelatedVideosScroll: RecyclerView.OnScrollListener,
        val itemDecoration: RecyclerView.ItemDecoration,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val onSpotifyPlayPauseBtnClickListener: View.OnClickListener,
        val onCloseSpotifyPlayerBtnClickListener: View.OnClickListener
)

data class MainViewState(
        val videos: ObservableList<Video> = ObservableArrayList(),
        val favouriteVideoPlaylists: ObservableList<VideoPlaylist> = ObservableArrayList(),
        val initialVideosLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val loadingMoreVideosInProgress: ObservableField<Boolean> = ObservableField(false),
        val playerState: ObservableField<PlayerState> = ObservableField(PlayerState.VIDEO),
        val isLoggedIn: ObservableField<Boolean> = ObservableField(false)
)

enum class PlayerState {
    VIDEO, TRACK, ALBUM, PLAYLIST
}