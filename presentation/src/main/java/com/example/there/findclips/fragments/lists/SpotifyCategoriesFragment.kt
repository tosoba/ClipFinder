package com.example.there.findclips.fragments.lists

import android.content.res.Configuration
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
import com.example.there.findclips.util.screenOrientation
import com.example.there.findclips.view.lists.CategoriesList
import com.example.there.findclips.view.lists.OnCategoryClickListener
import com.example.there.findclips.view.recycler.HeaderDecoration
import com.example.there.findclips.view.recycler.SeparatorDecoration

class SpotifyCategoriesFragment : BaseSpotifyListFragment<Category>() {

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
            val columnCount = if (activity?.screenOrientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2
            categoriesRecyclerView.layoutManager = GridLayoutManager(context, columnCount, GridLayoutManager.VERTICAL, false)
            categoriesRecyclerView.addItemDecoration(HeaderDecoration.with(context)
                    .inflate(R.layout.categories_header)
                    .parallax(1f)
                    .dropShadowDp(2)
                    .columns(columnCount)
                    .build())
        }.root
    }

    data class View(val state: BaseSpotifyListFragment.ViewState<Category>,
                    val adapter: CategoriesList.Adapter,
                    val itemDecoration: RecyclerView.ItemDecoration)

    companion object {
        fun newInstance(mainHintText: String, additionalHintText: String) = SpotifyCategoriesFragment().apply {
            BaseSpotifyListFragment.putArguments(this, mainHintText, additionalHintText)
        }
    }
}