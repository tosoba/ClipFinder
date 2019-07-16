package com.example.coreandroid.util.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> ViewGroup.makeItemBinding(
        @LayoutRes layoutId: Int
): T = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, this, false)