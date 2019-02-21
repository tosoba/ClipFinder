package com.example.there.findclips.util.ext

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.there.findclips.base.fragment.BaseNavHostFragment
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.main.controller.*
import com.spotify.sdk.android.player.ConnectionStateCallback
import java.util.*

val Fragment.appCompatActivity: AppCompatActivity?
    get() = activity as? AppCompatActivity

val Fragment.slidingPanelController: SlidingPanelController?
    get() = activity as? SlidingPanelController

val Fragment.videoPlaylistController: VideoPlaylistController?
    get() = activity as? VideoPlaylistController

val Fragment.spotifyPlayerController: SpotifyPlayerController?
    get() = activity as? SpotifyPlayerController

val Fragment.youtubePlayerController: YoutubePlayerController?
    get() = activity as? YoutubePlayerController

val Fragment.spotifyTrackChangeHandler: SpotifyTrackChangeHandler?
    get() = activity as? SpotifyTrackChangeHandler

val Fragment.backPressedWithNoPreviousStateHandler: BackPressedWithNoPreviousStateHandler?
    get() = activity as? BackPressedWithNoPreviousStateHandler

val Fragment.spotifyLoginController: SpotifyLoginController?
    get() = activity as? SpotifyLoginController

val Fragment.connectivitySnackbarHost: ConnectivitySnackbarHost?
    get() = activity as? ConnectivitySnackbarHost

val Fragment.connectionStateCallback: ConnectionStateCallback?
    get() = activity as? ConnectionStateCallback

val Fragment.navigationDrawerController: NavigationDrawerController?
    get() = activity as? NavigationDrawerController

val Fragment.toolbarController: ToolbarController?
    get() = activity as? ToolbarController

fun <I : Parcelable> BaseSpotifyListFragment<I>.putArguments(
        mainHintText: String,
        additionalHintText: String,
        items: ArrayList<I>?,
        shouldShowHeader: Boolean
) {
    val args = Bundle().apply {
        putString(BaseSpotifyListFragment.EXTRA_MAIN_HINT, mainHintText)
        putString(BaseSpotifyListFragment.EXTRA_ADDITIONAL_HINT, additionalHintText)
        items?.let { putParcelableArrayList(BaseSpotifyListFragment.EXTRA_ITEMS, it) }
        putBoolean(BaseSpotifyListFragment.EXTRA_SHOULD_SHOW_HEADER, shouldShowHeader)
    }
    arguments = args
}

val Fragment.navHostFragment: BaseNavHostFragment?
    get() {
        var fragment: Fragment? = this
        while (fragment?.parentFragment?.parentFragment != null) {
            fragment = fragment.parentFragment
        }
        return if (fragment != null && fragment is BaseNavHostFragment) fragment else null
    }