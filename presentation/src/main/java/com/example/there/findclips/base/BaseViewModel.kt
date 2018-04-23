package com.example.there.findclips.base

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.there.domain.usecase.AccessTokenUseCase
import com.example.there.findclips.util.SingleLiveEvent
import com.example.there.findclips.util.orDefault
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

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

    val accessTokenLiveData: MutableLiveData<String> = MutableLiveData()
    val accessTokenError: SingleLiveEvent<Throwable?> = SingleLiveEvent()

    protected fun loadAccessToken(onSuccess: (String) -> Unit) {
        addDisposable(accessTokenUseCase.getAccessToken(CLIENT_ID, CLIENT_SECRET)
                .subscribe({
                    accessTokenLiveData.value = it
                    onSuccess(it)
                }, {
                    accessTokenError.value = it
                    Log.e(javaClass.name, it.message.orDefault("Error loading access token."))
                }))
    }

    companion object {
        private const val CLIENT_ID = "6dc5e6590b8b48c5bd73a64f6c206d8a"
        private const val CLIENT_SECRET = "d5c30ea11b90401980c6ca37dc0512ba"
    }
}