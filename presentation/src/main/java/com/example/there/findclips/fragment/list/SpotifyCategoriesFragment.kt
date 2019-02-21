package com.example.there.findclips.fragment.list

import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyCategoriesBinding
import com.example.there.findclips.fragment.spotifyitem.category.CategoryFragment
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.navHostFragment
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.list.ClickHandler
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemView
import com.example.there.findclips.view.list.item.RecyclerViewItemViewState
import kotlinx.android.synthetic.main.fragment_spotify_categories.*

class SpotifyCategoriesFragment : BaseSpotifyListFragment<Category>() {

    override val itemsRecyclerView: RecyclerView?
        get() = categories_recycler_view

    override val defaultHeaderText: String = "Categories"

    override val viewState: ViewState<Category> = ViewState(ObservableSortedList<Category>(Category::class.java, Category.unsortedListCallback))

    override val view: BaseSpotifyListFragment.View<Category> by lazy {
        BaseSpotifyListFragment.View(
                state = viewState,
                recyclerViewItemView = RecyclerViewItemView(
                        RecyclerViewItemViewState(
                                ObservableField(false),
                                viewState.items
                        ),
                        object : ListItemView<Category>(viewState.items) {
                            override val itemViewBinder: ItemBinder<Category>
                                get() = ItemBinderBase(BR.category, R.layout.grid_category_item)
                        },
                        ClickHandler { category ->
                            onItemClick?.let { it(category) }
                                    ?: run { navHostFragment?.showFragment(CategoryFragment.newInstance(category), true) }
                        },
                        null,
                        null
                )
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyCategoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_categories, container, false)

        return binding.apply {
            view = this@SpotifyCategoriesFragment.view
            categoriesRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) categoriesRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }

    companion object {
        fun newInstance(
                mainHintText: String,
                additionalHintText: String,
                items: ArrayList<Category>?,
                shouldShowHeader: Boolean = false
        ) = SpotifyCategoriesFragment().apply {
            putArguments(mainHintText, additionalHintText, items, shouldShowHeader)
        }
    }
}