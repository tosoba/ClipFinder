package com.example.there.findclips.base

import android.arch.lifecycle.Observer
import com.example.there.findclips.util.saveAccessToken

abstract class BaseSpotifyVMFragment<T : BaseSpotifyViewModel>: BaseVMFragment<T>() {

    override fun setupObservers() {
        super.setupObservers()

        mainViewModel.accessTokenLiveData.observe(this, Observer { accessToken ->
            accessToken?.let {
                activity?.saveAccessToken(it)
            }
        })
    }
}