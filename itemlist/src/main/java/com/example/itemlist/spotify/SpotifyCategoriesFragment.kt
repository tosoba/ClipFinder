package com.example.itemlist.spotify

import androidx.fragment.app.Fragment
import com.example.core.android.BR
import com.example.core.android.R
import com.example.core.android.base.fragment.BaseListFragment
import com.example.core.android.model.spotify.Category
import com.example.core.android.util.list.IdentifiableObservableListItem
import com.example.core.android.util.list.ObservableSortedList
import com.example.core.android.view.recyclerview.binder.ItemBinder
import com.example.core.android.view.recyclerview.binder.ItemBinderBase
import com.example.core.android.view.recyclerview.item.ListItemView

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
