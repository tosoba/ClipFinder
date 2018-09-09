package com.example.there.findclips.base.fragment

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.di.Injectable
import com.example.there.findclips.di.vm.ViewModelFactory
import com.example.there.findclips.util.ext.messageOrDefault
import javax.inject.Inject

abstract class BaseVMFragment<T : BaseViewModel> : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory

    protected lateinit var viewModel: T

    protected val viewModelInitialized: Boolean
        get() = ::viewModel.isInitialized

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    protected abstract fun initViewModel()

    protected open fun setupObservers() {
        viewModel.errorState.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this.activity, it.messageOrDefault(), Toast.LENGTH_LONG).show()
            }
        })
    }
}