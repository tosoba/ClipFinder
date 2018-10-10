package com.example.there.findclips.base.fragment

import android.content.Context
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import com.example.there.findclips.R
import com.example.there.findclips.databinding.HeaderItemBinding
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.screenOrientation
import com.example.there.findclips.view.list.item.HeaderItemViewState
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.recycler.EndlessRecyclerOnScrollListener
import com.example.there.findclips.view.recycler.HeaderDecoration

abstract class BaseSpotifyListFragment<T : Parcelable> : Fragment() {

    var refreshData: ((BaseSpotifyListFragment<T>) -> Unit)? = null

    var loadMore: (() -> Unit)? = null

    var onItemClick: ((T) -> Unit)? = null

    protected abstract val itemsRecyclerView: RecyclerView?

    protected abstract val defaultHeaderText: String

    private var currentHeaderDecoration: RecyclerView.ItemDecoration? = null

    protected fun headerItemDecoration(): RecyclerView.ItemDecoration {
        val binding = DataBindingUtil.inflate<HeaderItemBinding>(
                LayoutInflater.from(context),
                R.layout.header_item,
                null,
                false
        ).apply {
            viewState = HeaderItemViewState(xmlHeaderText ?: defaultHeaderText)
            executePendingBindings()
        }

        currentHeaderDecoration = HeaderDecoration(binding.root, false, 1f, 0f, listColumnCount)
        return currentHeaderDecoration!!
    }

    private fun updateRecyclerViewOnConfigChange() {
        itemsRecyclerView?.let { recyclerView ->
            if (viewState.shouldShowHeader) {
                currentHeaderDecoration?.let { recyclerView.removeItemDecoration(it) }
                recyclerView.addItemDecoration(headerItemDecoration())
            }
            recyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        updateRecyclerViewOnConfigChange()
    }

    protected val onScrollListener: RecyclerView.OnScrollListener by lazy {
        object : EndlessRecyclerOnScrollListener() {
            override fun onLoadMore() {
                loadMore?.invoke()
            }
        }
    }

    protected val listColumnCount: Int
        get() = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) refreshData?.invoke(this)
    }

    fun updateItems(items: List<T>, shouldRemove: Boolean = true) {
        if (shouldRemove) {
            val toRemove = viewState.items.filter { !items.contains(it) }
            viewState.items.removeAll(toRemove)
        }
        viewState.items.addAll(items)
    }

    fun clearItems() = viewState.items.clear()

    fun resetItems(items: List<T>) {
        viewState.items.clear()
        viewState.items.addAll(items)
    }

    abstract val viewState: ViewState<T>

    class ViewState<T : Parcelable>(
            val items: ObservableSortedList<T>,
            val mainHintText: ObservableField<String> = ObservableField(""),
            val additionalHintText: ObservableField<String> = ObservableField(""),
            var shouldShowHeader: Boolean = false
    )

    abstract val view: View<T>

    class View<T : Parcelable>(
            val state: BaseSpotifyListFragment.ViewState<T>,
            val recyclerViewItemView: RecyclerViewItemView<T>
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initFromArguments()
    }

    private fun initFromArguments() {
        arguments?.let {
            viewState.mainHintText.set(it.getString(EXTRA_MAIN_HINT))
            viewState.additionalHintText.set(it.getString(EXTRA_ADDITIONAL_HINT))
            if (it.containsKey(EXTRA_ITEMS)) updateItems(it.getParcelableArrayList(EXTRA_ITEMS)!!)
            viewState.shouldShowHeader = it.getBoolean(EXTRA_SHOULD_SHOW_HEADER)
        }
    }

    private var xmlHeaderText: String? = null

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
        attributes?.getString(R.styleable.BaseSpotifyListFragment_header_text)?.let {
            xmlHeaderText = it
        }
        attributes?.recycle()
    }

    companion object {
        const val EXTRA_MAIN_HINT = "EXTRA_MAIN_HINT"
        const val EXTRA_ADDITIONAL_HINT = "EXTRA_ADDITIONAL_HINT"
        const val EXTRA_ITEMS = "EXTRA_ITEMS"
        const val EXTRA_SHOULD_SHOW_HEADER = "SHOULD_SHOW_HEADER"
    }
}