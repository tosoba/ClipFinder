package com.example.core.android.model

import java.io.IOException

interface HoldsData<Value> {
    val value: Value
    val status: DataStatus
    val copyWithLoadingInProgress: HoldsData<Value>
    val loadingFailed: Boolean
        get() = status is LoadingFailed<*>

    fun <E> copyWithError(error: E): HoldsData<Value>
}

data class Data<Value>(
    override val value: Value,
    override val status: DataStatus = Initial
) : HoldsData<Value> {

    override val copyWithLoadingInProgress: Data<Value>
        get() = copy(status = Loading)

    override fun <E> copyWithError(error: E): Data<Value> = copy(status = LoadingFailed(error))

    fun copyWithNewValue(value: Value): Data<Value> = copy(
        value = value,
        status = LoadedSuccessfully
    )
}

fun <Holder : HoldsData<Collection<Value>>, Value> Holder.isEmptyAndLastLoadingFailedWithNetworkError(): Boolean {
    val stat = status
    return value.isEmpty() && stat is LoadingFailed<*> && (stat.error == null || stat.error is IOException)
}

data class DataList<Value>(
    override val value: Collection<Value> = emptyList(),
    override val status: DataStatus = Initial
) : HoldsData<Collection<Value>> {

    override val copyWithLoadingInProgress: DataList<Value>
        get() = copy(status = Loading)

    override fun <E> copyWithError(error: E): DataList<Value> = copy(status = LoadingFailed(error))

    fun copyWithNewItems(newItems: Collection<Value>): DataList<Value> = copy(
        value = value + newItems,
        status = LoadedSuccessfully
    )

    fun copyWithNewItems(vararg newItems: Value): DataList<Value> = copy(
        value = value + newItems,
        status = LoadedSuccessfully
    )
}

data class PagedDataList<Value>(
    override val value: Collection<Value> = emptyList(),
    override val status: DataStatus = Initial,
    val offset: Int = 0,
    val totalItems: Int = Integer.MAX_VALUE
) : HoldsData<Collection<Value>> {

    val shouldLoad: Boolean
        get() = status !is Loading && offset < totalItems

    override val copyWithLoadingInProgress: PagedDataList<Value>
        get() = copy(status = Loading)

    override fun <E> copyWithError(error: E): PagedDataList<Value> = copy(
        status = LoadingFailed(error)
    )

    fun copyWithNewItems(newItems: Collection<Value>, offset: Int): PagedDataList<Value> = copy(
        value = value + newItems,
        offset = offset,
        status = LoadedSuccessfully
    )

    fun copyWithNewItems(
        newItems: Collection<Value>, offset: Int, totalItems: Int
    ): PagedDataList<Value> = copy(
        value = value + newItems,
        offset = offset,
        status = LoadedSuccessfully,
        totalItems = totalItems
    )
}

sealed class DataStatus
object Initial : DataStatus()
object Loading : DataStatus()
object LoadedSuccessfully : DataStatus()
data class LoadingFailed<E>(val error: E) : DataStatus()
