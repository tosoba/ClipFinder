package com.example.there.findclips.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.example.there.findclips.util.messageOrDefault
import com.example.there.findclips.util.saveAccessToken

abstract class BaseVMFragment<T : BaseViewModel> : Fragment() {

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

        viewModel.errorState.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this.activity, it.messageOrDefault(), Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setTitle() = tag?.let { activity?.title = it }
}