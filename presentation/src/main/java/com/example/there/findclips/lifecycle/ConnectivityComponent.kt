package com.example.there.findclips.lifecycle

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Intent
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.example.there.findclips.R
import com.example.there.findclips.util.ext.dpToPx
import com.example.there.findclips.util.ext.showSnackbarWithBottomMargin
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ConnectivityComponent(
        private val activity: Activity,
        private val isDataLoaded: () -> Boolean,
        private val parentView: View,
        private val reloadData: (() -> Unit)? = null,
        private val shouldShowSnackbarWithBottomMargin: Boolean = true
) : LifecycleObserver {

    private var internetDisposable: Disposable? = null
    private var connectionInterrupted = false
    private var lastConnectionStatus: Boolean? = null

    private var snackbar: Snackbar? = null
    private var isSnackbarShowing = false

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun observeInternetConnectivity() {
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isConnectedToInternet ->
                    lastConnectionStatus = isConnectedToInternet
                    handleConnectionStatus(isConnectedToInternet)
                }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun clear() {
        snackbar?.dismiss()
        snackbar = null
        safelyDispose(internetDisposable)
    }

    private fun handleConnectionStatus(isConnectedToInternet: Boolean) {
        if (!isConnectedToInternet) {
            connectionInterrupted = true
            if (!isSnackbarShowing) {
                showNoConnectionSnackbar()
            }
        } else {
            if (connectionInterrupted) {
                connectionInterrupted = false
                if (!isDataLoaded() && reloadData != null) {
                    reloadData.invoke()
                }
            }

            isSnackbarShowing = false
            snackbar?.dismiss()
        }
    }

    private fun safelyDispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun showNoConnectionSnackbar() {
        snackbar = Snackbar
                .make(parentView, "No internet connection.", Snackbar.LENGTH_LONG)
                .setAction("SETTINGS") {
                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                    activity.startActivity(settingsIntent)
                }
                .setCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_SWIPE) {
                            showNoConnectionSnackbar()
                        }
                    }
                })
                .setActionTextColor(ContextCompat.getColor(activity, R.color.colorAccent))

        val textView = snackbar?.view?.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView?.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent))
        snackbar?.duration = Snackbar.LENGTH_INDEFINITE

        if (!shouldShowSnackbarWithBottomMargin) {
            snackbar?.show()
        } else {
            snackbar?.showSnackbarWithBottomMargin(activity.dpToPx(50f).toInt())
        }
    }
}

