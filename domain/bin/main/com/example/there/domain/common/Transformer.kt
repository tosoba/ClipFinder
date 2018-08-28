package com.example.there.domain.common

import io.reactivex.ObservableTransformer

abstract class Transformer<T> : ObservableTransformer<T, T>