package com.example.there.findclips.base.fragment

import android.arch.lifecycle.Observer
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import com.example.there.findclips.util.ext.saveAccessToken

abstract class BaseSpotifyVMFragment<T : BaseSpotifyViewModel> (
        vmClass: Class<T>
) : BaseVMFragment<T>(vmClass) {

    override fun setupObservers() {
        super.setupObservers()

        viewModel.accessTokenLiveData.observe(this, Observer { accessToken ->
            accessToken?.let {
                activity?.saveAccessToken(it)
            }
        })
    }
}