package com.example.there.findclips.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.there.findclips.util.saveAccessToken

abstract class BaseMainFragment<T : BaseViewModel> : Fragment() {

    protected lateinit var viewModel: T

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle()
        setupObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        initViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseComponent()
    }

    protected abstract fun initComponent()

    protected abstract fun releaseComponent()

    protected abstract fun initViewModel()

    protected open fun setupObservers() {
        viewModel.accessTokenLiveData.observe(this, Observer { accessToken ->
            accessToken?.let {
                activity?.saveAccessToken(it)
            }
        })
    }

    private fun setTitle() = tag?.let { activity?.title = it }
}