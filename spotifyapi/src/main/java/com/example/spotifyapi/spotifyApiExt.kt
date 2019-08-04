package com.example.spotifyapi

import com.example.core.retrofit.NetworkResponse
import com.example.core.util.Optional
import com.example.spotifyapi.service.SpotifyRestAction
import com.example.spotifyapi.service.models.BadRequestException
import com.example.spotifyapi.service.models.SpotifyCategory
import com.example.spotifyapi.service.models.SpotifyImage
import com.example.there.domain.entity.spotify.CategoryEntity
import io.reactivex.Single
import java.io.IOException

//TODO: maybe think about taking advantage of rate limit info in SpotifyRatelimitedException somehow

fun <A : SpotifyRestAction<T?>, T : Any> A.executeSingleNullable(): Single<NetworkResponse<T, BadRequestException>> = Single
        .just(Optional.of(complete()))
        .map<NetworkResponse<T, BadRequestException>> {
            when (it) {
                is Optional.Some<T> -> NetworkResponse.Success(it.value)
                else -> throw IllegalStateException("Api returned a null object.")
            }
        }
        .wrapErrors()

fun <A : SpotifyRestAction<T>, T : Any> A.executeSingle(): Single<NetworkResponse<T, BadRequestException>> = Single
        .just(complete())
        .map<NetworkResponse<T, BadRequestException>> { NetworkResponse.Success(it) }
        .wrapErrors()

private fun <T : Any> Single<NetworkResponse<T, BadRequestException>>.wrapErrors() = onErrorReturn { throwable ->
    when (throwable) {
        is BadRequestException -> NetworkResponse.ServerError(throwable, throwable.statusCode)
        is IOException -> NetworkResponse.NetworkError(throwable)
        else -> NetworkResponse.DifferentError(throwable)
    }
}

val List<SpotifyImage>.firstIconUrlOrDefault: String
    get() = getOrNull(0)?.url ?: DEFAULT_ICON_URL

private const val DEFAULT_ICON_URL = "https://t.scdn.co/media/derived/r-b-274x274_fd56efa72f4f63764b011b68121581d8_0_0_274_274.jpg"

val SpotifyCategory.domain: CategoryEntity get() = CategoryEntity(_id, name, icons.firstIconUrlOrDefault)