package com.example.there.findclips.util.ext

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup

fun <T : ViewDataBinding> ViewGroup.makeItemBinding(
        @LayoutRes layoutId: Int
): T = DataBindingUtil.inflate(
        LayoutInflater.from(context),
        layoutId,
        this,
        false
) as T