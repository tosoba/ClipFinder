package com.example.there.findclips.base.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.di.vm.ViewModelFactory
import com.example.there.findclips.util.ext.messageOrDefault
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseVMActivity<T : BaseViewModel> constructor(
        private val vmClass: Class<T>
) : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

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

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

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