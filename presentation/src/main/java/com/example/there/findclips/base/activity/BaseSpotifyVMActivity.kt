package com.example.there.findclips.base.activity

import android.arch.lifecycle.Observer
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.util.ext.saveAccessToken

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