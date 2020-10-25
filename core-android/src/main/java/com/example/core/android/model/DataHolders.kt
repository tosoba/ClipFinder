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

val <Holder : HoldsData<Value>, Value> Holder.retryLoadOnNetworkAvailable: Boolean
    get() = with(status) { this is LoadingFailed<*> && (error == null || error is IOException) }


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

interface HoldsPagedData<Item> : HoldsData<Collection<Item>> {
    val shouldLoadMore: Boolean
}

data class PageTokenDataList<Value>(
    val list: DataList<Value> = DataList(),
    val nextPageToken: String? = null
) : HoldsPagedData<Value> {

    constructor(status: DataStatus) : this(list = DataList(status = status))

    override val value: Collection<Value>
        get() = list.value

    override val status: DataStatus
        get() = list.status

    override val copyWithLoadingInProgress: PageTokenDataList<Value>
        get() = copy(list = list.copy(status = Loading))

    override fun <E> copyWithError(error: E): PageTokenDataList<Value> = copy(
        list = list.copy(status = LoadingFailed(error))
    )

    override val shouldLoadMore: Boolean
        get() = status !is Loading && (nextPageToken != null || value.isEmpty())

    fun copyWithNewItems(
        newItems: Iterable<Value>, nextPageToken: String?
    ): PageTokenDataList<Value> = copy(
        list = list.copyWithNewItems(newItems),
        nextPageToken = nextPageToken
    )
}

data class PagedDataList<Value>(
    override val value: Collection<Value> = emptyList(),
    override val status: DataStatus = Initial,
    val offset: Int = 0,
    val totalItems: Int = Integer.MAX_VALUE
) : HoldsPagedData<Value> {

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
}

sealed class DataStatus
object Initial : DataStatus()
object Loading : DataStatus()
object LoadedSuccessfully : DataStatus()
data class LoadingFailed<E>(val error: E) : DataStatus()
