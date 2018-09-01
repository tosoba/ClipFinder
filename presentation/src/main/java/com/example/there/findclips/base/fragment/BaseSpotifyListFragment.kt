package com.example.there.findclips.base.fragment

import android.content.Context
import android.content.res.Configuration
import android.databinding.ObservableField
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.util.AttributeSet
import com.example.there.findclips.R
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.screenOrientation

abstract class BaseSpotifyListFragment<T : Parcelable> : Fragment() {

    var refresh: ((BaseSpotifyListFragment<T>) -> Unit)? = null

    protected val listColumnCount: Int
        get() = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) refresh?.invoke(this)
    }

    fun updateItems(items: List<T>) {
        val toRemove = viewState.items.filter { !items.contains(it) }
        viewState.items.removeAll(toRemove)
        viewState.items.addAll(items)
    }

    abstract val viewState: ViewState<T>

    data class ViewState<T : Parcelable>(
            val items: ObservableSortedList<T>,
            val mainHintText: ObservableField<String> = ObservableField(""),
            val additionalHintText: ObservableField<String> = ObservableField(""),
            var shouldShowHeader: Boolean = false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFromArguments()

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVED_ITEMS)) {
            updateItems(savedInstanceState.getParcelableArrayList(KEY_SAVED_ITEMS))
        }
    }

    private fun initFromArguments() {
        arguments?.let {
            viewState.mainHintText.set(it.getString(EXTRA_MAIN_HINT))
            viewState.additionalHintText.set(it.getString(EXTRA_ADDITIONAL_HINT))
            if (it.containsKey(EXTRA_ITEMS)) updateItems(it.getParcelableArrayList(EXTRA_ITEMS))
            viewState.shouldShowHeader = it.getBoolean(EXTRA_SHOULD_SHOW_HEADER)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (viewState.items.isNotEmpty()) {
            outState.putParcelableArrayList(KEY_SAVED_ITEMS, ArrayList<T>(viewState.items.size).apply {
                addAll(viewState.items)
            })
        }
    }

    override fun onInflate(context: Context?, attrs: AttributeSet?, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)

        val attributes = activity?.obtainStyledAttributes(attrs, R.styleable.BaseSpotifyListFragment)
        attributes?.getText(R.styleable.BaseSpotifyListFragment_main_hint_text)?.let {
            viewState.mainHintText.set(it.toString())
        }
        attributes?.getText(R.styleable.BaseSpotifyListFragment_additional_hint_text)?.let {
            viewState.additionalHintText.set(it.toString())
        }
        attributes?.getBoolean(R.styleable.BaseSpotifyListFragment_should_show_header, false)?.let {
            viewState.shouldShowHeader = it
        }
        attributes?.recycle()
    }

    companion object {
        private const val KEY_SAVED_ITEMS = "KEY_SAVED_ITEMS"

        const val EXTRA_MAIN_HINT = "EXTRA_MAIN_HINT"
        const val EXTRA_ADDITIONAL_HINT = "EXTRA_ADDITIONAL_HINT"
        const val EXTRA_ITEMS = "EXTRA_ITEMS"
        const val EXTRA_SHOULD_SHOW_HEADER = "SHOULD_SHOW_HEADER"
    }
}