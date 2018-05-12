package com.example.there.findclips.base

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.there.domain.entities.spotify.AccessTokenEntity
import com.example.there.domain.usecases.spotify.GetAccessToken
import com.example.there.findclips.SpotifyClient
import com.example.there.findclips.util.messageOrDefault
import retrofit2.HttpException

open class BaseSpotifyViewModel(private val getAccessToken: GetAccessToken) : BaseViewModel() {

    val accessTokenLiveData: MutableLiveData<AccessTokenEntity> = MutableLiveData()

    private var accessTokenLoading = false
    protected fun loadAccessToken(onAccessTokenLoaded: (AccessTokenEntity) -> Unit) {
        if (!accessTokenLoading) {
            clearDisposables()
            accessTokenLoading = true
            addDisposable(getAccessToken.execute(SpotifyClient.id, SpotifyClient.secret)
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
            if (t.code() == 401) { // Unauthorized
                loadAccessToken { onErrorsResolved(it) }
            }
        } else {
            Log.e("ERROR", t.messageOrDefault("Unknown error."))
        }
    }
}