package com.example.main

import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.MutableLiveData
import com.example.core.android.spotify.model.Track
import com.example.core.android.model.spotify.User
import com.example.core.android.model.videos.VideoPlaylist
import com.google.android.material.navigation.NavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class MainView(
    val state: MainViewState,
    val onDrawerNavigationItemSelectedListener: NavigationView.OnNavigationItemSelectedListener,
    val fadeOnClickListener: View.OnClickListener,
    val slideListener: SlidingUpPanelLayout.PanelSlideListener,
    val initialSlidePanelState: SlidingUpPanelLayout.PanelState,
    val onFavouriteBtnClickListener: View.OnClickListener,
    val pagerAdapter: FragmentStatePagerAdapter,
    val offScreenPageLimit: Int
)

class MainViewState(
    val playerState: ObservableField<PlayerState> = ObservableField(PlayerState.VIDEO),
    val isLoggedIn: MutableLiveData<Boolean> = MutableLiveData(false),
    val itemFavouriteState: ObservableField<Boolean> = ObservableField(false),
    val similarTracks: MutableLiveData<List<Track>> = MutableLiveData(),
    val favouriteVideoPlaylists: ObservableList<VideoPlaylist> = ObservableArrayList(),
    val mainContent: ObservableField<MainContent> = ObservableField(MainContent.SPOTIFY)
)

class DrawerHeaderViewState(
    val user: ObservableField<User> = ObservableField()
)

enum class PlayerState {
    VIDEO, TRACK, ALBUM, PLAYLIST, VIDEO_PLAYLIST, SOUND_CLOUD_TRACK
}

enum class MainContent {
    SPOTIFY, SOUNDCLOUD
}
