package com.example.there.findclips.search

import android.databinding.ObservableField
import android.os.Parcel
import android.os.Parcelable

data class SearchViewState(val spinnerSelection: ObservableField<Int> = ObservableField(0),
                           val lastQuery: String = "",
                           val queryHint: ObservableField<String> = ObservableField("Search Spotify")) : Parcelable {

    fun updateQueryHint(position: Int) {
        if (position == 0) {
            queryHint.set(SEARCH_SPOTIFY)
        } else if (position == 1) {
            queryHint.set(SEARCH_YOUTUBE)
        }
    }

    private constructor(parcel: Parcel) : this(
            ObservableField(parcel.readInt()),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(spinnerSelection.get() ?: 0)
        parcel.writeString(lastQuery)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SearchViewState> {
        override fun createFromParcel(parcel: Parcel): SearchViewState = SearchViewState(parcel)

        override fun newArray(size: Int): Array<SearchViewState?> = arrayOfNulls(size)

        private const val SEARCH_SPOTIFY = "Search Spotify"
        private const val SEARCH_YOUTUBE = "Search Youtube"
    }
}