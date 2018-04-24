package com.example.there.findclips.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.there.domain.entities.AccessTokenEntity
import com.example.there.domain.usecase.AccessTokenUseCase
import com.example.there.findclips.util.SingleLiveEvent
import com.example.there.findclips.util.messageOrDefault
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

open class BaseViewModel(private val accessTokenUseCase: AccessTokenUseCase) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    protected fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun clearDisposables() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        clearDisposables()
    }

    val accessTokenLiveData: MutableLiveData<AccessTokenEntity> = MutableLiveData()
    val errorState: SingleLiveEvent<Throwable?> = SingleLiveEvent()

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