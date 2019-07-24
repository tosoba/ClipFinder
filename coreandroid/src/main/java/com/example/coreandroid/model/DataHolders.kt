package com.example.coreandroid.model

interface HoldsData<Value> {
    val value: Value
    val status: DataStatus
    val loadingFailed: Boolean
        get() = status is LoadingFailed<*>
    val copyWithLoadingInProgress: HoldsData<Value>
    fun copyWithError(throwable: Throwable?): HoldsData<Value>
    fun success(value: Value): HoldsData<Value>
}

inline fun <Holder : HoldsData<Value>, Value> Holder.ifNotLoading(block: (Holder) -> Unit) {
    if (status !is Loading) block(this)
}

inline fun <Holder : HoldsData<Value>, Value> Holder.ifLastLoadingFailed(block: (Holder) -> Unit) {
    if (status is LoadingFailed<*>) block(this)
}

inline fun <Holder : HoldsData<Value>, Value> Holder.ifLastLoadingCompletedSuccessFully(block: (Holder) -> Unit) {
    if (status is LoadedSuccessfully) block(this)
}

data class Data<Value>(
        override val value: Value,
        override val status: DataStatus = Initial
) : HoldsData<Value> {

    override val copyWithLoadingInProgress: Data<Value>
        get() = copy(status = Loading)

    override fun copyWithError(throwable: Throwable?): Data<Value> = copy(
            status = LoadingFailed(throwable)
    )

    override fun success(value: Value): Data<Value> = Data(value, LoadedSuccessfully)

    fun copyWithNewValue(value: Value): Data<Value> = copy(
            value = value,
            status = LoadedSuccessfully
    )
}

interface HoldsDataList<Value> : HoldsData<List<Value>> {
    val isEmptyAndLastLoadingFailed: Boolean
        get() = loadingFailed && value.isEmpty()
}

inline fun <Data : HoldsDataList<Value>, Value> Data.ifEmptyAndIsNotLoading(block: (Data) -> Unit) {
    if (status !is Loading && value.isEmpty()) block(this)
}

inline fun <Data : HoldsDataList<Item>, Item> Data.ifNotEmpty(block: (Data) -> Unit) {
    if (value.isNotEmpty()) block(this)
}

data class DataList<Value>(
        override val value: List<Value> = emptyList(),
        override val status: DataStatus = Initial
) : HoldsDataList<Value> {

    override val copyWithLoadingInProgress: DataList<Value>
        get() = copy(status = Loading)

    override fun copyWithError(throwable: Throwable?): DataList<Value> = copy(
            status = LoadingFailed(throwable)
    )

    override fun success(value: List<Value>): DataList<Value> = DataList(value, LoadedSuccessfully)

    fun copyWithNewItems(newItems: List<Value>): DataList<Value> = copy(
            value = value + newItems,
            status = LoadedSuccessfully
    )
}

data class PagedDataList<Value>(
        override val value: List<Value> = emptyList(),
        override val status: DataStatus = Initial,
        val offset: Int = 0,
        val totalItems: Int = Integer.MAX_VALUE
) : HoldsDataList<Value> {

    override val copyWithLoadingInProgress: PagedDataList<Value>
        get() = copy(status = Loading)

    override fun copyWithError(throwable: Throwable?): PagedDataList<Value> = copy(
            status = LoadingFailed(throwable)
    )

    override fun success(value: List<Value>): DataList<Value> = throw IllegalStateException("TODOLOL") //TODO

    fun copyWithNewItems(
            newItems: List<Value>, offset: Int
    ): PagedDataList<Value> = copy(
            value = value + newItems,
            offset = offset,
            status = LoadedSuccessfully
    )

    fun copyWithNewItems(
            newItems: List<Value>, offset: Int, totalItems: Int
    ): PagedDataList<Value> = copy(
            value = value + newItems,
            offset = offset,
            status = LoadedSuccessfully,
            totalItems = totalItems
    )

    inline fun ifNotLoadingAndNotAllLoaded(block: (PagedDataList<Value>) -> Unit) {
        if (status !is Loading && offset < totalItems) block(this)
    }
}

sealed class DataStatus
object Initial : DataStatus()
object Loading : DataStatus()
object LoadedSuccessfully : DataStatus()
data class LoadingFailed<E>(val error: E) : DataStatus()