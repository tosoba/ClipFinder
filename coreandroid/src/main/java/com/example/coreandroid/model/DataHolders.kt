package com.example.coreandroid.model

interface HoldsData<Value> {
    val value: Value
    val status: DataStatus
    val loadingFailed: Boolean
        get() = status is DataStatus.LoadingFailed<*>
}

inline fun <Holder : HoldsData<Value>, Value> Holder.ifNotLoading(block: (Holder) -> Unit) {
    if (status !is DataStatus.InProgress) block(this)
}

inline fun <Holder : HoldsData<Value>, Value> Holder.ifLastLoadingFailed(block: (Holder) -> Unit) {
    if (status is DataStatus.LoadingFailed<*>) block(this)
}

inline fun <Holder : HoldsData<Value>, Value> Holder.ifLastLoadingCompletedSuccessFully(block: (Holder) -> Unit) {
    if (status is DataStatus.LoadedSuccessfully) block(this)
}

data class Data<Value>(
        override val value: Value,
        override val status: DataStatus = DataStatus.Initial
) : HoldsData<Value>

interface HoldsDataList<Value> : HoldsData<List<Value>> {
    val isEmptyAndLastLoadingFailed: Boolean
        get() = loadingFailed && value.isEmpty()
}

inline fun <Data : HoldsDataList<Value>, Value> Data.ifEmptyAndIsNotLoading(block: (Data) -> Unit) {
    if (status !is DataStatus.InProgress && value.isEmpty()) block(this)
}

inline fun <Data : HoldsDataList<Item>, Item> Data.ifNotEmpty(block: (Data) -> Unit) {
    if (value.isNotEmpty()) block(this)
}

data class DataList<Value>(
        override val value: List<Value> = emptyList(),
        override val status: DataStatus = DataStatus.Initial
) : HoldsDataList<Value> {

    val copyWithLoadingInProgress: DataList<Value>
        get() = copy(status = DataStatus.InProgress)

    fun copyWithError(throwable: Throwable?): DataList<Value> = copy(
            status = DataStatus.LoadingFailed(throwable)
    )

    fun copyWithNewItems(newItems: List<Value>): DataList<Value> = copy(
            value = value + newItems,
            status = DataStatus.LoadedSuccessfully
    )
}

data class PagedDataList<Value>(
        override val value: List<Value> = emptyList(),
        override val status: DataStatus = DataStatus.Initial,
        val offset: Int = 0,
        val totalItems: Int = Integer.MAX_VALUE
) : HoldsDataList<Value> {

    val copyWithLoadingInProgress: PagedDataList<Value>
        get() = copy(status = DataStatus.InProgress)

    fun copyWithError(throwable: Throwable?): PagedDataList<Value> = copy(
            status = DataStatus.LoadingFailed(throwable)
    )

    fun copyWithNewItems(
            newItems: List<Value>, offset: Int
    ): PagedDataList<Value> = copy(
            value = value + newItems,
            offset = offset,
            status = DataStatus.LoadedSuccessfully
    )

    fun copyWithNewItems(
            newItems: List<Value>, offset: Int, totalItems: Int
    ): PagedDataList<Value> = copy(
            value = value + newItems,
            offset = offset,
            status = DataStatus.LoadedSuccessfully,
            totalItems = totalItems
    )

    inline fun ifNotLoadingAndNotAllLoaded(block: (PagedDataList<Value>) -> Unit) {
        if (status !is DataStatus.InProgress && offset < totalItems) block(this)
    }
}

sealed class DataStatus {
    object Initial : DataStatus()
    object InProgress : DataStatus()
    object LoadedSuccessfully : DataStatus()
    data class LoadingFailed<E>(val error: E) : DataStatus()
}