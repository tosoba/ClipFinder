package com.example.there.findclips.fragment.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseSpotifyListFragment
import com.example.there.findclips.databinding.FragmentSpotifyCategoriesBinding
import com.example.there.findclips.fragment.category.CategoryFragment
import com.example.there.findclips.model.entity.Category
import com.example.there.findclips.util.ObservableSortedList
import com.example.there.findclips.util.ext.hostFragment
import com.example.there.findclips.util.ext.putArguments
import com.example.there.findclips.view.list.impl.CategoriesList
import com.example.there.findclips.view.recycler.SeparatorDecoration
import kotlinx.android.synthetic.main.fragment_spotify_categories.*

class SpotifyCategoriesFragment : BaseSpotifyListFragment<Category>() {

    override val itemsRecyclerView: RecyclerView?
        get() = categories_recycler_view

    override val headerText: String = "Categories"

    override val viewState: ViewState<Category> = ViewState(ObservableSortedList<Category>(Category::class.java, Category.sortedListCallback))

    private val categoriesAdapter: CategoriesList.Adapter by lazy {
        CategoriesList.Adapter(viewState.items, R.layout.category_item)
    }

    private val view: SpotifyCategoriesFragment.View by lazy {
        SpotifyCategoriesFragment.View(
                state = viewState,
                adapter = categoriesAdapter,
                itemDecoration = SeparatorDecoration(context!!, ResourcesCompat.getColor(resources, R.color.colorAccent, null), 2f)
        )
    }

    override fun initItemClicks() {
        disposablesComponent.add(categoriesAdapter.itemClicked.subscribe {
            hostFragment?.showFragment(CategoryFragment.newInstance(category = it), true)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        val binding: FragmentSpotifyCategoriesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_categories, container, false)

        return binding.apply {
            view = this@SpotifyCategoriesFragment.view
            categoriesRecyclerView.layoutManager = GridLayoutManager(context, listColumnCount, GridLayoutManager.VERTICAL, false)
            if (viewState.shouldShowHeader) categoriesRecyclerView.addItemDecoration(headerItemDecoration())
        }.root
    }


    class View(
            val state: BaseSpotifyListFragment.ViewState<Category>,
            val adapter: CategoriesList.Adapter,
            val itemDecoration: RecyclerView.ItemDecoration
    )

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