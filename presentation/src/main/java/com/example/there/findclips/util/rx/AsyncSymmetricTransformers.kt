package com.example.there.findclips.util.rx

import com.example.there.domain.common.SymmetricObservableTransformer
import com.example.there.domain.common.SymmetricSingleTransformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AsyncSymmetricObservableTransformer<T> : SymmetricObservableTransformer<T>() {
    override fun apply(
            upstream: Observable<T>
    ): ObservableSource<T> = upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

class AsyncSymmetricSingleTransformer<T> : SymmetricSingleTransformer<T>() {
    override fun apply(
            upstream: Single<T>
    ): SingleSource<T> = upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}