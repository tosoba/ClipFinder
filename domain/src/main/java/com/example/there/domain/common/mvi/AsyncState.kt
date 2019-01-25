package com.example.there.domain.common.mvi

data class AsyncState<Value : Any>(val isLoading: Boolean, val value: Value)