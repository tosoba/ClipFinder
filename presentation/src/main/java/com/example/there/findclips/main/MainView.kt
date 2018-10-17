package com.example.there.findclips.main

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.SeekBar
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.model.entity.User
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.VideoItemView
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class MainView(
        val state: MainViewState,
        val onNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener,
        val pagerAdapter: FragmentStatePagerAdapter,
        val onPageChangeListener: ViewPager.OnPageChangeListener,
        val offScreenPageLimit: Int,
        val onDrawerNavigationItemSelectedListener: NavigationView.OnNavigationItemSelectedListener,
        val fadeOnClickListener: View.OnClickListener,
        val slideListener: SlidingUpPanelLayout.PanelSlideListener,
        val initialSlidePanelState: SlidingUpPanelLayout.PanelState,
        val relatedVideosRecyclerViewItemView: RecyclerViewItemView<VideoItemView>,
        val onFavouriteBtnClickListener: View.OnClickListener,
        val onSpotifyPlayPauseBtnClickListener: View.OnClickListener,
        val onCloseSpotifyPlayerBtnClickListener: View.OnClickListener,
        val onPreviousBtnClickListener: View.OnClickListener,
        val onNextBtnClickListener: View.OnClickListener,
        val onPlaybackSeekBarProgressChangeListener: SeekBar.OnSeekBarChangeListener,
        val onYoutubePlayerCloseBtnClickListener: View.OnClickListener,
        val onYoutubePlayerPlayPauseBtnClickListener: View.OnClickListener
)

class MainViewState(
        val videos: ObservableList<VideoItemView> = ObservableArrayList(),
        val similarTracks: MutableLiveData<List<Track>> = MutableLiveData(),
        val favouriteVideoPlaylists: ObservableList<VideoPlaylist> = ObservableArrayList(),
        val initialVideosLoadingInProgress: ObservableField<Boolean> = ObservableField(false),
        val loadingMoreVideosInProgress: ObservableField<Boolean> = ObservableField(false),
        val playerState: ObservableField<PlayerState> = ObservableField(PlayerState.VIDEO),
        val isLoggedIn: ObservableField<Boolean> = ObservableField(false),
        val playbackSeekbarMaxValue: ObservableField<Int> = ObservableField(0),
        val nextTrackExists: ObservableField<Boolean> = ObservableField(false),
        val previousTrackExists: ObservableField<Boolean> = ObservableField(false),
        val currentTrackTitle: ObservableField<String> = ObservableField(""),
        val itemFavouriteState: ObservableField<Boolean> = ObservableField(false)
)

class DrawerHeaderViewState(
        val user: ObservableField<User> = ObservableField()
)

enum class PlayerState {
    VIDEO, TRACK, ALBUM, PLAYLIST, VIDEO_PLAYLIST
}