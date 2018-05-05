package com.example.there.findclips.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.example.there.findclips.util.messageOrDefault

abstract class BaseVMFragment<T : BaseViewModel> : Fragment() {

    protected lateinit var viewModel: T

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
        viewModel.errorState.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this.activity, it.messageOrDefault(), Toast.LENGTH_LONG).show()
            }
        })
    }
}