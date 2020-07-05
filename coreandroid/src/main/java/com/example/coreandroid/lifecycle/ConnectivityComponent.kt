package com.example.coreandroid.lifecycle

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.core.ext.disposeIfNeeded
import com.example.coreandroid.R
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class ConnectivityComponent(private val binder: Binder) : LifecycleObserver {

    private var internetDisposable: Disposable? = null
    private var connectionInterrupted = false
    private var lastConnectionStatus: Status = Status.INITIAL

    private var snackbar: Snackbar? = null
    private var isSnackbarShowing = false

    @SuppressLint("MissingPermission")
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun observeInternetConnectivity() {
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(::handleConnectionStatus, Timber::e)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun cleanUp() {
        snackbar?.dismiss()
        internetDisposable?.disposeIfNeeded()
    }

    private fun handleConnectionStatus(isConnectedToInternet: Boolean) {
        lastConnectionStatus = if (isConnectedToInternet) {
            Status.CONNECTED
        } else {
            Status.NOT_CONNECTED
        }

        if (!isConnectedToInternet) {
            connectionInterrupted = true
            if (!isSnackbarShowing) showNoConnectionSnackbar()
        } else {
            if (connectionInterrupted) {
                connectionInterrupted = false
                if (binder.shouldReload()) binder.reload()
            }
            isSnackbarShowing = false
            snackbar?.dismiss()
        }
    }

    private fun showNoConnectionSnackbar() {
        binder.snackbarParentView?.let {
            snackbar = Snackbar
                .make(it, "No internet connection.", Snackbar.LENGTH_LONG)
                .setAction("SETTINGS") {
                    binder.openSettings()
                }
                .setCallback(object : Snackbar.Callback() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        if (event == DISMISS_EVENT_SWIPE) showNoConnectionSnackbar()
                    }
                })
                .setActionTextColor(ContextCompat.getColor(binder.context, R.color.colorAccent))
                .apply {
                    duration = BaseTransientBottomBar.LENGTH_INDEFINITE
                    view.findViewById<TextView>(R.id.snackbar_text)
                        ?.setTextColor(ContextCompat.getColor(binder.context, R.color.colorAccent))
                    show()
                }
        }
    }

    enum class Status {
        INITIAL, CONNECTED, NOT_CONNECTED
    }

    interface Binder {
        val context: Context
        val snackbarParentView: View?
        fun shouldReload(): Boolean
        fun reload()
        fun openSettings()
    }
}
