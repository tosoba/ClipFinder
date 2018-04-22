package com.example.there.findclips.base

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class BaseMainFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setTitle()
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
    protected abstract fun initViewModel()
    protected abstract fun setupObservers()
    protected abstract fun releaseComponent()

    private fun setTitle() = tag?.let { activity?.title = it }
}