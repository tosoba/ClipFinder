package com.example.there.findclips.base.fragment

import android.arch.lifecycle.Observer
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.util.saveAccessToken

abstract class BaseSpotifyVMFragment<T : BaseSpotifyViewModel>: BaseVMFragment<T>() {

    override fun setupObservers() {
        super.setupObservers()

        viewModel.accessTokenLiveData.observe(this, Observer { accessToken ->
            accessToken?.let {
                activity?.saveAccessToken(it)
            }
        })
    }
}