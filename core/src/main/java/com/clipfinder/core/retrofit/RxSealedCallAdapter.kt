package com.clipfinder.core.retrofit

import com.clipfinder.core.model.NetworkResponse
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type

internal class RxSealedCallAdapter<T : Any, U : Any>(
    private val successBodyType: Type,
    private val delegateAdapter: CallAdapter<T, Observable<T>>,
    private val errorConverter: Converter<ResponseBody, U>,
    private val isFlowable: Boolean,
    private val isSingle: Boolean,
    private val isMaybe: Boolean
) : CallAdapter<T, Any> {

    override fun adapt(call: Call<T>): Any =
        delegateAdapter
            .adapt(call)
            .flatMap { Observable.just<NetworkResponse<T, U>>(NetworkResponse.Success(it)) }
            .onErrorResumeNext(
                Function<Throwable, Observable<NetworkResponse<T, U>>> { throwable ->
                    when (throwable) {
                        is HttpException -> {
                            val error = throwable.response()?.errorBody()
                            val errorBody =
                                when {
                                    error == null -> null
                                    error.contentLength() == 0L -> null
                                    else ->
                                        try {
                                            errorConverter.convert(error)
                                        } catch (e: Exception) {
                                            return@Function Observable.just(
                                                NetworkResponse.NetworkError(
                                                    IOException(
                                                        "Couldn't deserialize error body: ${error.string()}",
                                                        e
                                                    )
                                                )
                                            )
                                        }
                                }
                            val serverError =
                                NetworkResponse.ServerError(errorBody, throwable.response()?.code())
                            Observable.just(serverError)
                        }
                        is IOException -> Observable.just(NetworkResponse.NetworkError(throwable))
                        else -> throw throwable
                    }
                }
            )
            .run {
                when {
                    isFlowable -> toFlowable(BackpressureStrategy.LATEST)
                    isSingle -> singleOrError()
                    isMaybe -> singleElement()
                    else -> this
                }
            }

    override fun responseType(): Type = successBodyType
}
