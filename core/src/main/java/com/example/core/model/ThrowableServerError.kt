package com.example.core.model

class ThrowableServerError(
    val error: NetworkResponse.ServerError<*>
) : Throwable("${error.code ?: "No error code"} : ${error.body ?: "No error body."}")