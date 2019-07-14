package com.example.coreandroid.base.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import com.example.core.ext.messageOrDefault
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.di.Injectable
import com.example.coreandroid.di.vm.ViewModelFactory
import javax.inject.Inject

abstract class BaseVMFragment<T : BaseViewModel> constructor(
        private val vmClass: Class<T>
) : Fragment(), Injectable {

    @Inject
    lateinit var factory: ViewModelFactory

    protected lateinit var viewModel: T

    protected val viewModelInitialized: Boolean
        get() = ::viewModel.isInitialized

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    protected open fun T.onInitialized() = Unit

    protected open fun setupObservers() {
        viewModel.errorState.observe(this, Observer { error ->
            error?.let { Log.e(javaClass.name ?: "BaseVMFragment error: ", it.messageOrDefault()) }
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(vmClass)
        viewModel.onInitialized()
    }
}