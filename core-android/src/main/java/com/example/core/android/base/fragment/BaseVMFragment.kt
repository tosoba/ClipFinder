package com.example.core.android.base.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.clipfinder.core.ext.messageOrDefault
import com.example.core.android.base.vm.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.reflect.KClass

abstract class BaseVMFragment<T : BaseViewModel>(vmClass: KClass<T>) : Fragment() {

    protected val viewModel: T by viewModel(vmClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onInitialized()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupObservers()
    }

    protected open fun T.onInitialized() = Unit

    protected open fun setupObservers() {
        viewModel.errorState.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Timber.e(javaClass.name ?: "BaseVMFragment error: ", it.messageOrDefault())
            }
        })
    }
}
