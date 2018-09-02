package com.example.there.domain.common

import io.reactivex.ObservableTransformer
import io.reactivex.SingleTransformer

abstract class SymmetricObservableTransformer<T> : ObservableTransformer<T, T>

abstract class SymmetricSingleTransformer<T> : SingleTransformer<T, T>