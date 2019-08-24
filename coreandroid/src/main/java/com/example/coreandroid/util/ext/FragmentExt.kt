package com.example.coreandroid.util.ext

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.airbnb.mvrx.*
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.activity.IntentProvider
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.base.fragment.BaseNavHostFragment
import com.example.coreandroid.base.fragment.IMainContentFragment
import com.example.coreandroid.base.handler.*
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.spotify.sdk.android.player.ConnectionStateCallback
import java.util.*
import kotlin.reflect.KClass

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

val Fragment.soundCloudPlayerController: SoundCloudPlayerController?
    get() = activity as? SoundCloudPlayerController

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

private inline fun <reified T> Fragment.findAncestorFragmentOfType(): T? {
    var ancestorFragment = parentFragment
    while (ancestorFragment != null) {
        if (ancestorFragment is T) return ancestorFragment
        ancestorFragment = ancestorFragment.parentFragment
    }
    return null
}

val Fragment.navHostFragment: BaseNavHostFragment?
    get() = findAncestorFragmentOfType()

val Fragment.mainContentFragment: IMainContentFragment?
    get() = findAncestorFragmentOfType()

//TODO: use this for navigation
interface NavigationCapable {
    val factory: IFragmentFactory
}

inline fun <T> T.show(
        addToBackStack: Boolean = true, getFragment: IFragmentFactory.() -> Fragment
) where T : Fragment, T : NavigationCapable {
    navHostFragment?.showFragment(factory.getFragment(), addToBackStack)
}

fun Fragment.reloadingConnectivityComponent(
        reloadData: () -> Unit,
        isReloadNeeded: () -> Boolean
): ConnectivityComponent = ConnectivityComponent(object : ConnectivityComponent.Binder {
    override val context: Context
        get() = this@reloadingConnectivityComponent.requireContext()

    override val snackbarParentView: View?
        get() = connectivitySnackbarHost?.snackbarParentView

    override fun shouldReload(): Boolean = isReloadNeeded()

    override fun reload() = reloadData()

    override fun openSettings() {
        activity?.startActivity(Intent(Settings.ACTION_SETTINGS))
    }
})

fun Fragment.enableSpotifyPlayButton(playClicked: SpotifyPlayerController.() -> Unit) {
    mainContentFragment?.enablePlayButton {
        if (spotifyPlayerController?.isPlayerLoggedIn == true) spotifyPlayerController?.playClicked()
        else spotifyLoginController?.let {
            it.showLoginDialog()
            it.onLoginSuccessful = { spotifyPlayerController?.playClicked() }
        }
    }
}

inline fun <T, reified VM : BaseMvRxViewModel<S>, S : MvRxState> T.parentFragmentViewModel(
        viewModelClass: KClass<VM> = VM::class,
        crossinline keyFactory: () -> String = { viewModelClass.java.name }
) where T : Fragment, T : MvRxView = lifecycleAwareLazy(this) {
    val factory = MvRxFactory { throw IllegalStateException("ViewModel for ${requireActivity()}[${keyFactory()}] does not exist yet!") }
    ViewModelProviders.of(parentFragment!!, factory).get(keyFactory(), viewModelClass.java)
            .apply { subscribe(this@parentFragmentViewModel, subscriber = { postInvalidate() }) }
}