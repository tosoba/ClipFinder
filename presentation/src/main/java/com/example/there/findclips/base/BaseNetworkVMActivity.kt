package com.example.there.findclips.base

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.widget.Toast
import com.example.there.findclips.R
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class BaseNetworkVMActivity<T : BaseViewModel> : BaseVMActivity<T>() {
    private var internetDisposable: Disposable? = null

    private var connectionInterrupted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let { connectionInterrupted = it.getBoolean(KEY_SAVED_CONNECTION_INTERRUPTED) }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(KEY_SAVED_CONNECTION_INTERRUPTED, connectionInterrupted)
    }

    override fun onResume() {
        super.onResume()

        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isConnectedToInternet ->
                    handleConnectionStatus(isConnectedToInternet)
                }
    }

    private fun handleConnectionStatus(isConnectedToInternet: Boolean) {
        if (!isConnectedToInternet) {
            connectionInterrupted = true
            if (!isDialogShowing) {
                showNoConnectionDialog()
            }
        } else {
            if (connectionInterrupted) {
                connectionInterrupted = false
                if (!isDataLoaded()) {
                    reloadData()
                }
            }

            isDialogShowing = false
            dialog?.dismiss()
        }
    }

    override fun onPause() {
        super.onPause()
        safelyDispose(internetDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog = null
    }

    private fun safelyDispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private var isDialogShowing = false

    private var dialog: AlertDialog? = null

    private fun showNoConnectionDialog() {
        dialog = AlertDialog.Builder(this)
                .setMessage(getString(R.string.no_internet_connection))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.settings), { dialog, _ ->
                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)
                    startActivity(settingsIntent)
                    dialog.dismiss()
                    isDialogShowing = false
                })
                .setNegativeButton(getString(R.string.cancel), { dialog, _ ->
                    Toast.makeText(this, R.string.internet_connection_is_needed, Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                    isDialogShowing = false
                })
                .create()
                .apply {
                    if (!isDialogShowing) {
                        isDialogShowing = true
                        show()
                    }
                }
    }

    abstract fun isDataLoaded(): Boolean

    abstract fun reloadData()

    companion object {
        private const val KEY_SAVED_CONNECTION_INTERRUPTED = "KEY_SAVED_CONNECTION_INTERRUPTED"
    }
}