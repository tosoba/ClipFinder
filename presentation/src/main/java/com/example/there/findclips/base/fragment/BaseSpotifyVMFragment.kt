package com.example.there.findclips.base.fragment

import android.arch.lifecycle.Observer
import com.example.there.data.preferences.AppPreferences
import com.example.there.findclips.base.vm.BaseSpotifyViewModel
import javax.inject.Inject

abstract class BaseSpotifyVMFragment<T : BaseSpotifyViewModel> (
        vmClass: Class<T>
) : BaseVMFragment<T>(vmClass) {

    @Inject
    lateinit var preferenceHelper: AppPreferences

    override fun setupObservers() {
        super.setupObservers()

        viewModel.accessTokenLiveData.observe(this, Observer { accessToken ->
            accessToken?.let { preferenceHelper.accessToken = it }
        })
    }
}