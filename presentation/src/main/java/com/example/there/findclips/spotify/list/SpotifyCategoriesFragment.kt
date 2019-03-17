package com.example.there.findclips.spotify.list

import android.support.v4.app.Fragment
import com.example.there.findclips.R
import com.example.there.findclips.spotify.spotifyitem.category.CategoryFragment


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