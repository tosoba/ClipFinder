package com.example.coreandroid.base.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.core.ext.messageOrDefault
import com.example.coreandroid.base.vm.BaseViewModel
import com.example.coreandroid.di.vm.ViewModelFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseVMActivity<T : BaseViewModel> constructor(
        private val vmClass: Class<T>
) : DaggerAppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<androidx.fragment.app.Fragment>

    @Inject
    lateinit var factory: ViewModelFactory

    protected lateinit var viewModel: T

    protected val viewModelInitialized: Boolean
        get() = ::viewModel.isInitialized

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        setupObservers()
    }

    override fun supportFragmentInjector(): AndroidInjector<androidx.fragment.app.Fragment> = fragmentDispatchingAndroidInjector

    protected open fun T.onInitialized() = Unit

    protected open fun setupObservers() {
        viewModel.errorState.observe(this, Observer { error ->
            error?.let { Log.e(javaClass.name ?: "Error", it.messageOrDefault()) }
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, factory).get(vmClass)
        viewModel.onInitialized()
    }
}