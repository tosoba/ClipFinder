package com.example.itemlist.spotify

import androidx.fragment.app.Fragment
import com.example.coreandroid.BR
import com.example.coreandroid.R
import com.example.coreandroid.base.fragment.BaseListFragment
import com.example.coreandroid.model.spotify.Category
import com.example.coreandroid.util.list.IdentifiableObservableListItem
import com.example.coreandroid.util.list.ObservableSortedList
import com.example.coreandroid.view.recyclerview.binder.ItemBinder
import com.example.coreandroid.view.recyclerview.binder.ItemBinderBase
import com.example.coreandroid.view.recyclerview.item.ListItemView

class SpotifyCategoriesFragment : BaseListFragment<Category>() {

    override val defaultHeaderText: String = "Categories"

    override val viewState: ViewState<Category> = ViewState(
        ObservableSortedList(
            Category::class.java,
            IdentifiableObservableListItem.unsortedCallback()
        )
    )

    override val listItemView: ListItemView<Category>
        get() = object : ListItemView<Category>(viewState.items) {
            override val itemViewBinder: ItemBinder<Category>
                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(
        item: Category
    ): Fragment = fragmentFactory.newSpotifyCategoryFragment(category = item)
}
