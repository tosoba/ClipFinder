package com.clipfinder.main

import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import com.airbnb.mvrx.MvRxState
import com.clipfinder.core.android.spotify.model.User
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Loadable
import com.google.android.material.navigation.NavigationView
import com.sothree.slidinguppanel.SlidingUpPanelLayout

class MainView(
    val onDrawerNavigationItemSelectedListener: NavigationView.OnNavigationItemSelectedListener,
    val fadeOnClickListener: View.OnClickListener,
    val slideListener: SlidingUpPanelLayout.PanelSlideListener,
    val initialSlidePanelState: SlidingUpPanelLayout.PanelState,
    val onFavouriteBtnClickListener: View.OnClickListener,
    val pagerAdapter: FragmentStatePagerAdapter,
    val offScreenPageLimit: Int
)

data class MainState(
    val playerState: PlayerState = PlayerState.VIDEO,
    val itemFavouriteState: Boolean = false,
    val mainContent: MainContent = MainContent.SPOTIFY,
    val user: Loadable<User> = Empty,
    val isPrivateAuthorized: Boolean = false
) : MvRxState

enum class PlayerState {
    VIDEO,
    TRACK,
    ALBUM,
    PLAYLIST,
    VIDEO_PLAYLIST,
    SOUND_CLOUD_TRACK
}

enum class MainContent {
    SPOTIFY,
    SOUNDCLOUD
}
