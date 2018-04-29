package com.example.there.findclips.base

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecase.spotify.AccessTokenUseCase
import com.example.there.findclips.util.messageOrDefault
import retrofit2.HttpException

open class BaseSpotifyViewModel(private val accessTokenUseCase: AccessTokenUseCase): BaseViewModel() {

    val accessTokenLiveData: MutableLiveData<AccessTokenEntity> = MutableLiveData()

    private var accessTokenLoading = false
    protected fun loadAccessToken(onAccessTokenLoaded: (AccessTokenEntity) -> Unit) {
        if (!accessTokenLoading) {
            clearDisposables()
            accessTokenLoading = true
            addDisposable(accessTokenUseCase.getAccessToken(CLIENT_ID, CLIENT_SECRET)
                    .doFinally { accessTokenLoading = false }
                    .subscribe({
                        accessTokenLiveData.value = it
                        onAccessTokenLoaded(it)
                    }, {
                        errorState.value = it
                        Log.e(javaClass.name, it.messageOrDefault("Error loading access token."))
                    }))
        }
    }

    protected fun handleErrors(t: Throwable, onErrorsResolved: (AccessTokenEntity) -> Unit) {
        if (t is HttpException) {
            // Unauthorized
            if (t.code() == 401) {
                loadAccessToken { onErrorsResolved(it) }
            }
        } else {
            Log.e("ERROR", t.messageOrDefault("Unknown error."))
        }
    }

    companion object {
        private const val CLIENT_ID = "6dc5e6590b8b48c5bd73a64f6c206d8a"
        private const val CLIENT_SECRET = "d5c30ea11b90401980c6ca37dc0512ba"
    }
}