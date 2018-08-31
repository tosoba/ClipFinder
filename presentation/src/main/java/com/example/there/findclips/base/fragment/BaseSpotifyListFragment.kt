package com.example.there.findclips.base.fragment

import android.content.Context
import android.databinding.ObservableArrayList
import android.databinding.ObservableField
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.util.AttributeSet
import com.example.there.findclips.R

abstract class BaseSpotifyListFragment<T : Parcelable> : Fragment() {

    fun addItems(items: List<T>) {
        if (viewState.items.isNotEmpty()) viewState.items.clear()
        viewState.items.addAll(items)
    }

    val viewState: ViewState<T> = ViewState()

    data class ViewState<T : Parcelable>(
            val items: ObservableArrayList<T> = ObservableArrayList(),
            val mainHintText: ObservableField<String> = ObservableField(""),
            val additionalHintText: ObservableField<String> = ObservableField("")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initHintsFromArguments()

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_SAVED_ITEMS)) {
            viewState.items.addAll(savedInstanceState.getParcelableArrayList(KEY_SAVED_ITEMS))
        }
    }

    private fun initHintsFromArguments() {
        arguments?.let {
            viewState.mainHintText.set(it.getString(EXTRA_MAIN_HINT))
            viewState.additionalHintText.set(it.getString(EXTRA_ADDITIONAL_HINT))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (viewState.items.isNotEmpty()) {
            outState.putParcelableArrayList(KEY_SAVED_ITEMS, viewState.items)
        }
    }

    override fun onInflate(context: Context?, attrs: AttributeSet?, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)
        val attributes = activity?.obtainStyledAttributes(attrs, R.styleable.BaseSpotifyListFragment)
        attributes?.getText(R.styleable.BaseSpotifyListFragment_main_hint_text)?.let { viewState.mainHintText.set(it.toString()) }
        attributes?.getText(R.styleable.BaseSpotifyListFragment_additional_hint_text)?.let { viewState.additionalHintText.set(it.toString()) }
        attributes?.recycle()
    }

    companion object {
        private const val KEY_SAVED_ITEMS = "KEY_SAVED_ITEMS"

        private const val EXTRA_MAIN_HINT = "EXTRA_MAIN_HINT"
        private const val EXTRA_ADDITIONAL_HINT = "EXTRA_ADDITIONAL_HINT"

        fun <T, I> putArguments(fragment: T, mainHintText: String, additionalHintText: String) where T : BaseSpotifyListFragment<I> {
            val args = Bundle().apply {
                putString(EXTRA_MAIN_HINT, mainHintText)
                putString(EXTRA_ADDITIONAL_HINT, additionalHintText)
            }
            fragment.setArguments(args)
        }
    }
}