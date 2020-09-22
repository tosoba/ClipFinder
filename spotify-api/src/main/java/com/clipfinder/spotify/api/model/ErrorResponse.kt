package com.clipfinder.spotify.api.model

data class ErrorResponse(val error: ErrorObject)

data class ErrorObject(override val message: String, val status: Int) : Throwable(message)
