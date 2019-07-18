package com.example.coreandroid.base.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.core.ext.messageOrDefault
import com.example.coreandroid.base.vm.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class BaseVMActivity<T : BaseViewModel>(vmClass: KClass<T>) : AppCompatActivity() {

    protected val viewModel: T by viewModel(vmClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onInitialized()
        setupObservers()
    }

    protected open fun T.onInitialized() = Unit

    protected open fun setupObservers() {
        viewModel.errorState.observe(this, Observer { error ->
            error?.let { Log.e(javaClass.name ?: "Error", it.messageOrDefault()) }
        })
    }
}