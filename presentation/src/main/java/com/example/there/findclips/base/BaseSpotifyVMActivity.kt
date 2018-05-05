package com.example.there.findclips.base

import android.arch.lifecycle.Observer
import com.example.there.findclips.util.saveAccessToken

abstract class BaseSpotifyVMActivity<T : BaseSpotifyViewModel> : BaseVMActivity<T>() {

    override fun setupObservers() {
        super.setupObservers()

        viewModel.accessTokenLiveData.observe(this, Observer { accessToken ->
            accessToken?.let {
                saveAccessToken(it)
            }
        })
    }
}