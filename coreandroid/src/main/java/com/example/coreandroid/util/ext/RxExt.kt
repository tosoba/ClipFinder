package com.example.coreandroid.util.ext

import com.example.coreandroid.lifecycle.DisposablesComponent
import io.reactivex.disposables.Disposable

fun Disposable.disposeWith(disposablesComponent: DisposablesComponent) = disposablesComponent.add(this)