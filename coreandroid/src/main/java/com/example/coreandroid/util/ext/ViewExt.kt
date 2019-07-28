package com.example.coreandroid.util.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.squareup.picasso.Picasso
import io.reactivex.disposables.Disposable

fun <T : ViewDataBinding> ViewGroup.makeItemBinding(
        @LayoutRes layoutId: Int
): T = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, this, false)

fun View.loadBackgroundGradient(
        url: String, disposables: DisposablesComponent? = null
): Disposable? = Picasso.with(context)
        .getBitmapSingle(url) { bitmap ->
            bitmap.generateColorGradient {
                background = it
                invalidate()
            }
        }
        .run {
            disposables?.let { disposeWith(it) }; null ?: this
        }