package com.clipfinder.core.android.base.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clipfinder.core.android.R
import com.clipfinder.core.android.databinding.FragmentListBinding
import com.clipfinder.core.android.databinding.HeaderItemBinding
import com.clipfinder.core.android.util.ext.navHostFragment
import com.clipfinder.core.android.util.ext.screenOrientation
import com.clipfinder.core.android.view.recyclerview.decoration.HeaderDecoration
import com.clipfinder.core.android.view.recyclerview.item.ListItemView
import com.clipfinder.core.android.view.recyclerview.item.RecyclerViewItemView
import com.clipfinder.core.android.view.recyclerview.item.RecyclerViewItemViewState
import com.clipfinder.core.android.view.recyclerview.listener.ClickHandler
import com.clipfinder.core.android.view.recyclerview.listener.EndlessRecyclerOnScrollListener
import kotlinx.android.synthetic.main.fragment_list.*

abstract class BaseListFragment<T : Parcelable> : Fragment() {

    var refreshData: ((BaseListFragment<T>) -> Unit)? = null
    var loadMore: (() -> Unit)? = null
    var onItemClick: ((T) -> Unit)? = null

    protected abstract val defaultHeaderText: String

    private var currentHeaderDecoration: RecyclerView.ItemDecoration? = null

    private var xmlHeaderText: String? = null

    private val view: View<T> by lazy(LazyThreadSafetyMode.NONE) {
        View(
            state = viewState,
            recyclerViewItemView = RecyclerViewItemView(
                RecyclerViewItemViewState(
                    ObservableField(false),
                    viewState.items,
                    ObservableField(false)
                ),
                listItemView,
                ClickHandler { item ->
                    onItemClick?.let { it(item) }
                        ?: run { navHostFragment?.showFragment(fragmentToShowOnItemClick(item), true) }
                },
                onScrollListener = onScrollListener
            )
        )
    }

    abstract val viewState: ViewState<T>

    protected abstract val listItemView: ListItemView<T>

    private val listColumnCount: Int
        get() = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

    private val onScrollListener: RecyclerView.OnScrollListener by lazy {
        EndlessRecyclerOnScrollListener { loadMore?.invoke() }
    }

    protected abstract fun fragmentToShowOnItemClick(item: T): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFromArguments()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false)
        return binding.apply {
            @Suppress("UNCHECKED_CAST")
            view = this@BaseListFragment.view as View<Parcelable>
            listFragmentRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, RecyclerView.VERTICAL, false)
            if (viewState.shouldShowHeader) listFragmentRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        updateRecyclerViewOnConfigChange()
    }

    override fun onInflate(context: Context, attrs: AttributeSet, savedInstanceState: Bundle?) {
        super.onInflate(context, attrs, savedInstanceState)

        val attributes = activity?.obtainStyledAttributes(attrs, R.styleable.BaseListFragment)
        attributes?.getText(R.styleable.BaseListFragment_main_hint_text)?.let {
            viewState.mainHintText.set(it.toString())
        }
        attributes?.getText(R.styleable.BaseListFragment_additional_hint_text)?.let {
            viewState.additionalHintText.set(it.toString())
        }
        attributes?.getBoolean(R.styleable.BaseListFragment_should_show_header, false)?.let {
            viewState.shouldShowHeader = it
        }
        attributes?.getString(R.styleable.BaseListFragment_header_text)?.let {
            xmlHeaderText = it
        }
        attributes?.recycle()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) refreshData?.invoke(this)
    }

    fun updateItems(items: List<T>, shouldRemove: Boolean = true) {
        if (shouldRemove) {
            val toRemove = viewState.items.filterNot(items::contains)
            viewState.items.removeAll(toRemove)
        }
        viewState.items.addAll(items)
    }

    fun clearItems() = viewState.items.clear()

    fun resetItems(items: List<T>) {
        viewState.items.clear()
        viewState.items.addAll(items)
    }

    private fun headerItemDecoration(): RecyclerView.ItemDecoration {
        val binding = HeaderItemBinding.inflate(LayoutInflater.from(context), null, false)
            .apply {
                text = xmlHeaderText ?: defaultHeaderText
                executePendingBindings()
            }
        currentHeaderDecoration = HeaderDecoration(binding.root, false, 1f, 0f, listColumnCount)
        return currentHeaderDecoration!!
    }

    private fun initFromArguments() {
        arguments?.let {
            viewState.mainHintText.set(it.getString(EXTRA_MAIN_HINT))
            viewState.additionalHintText.set(it.getString(EXTRA_ADDITIONAL_HINT))
            if (it.containsKey(EXTRA_ITEMS)) updateItems(it.getParcelableArrayList(EXTRA_ITEMS)!!)
            viewState.shouldShowHeader = it.getBoolean(EXTRA_SHOULD_SHOW_HEADER)
        }
    }

    private fun updateRecyclerViewOnConfigChange() {
        list_fragment_recycler_view?.let { recyclerView ->
            if (viewState.shouldShowHeader) {
                currentHeaderDecoration?.let(recyclerView::removeItemDecoration)
                recyclerView.addItemDecoration(headerItemDecoration())
            }
            recyclerView.layoutManager = GridLayoutManager(context, listColumnCount, RecyclerView.VERTICAL, false)
        }
    }

    class ViewState<T : Parcelable>(
        val items: ObservableArrayList<T> = ObservableArrayList(),
        val mainHintText: ObservableField<String> = ObservableField(""),
        val additionalHintText: ObservableField<String> = ObservableField(""),
        var shouldShowHeader: Boolean = false
    )

    class View<T : Parcelable>(
        val state: ViewState<T>,
        val recyclerViewItemView: RecyclerViewItemView<T>
    )

    companion object {
        const val EXTRA_MAIN_HINT = "EXTRA_MAIN_HINT"
        const val EXTRA_ADDITIONAL_HINT = "EXTRA_ADDITIONAL_HINT"
        const val EXTRA_ITEMS = "EXTRA_ITEMS"
        const val EXTRA_SHOULD_SHOW_HEADER = "SHOULD_SHOW_HEADER"
    }
}
