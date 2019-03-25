package com.example.coreandroid.util.ext

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.example.coreandroid.base.activity.IntentProvider
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.base.fragment.BaseNavHostFragment
import com.example.coreandroid.base.handler.*
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

val Fragment.backPressedWithNoPreviousStateController: BackPressedWithNoPreviousStateController?
    get() = activity as? BackPressedWithNoPreviousStateController

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

val Fragment.intentProvider: IntentProvider?
    get() = activity as? IntentProvider

fun <I : Parcelable> BaseListFragment<I>.putArguments(
        mainHintText: String,
        additionalHintText: String,
        items: ArrayList<I>?,
        shouldShowHeader: Boolean
) {
    val args = Bundle().apply {
        putString(BaseListFragment.EXTRA_MAIN_HINT, mainHintText)
        putString(BaseListFragment.EXTRA_ADDITIONAL_HINT, additionalHintText)
        items?.let { putParcelableArrayList(BaseListFragment.EXTRA_ITEMS, it) }
        putBoolean(BaseListFragment.EXTRA_SHOULD_SHOW_HEADER, shouldShowHeader)
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