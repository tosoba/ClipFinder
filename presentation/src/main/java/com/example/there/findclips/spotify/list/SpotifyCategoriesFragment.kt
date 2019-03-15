package com.example.there.findclips.spotify.list

import android.support.v4.app.Fragment
import com.android.databinding.library.baseAdapters.BR
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseListFragment
import com.example.there.findclips.model.entity.spotify.Category
import com.example.there.findclips.spotify.spotifyitem.category.CategoryFragment
import com.example.there.findclips.util.list.IdentifiableObservableListItem
import com.example.there.findclips.util.list.ObservableSortedList
import com.example.there.findclips.view.list.binder.ItemBinder
import com.example.there.findclips.view.list.binder.ItemBinderBase
import com.example.there.findclips.view.list.item.ListItemView


class SpotifyCategoriesFragment : BaseListFragment<Category>() {

    override val defaultHeaderText: String = "Categories"

    override val viewState: ViewState<Category> = ViewState(ObservableSortedList<Category>(Category::class.java, IdentifiableObservableListItem.unsortedCallback()))

    override val listItemView: ListItemView<Category>
        get() = object : ListItemView<Category>(viewState.items) {
            override val itemViewBinder: ItemBinder<Category>
                get() = ItemBinderBase(BR.imageListItem, R.layout.named_image_grid_list_item)
        }

    override fun fragmentToShowOnItemClick(
            item: Category
    ): Fragment = CategoryFragment.newInstance(item)
}