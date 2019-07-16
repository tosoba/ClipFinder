package com.example.coreandroid.base.fragment

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.core.ext.messageOrDefault
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.di.vm.ViewModelFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseVMFragment<T : BaseViewModel> constructor(
        private val vmClass: Class<T>
) : DaggerFragment() {

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