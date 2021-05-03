package com.clipfinder.spotify.api.model

import com.squareup.moshi.Json

data class ErrorResponse(val error: ErrorObject)

data class ErrorObject(override val message: String, val status: Int) : Throwable(message)

data class AuthErrorObject(
    val error: String,
    @Json(name = "error_description") val errorDescription: String?
) : Throwable("$error : ${errorDescription ?: "No description."}")
