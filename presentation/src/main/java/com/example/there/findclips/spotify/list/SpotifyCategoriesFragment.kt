package com.example.there.findclips.spotify.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseListFragment
import com.example.there.findclips.databinding.FragmentSpotifyCategoriesBinding
import com.example.there.findclips.model.entity.spotify.Category
import com.example.there.findclips.spotify.spotifyitem.category.CategoryFragment
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView
import kotlinx.android.synthetic.main.fragment_spotify_categories.*

class SpotifyCategoriesFragment : BaseListFragment<Category>() {

    override val itemsRecyclerView: RecyclerView?
        get() = categories_recycler_view

    override val defaultHeaderText: String = "Categories"

    override val viewState: ViewState<Category> = ViewState(ObservableSortedList<Category>(Category::class.java, Category.unsortedListCallback))

    override val listItemView: ListItemView<Category>
        get() = object : ListItemView<Category>(viewState.items) {
            override val itemViewBinder: ItemBinder<Category>
                get() = ItemBinderBase(BR.category, R.layout.grid_category_item)
        }

    override fun newInstanceOfFragmentToShowOnClick(item: Category): Fragment = CategoryFragment.newInstance(item)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyCategoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_categories, container, false)

        return binding.apply {
            view = this@SpotifyCategoriesFragment.view
            categoriesRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) categoriesRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }
}