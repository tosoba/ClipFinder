package com.example.there.findclips.main

import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.support.design.widget.NavigationView
import android.view.View
import com.example.there.findclips.model.entity.Track
import com.example.there.findclips.model.entity.User
import com.example.there.findclips.model.entity.VideoPlaylist
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class MainView(
        val state: MainViewState,
        val onDrawerNavigationItemSelectedListener: NavigationView.OnNavigationItemSelectedListener,
        val fadeOnClickListener: View.OnClickListener,
        val slideListener: SlidingUpPanelLayout.PanelSlideListener,
        val initialSlidePanelState: SlidingUpPanelLayout.PanelState,
        val onFavouriteBtnClickListener: View.OnClickListener
)

class MainViewState(
        val playerState: ObservableField<PlayerState> = ObservableField(PlayerState.VIDEO),
        val isLoggedIn: ObservableField<Boolean> = ObservableField(false),
        val itemFavouriteState: ObservableField<Boolean> = ObservableField(false),
        val similarTracks: MutableLiveData<List<Track>> = MutableLiveData(),
        val favouriteVideoPlaylists: ObservableList<VideoPlaylist> = ObservableArrayList<VideoPlaylist>()
)

class DrawerHeaderViewState(
        val user: ObservableField<User> = ObservableField()
)

enum class PlayerState {
    VIDEO, TRACK, ALBUM, PLAYLIST, VIDEO_PLAYLIST
}