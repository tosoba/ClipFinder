package com.example.there.findclips.base

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.there.findclips.util.messageOrDefault
import com.example.there.findclips.util.saveAccessToken

abstract class BaseVMActivity<T : BaseViewModel> : AppCompatActivity() {

    protected lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
        initViewModel()
        setupObservers()
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
                Toast.makeText(this, it.messageOrDefault(), Toast.LENGTH_LONG).show()
            }
        })
    }
}