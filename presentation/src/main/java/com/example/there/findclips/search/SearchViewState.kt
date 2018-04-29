package com.example.there.findclips.search

import android.databinding.ObservableField
import android.os.Parcel
import android.os.Parcelable

data class SearchViewState(val spinnerSelection: ObservableField<Int> = ObservableField(0)) : Parcelable {

    constructor(parcel: Parcel) : this(ObservableField(parcel.readInt()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(spinnerSelection.get() ?: 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SearchViewState> {
        override fun createFromParcel(parcel: Parcel): SearchViewState = SearchViewState(parcel)

        override fun newArray(size: Int): Array<SearchViewState?> = arrayOfNulls(size)
    }
}