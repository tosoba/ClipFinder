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

val <Holder : HoldsData<Collection<Value>>, Value> Holder.retryLoadItemsOnNetworkAvailable: Boolean
    get() = with(status) {
        value.isEmpty() && this is LoadingFailed<*> && (error == null || error is IOException)
    }

data class DataList<Value>(
    override val value: Collection<Value> = emptyList(),
    override val status: DataStatus = Initial
) : HoldsData<Collection<Value>> {

    override val copyWithLoadingInProgress: DataList<Value>
        get() = copy(status = Loading)

    override fun <E> copyWithError(error: E): DataList<Value> = copy(status = LoadingFailed(error))

    fun copyWithNewItems(newItems: Iterable<Value>): DataList<Value> = copy(
        value = value + newItems,
        status = LoadedSuccessfully
    )

    fun copyWithNewItems(vararg newItems: Value): DataList<Value> = copy(
        value = value + newItems,
        status = LoadedSuccessfully
    )
}

interface HoldsPagedData<Item, P : HoldsPagedData<Item, P>> : HoldsData<Collection<Item>> {
    val shouldLoadMore: Boolean
    fun copyWithClearedError(): P
}

data class PagedDataList<Value>(
    override val value: Collection<Value> = emptyList(),
    override val status: DataStatus = Initial,
    val offset: Int = 0,
    val totalItems: Int = Integer.MAX_VALUE
) : HoldsPagedData<Value, PagedDataList<Value>> {

    override val shouldLoadMore: Boolean
        get() = status !is Loading && offset < totalItems

    override val copyWithLoadingInProgress: PagedDataList<Value>
        get() = copy(status = Loading)

    override fun <E> copyWithError(error: E): PagedDataList<Value> = copy(
        status = LoadingFailed(error)
    )

    fun copyWithNewItems(
        newItems: Iterable<Value>, offset: Int, totalItems: Int
    ): PagedDataList<Value> = copy(
        value = value + newItems,
        offset = offset,
        status = LoadedSuccessfully,
        totalItems = totalItems
    )

    override fun copyWithClearedError(): PagedDataList<Value> = copy(status = LoadedSuccessfully)
}

sealed class DataStatus
object Initial : DataStatus()
object Loading : DataStatus()
object LoadedSuccessfully : DataStatus()
data class LoadingFailed<E>(val error: E) : DataStatus()
