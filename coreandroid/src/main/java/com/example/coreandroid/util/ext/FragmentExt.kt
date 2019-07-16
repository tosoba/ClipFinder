package com.example.coreandroid.util.ext

import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.example.coreandroid.base.activity.IntentProvider
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.base.fragment.BaseNavHostFragment
import com.example.coreandroid.base.handler.*
import com.spotify.sdk.android.player.ConnectionStateCallback
import java.util.*

val androidx.fragment.app.Fragment.appCompatActivity: AppCompatActivity?
    get() = activity as? AppCompatActivity

val androidx.fragment.app.Fragment.slidingPanelController: SlidingPanelController?
    get() = activity as? SlidingPanelController

val androidx.fragment.app.Fragment.videoPlaylistController: VideoPlaylistController?
    get() = activity as? VideoPlaylistController

val androidx.fragment.app.Fragment.spotifyPlayerController: SpotifyPlayerController?
    get() = activity as? SpotifyPlayerController

val androidx.fragment.app.Fragment.youtubePlayerController: YoutubePlayerController?
    get() = activity as? YoutubePlayerController

val androidx.fragment.app.Fragment.soundCloudPlayerController: SoundCloudPlayerController?
    get() = activity as? SoundCloudPlayerController

val androidx.fragment.app.Fragment.spotifyTrackChangeHandler: SpotifyTrackChangeHandler?
    get() = activity as? SpotifyTrackChangeHandler

val androidx.fragment.app.Fragment.backPressedWithNoPreviousStateController: BackPressedWithNoPreviousStateController?
    get() = activity as? BackPressedWithNoPreviousStateController

val androidx.fragment.app.Fragment.spotifyLoginController: SpotifyLoginController?
    get() = activity as? SpotifyLoginController

val androidx.fragment.app.Fragment.connectivitySnackbarHost: ConnectivitySnackbarHost?
    get() = activity as? ConnectivitySnackbarHost

val androidx.fragment.app.Fragment.connectionStateCallback: ConnectionStateCallback?
    get() = activity as? ConnectionStateCallback

val androidx.fragment.app.Fragment.navigationDrawerController: NavigationDrawerController?
    get() = activity as? NavigationDrawerController

val androidx.fragment.app.Fragment.toolbarController: ToolbarController?
    get() = activity as? ToolbarController

val androidx.fragment.app.Fragment.intentProvider: IntentProvider?
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

val androidx.fragment.app.Fragment.navHostFragment: BaseNavHostFragment?
    get() {
        var fragment: androidx.fragment.app.Fragment? = this
        while (fragment?.parentFragment?.parentFragment != null) {
            fragment = fragment.parentFragment
        }
        return if (fragment != null && fragment is BaseNavHostFragment) fragment else null
    }