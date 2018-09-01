package com.example.there.findclips.fragments.lists

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.Router
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyCategoriesBinding
import com.example.there.findclips.model.entities.Category
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.lists.CategoriesList
import com.example.there.findclips.view.lists.OnCategoryClickListener
import com.example.there.findclips.view.recycler.HeaderDecoration
import com.example.there.findclips.view.recycler.SeparatorDecoration

class SpotifyCategoriesFragment : BaseSpotifyListFragment<Category>() {

    override val viewState: ViewState<Category> =
            ViewState(ObservableSortedList<Category>(Category::class.java, object : ObservableSortedList.Callback<Category> {
                override fun compare(o1: Category, o2: Category): Int = o1.name.toLowerCase().compareTo(o2.name.toLowerCase())

                override fun areItemsTheSame(item1: Category, item2: Category): Boolean = item1.id == item2.id

                override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem.id == newItem.id
            }))

    private val onCategoryClickListener = object : OnCategoryClickListener {
        override fun onClick(item: Category) = Router.goToCategoryActivity(activity, category = item)
    }

    private val view: SpotifyCategoriesFragment.View by lazy {
        SpotifyCategoriesFragment.View(
                state = viewState,
                adapter = CategoriesList.Adapter(viewState.items, R.layout.category_item, onCategoryClickListener),
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyCategoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_categories, container, false)

        return binding.apply {
            view = this@SpotifyCategoriesFragment.view
            categoriesRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader)
                categoriesRecyclerView.addItemDecoration(HeaderDecoration.with(context)
                        .inflate(R.layout.categories_header)
                        .parallax(1f)
                        .dropShadowDp(2)
                        .columns(listColumnCount)
                        .build())
        }.root
    }

    data class View(val state: BaseSpotifyListFragment.ViewState<Category>,
                    val adapter: CategoriesList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration)

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