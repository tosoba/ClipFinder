package com.clipfinder.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.airbnb.mvrx.withState
import com.clipfinder.core.android.base.viewmodel.MvRxViewModel
import com.clipfinder.core.android.spotify.auth.SpotifyAutoAuth
import com.clipfinder.core.android.spotify.auth.SpotifyManualAuth
import com.clipfinder.core.android.spotify.model.User
import com.clipfinder.core.android.spotify.preferences.SpotifyPreferences
import com.clipfinder.core.android.util.ext.retryLoadOnNetworkAvailable
import com.clipfinder.core.ext.mapData
import com.clipfinder.core.model.Empty
import com.clipfinder.core.model.Resource
import com.clipfinder.core.model.invoke
import com.clipfinder.core.spotify.usecase.GetCurrentUser
import io.reactivex.Single
import org.koin.android.ext.android.get
import timber.log.Timber

class MainViewModel(
    initialState: MainState,
    private val getCurrentUser: GetCurrentUser,
    private val spotifyManualAuth: SpotifyManualAuth,
    private val spotifyAutoAuth: SpotifyAutoAuth,
    private val spotifyPreferences: SpotifyPreferences,
    context: Context
) : MvRxViewModel<MainState>(initialState) {
    init {
        handleConnectivityChanges(context)
        observePrivateAccessToken()
    }

    internal val privateAccessToken: String?
        get() = spotifyPreferences.privateAccessToken

    internal val isPrivateAuthorized: Boolean
        get() = withState(this, MainState::isPrivateAuthorized)

    internal var onLoginSuccessful: (() -> Unit)? = null

    private fun loadCurrentUser() = load(MainState::user, getCurrentUser::intoState) { copy(user = it) }
    fun setMainContent(mainContent: MainContent) = setState { copy(mainContent = mainContent) }
    fun setPlayerState(playerState: PlayerState) = setState { copy(playerState = playerState) }

    fun onLoginActivityResult(intent: Intent, onError: (Throwable) -> Unit) {
        spotifyManualAuth.sendTokenRequestFrom(intent)
            .subscribe({}, onError)
            .disposeOnClear()
    }

    fun onLoggedOut() {
        setState { copy(user = Empty, isPrivateAuthorized = false) }
        spotifyPreferences.authState = null
    }

    private fun observePrivateAccessToken() {
        spotifyAutoAuth.authorizePrivate()
            .onErrorComplete()
            .andThen(spotifyPreferences.isPrivateAuthorizedObservable)
            .subscribe(
                {
                    setState { copy(isPrivateAuthorized = it) }
                    if (isPrivateAuthorized) loadCurrentUser()
                },
                Timber::e
            )
            .disposeOnClear()
    }

    @SuppressLint("MissingPermission")
    private fun handleConnectivityChanges(context: Context) {
        context.handleConnectivityChanges { (_, _, _, user, isPrivateAuthorized) ->
            if (isPrivateAuthorized && user.retryLoadOnNetworkAvailable) loadCurrentUser()
        }
    }

    companion object : MvRxViewModelFactory<MainViewModel, MainState> {
        override fun create(
            viewModelContext: ViewModelContext, state: MainState
        ): MainViewModel = MainViewModel(
            state,
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.activity.get(),
            viewModelContext.app()
        )
    }
}

private fun GetCurrentUser.intoState(state: MainState): Single<Resource<User>> = this()
    .mapData {
        User(
            it.displayName ?: "Unknown user",
            it.images?.firstOrNull()?.url
                ?: "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"
        )
    }
