package com.example.coreandroid.lifecycle

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.coreandroid.R
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ConnectivityComponent(
        private val activity: Activity,
        private val isDataLoaded: () -> Boolean,
        private val parentView: View,
        private val reloadData: (() -> Unit)? = null
) : LifecycleObserver {

    private var internetDisposable: Disposable? = null
    private var connectionInterrupted = false
    private var lastConnectionStatus: Boolean? = null

    private var snackbar: Snackbar? = null
    private var isSnackbarShowing = false

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun observeInternetConnectivity() {
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(::handleConnectionStatus)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun clear() {
        snackbar?.dismiss()
        snackbar = null
        safelyDispose(internetDisposable)
    }

    private fun handleConnectionStatus(isConnectedToInternet: Boolean) {
        lastConnectionStatus = isConnectedToInternet
        if (!isConnectedToInternet) {
            connectionInterrupted = true
            if (!isSnackbarShowing) showNoConnectionSnackbar()
        } else {
            if (connectionInterrupted) {
                connectionInterrupted = false
                if (!isDataLoaded() && reloadData != null) reloadData.invoke()
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
                    activity.startActivity(Intent(Settings.ACTION_SETTINGS))
                }
                .setCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if (event == DISMISS_EVENT_SWIPE) showNoConnectionSnackbar()
                    }
                })
                .setActionTextColor(ContextCompat.getColor(activity, R.color.colorAccent))
                .apply {
                    duration = BaseTransientBottomBar.LENGTH_INDEFINITE
                    view.findViewById<TextView>(R.id.snackbar_text)?.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent))
                    show()
                }
    }
}

