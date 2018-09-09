package com.example.there.findclips.base.activity

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.there.findclips.base.vm.BaseViewModel
import com.example.there.findclips.di.vm.ViewModelFactory
import com.example.there.findclips.util.ext.messageOrDefault
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseVMActivity<T : BaseViewModel> : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

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

    protected abstract fun initViewModel()

    protected open fun setupObservers() {
        viewModel.errorState.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, it.messageOrDefault(), Toast.LENGTH_LONG).show()
            }
        })
    }
}