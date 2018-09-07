package com.example.there.findclips.util.ext

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import com.example.there.findclips.activities.main.MainActivity
import com.example.there.findclips.base.fragment.BaseHostFragment
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import java.util.*

val Fragment.mainActivity: MainActivity?
    get() = activity as? MainActivity

fun <I : Parcelable> BaseSpotifyListFragment<I>.putArguments(
        mainHintText: String,
        additionalHintText: String,
        items: ArrayList<I>?,
        shouldShowHeader: Boolean
) {
    val args = Bundle().apply {
        putString(BaseSpotifyListFragment.EXTRA_MAIN_HINT, mainHintText)
        putString(BaseSpotifyListFragment.EXTRA_ADDITIONAL_HINT, additionalHintText)
        items?.let { putParcelableArrayList(BaseSpotifyListFragment.EXTRA_ITEMS, it) }
        putBoolean(BaseSpotifyListFragment.EXTRA_SHOULD_SHOW_HEADER, shouldShowHeader)
    }
    arguments = args
}

val Fragment.hostFragment: BaseHostFragment?
    get() = parentFragment as? BaseHostFragment