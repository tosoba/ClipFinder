package com.clipfinder.spotify.api.models

data class ErrorResponse(val error: ErrorObject)

data class ErrorObject(override val message: String, val status: Int) : Throwable(message)
