package com.example.there.findclips.fragments.search

import android.app.SearchManager
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.there.findclips.R
import com.example.there.findclips.databinding.FragmentSearchBinding
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private val pagerAdapter: SearchFragmentPagerAdapter by lazy { SearchFragmentPagerAdapter(childFragmentManager) }

    private val view: com.example.there.findclips.fragments.search.SearchView by lazy { SearchView(pagerAdapter) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.searchFragmentView = view
        binding.searchFragmentViewPager.offscreenPageLimit = 1
        return binding.root
    }

    private val onMenuItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            search_fragment_view_pager.currentItem = position
        }
    }

    private var searchViewMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        menu?.findItem(R.id.favourites_spinner_menu_item)?.isVisible = false
        menu?.findItem(R.id.spinner_menu_item)?.isVisible = true
        menu?.findItem(R.id.search_view_menu_item)?.isVisible = true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        fun initSpinner(menu: Menu?) {
            val spinnerItem = menu?.findItem(R.id.spinner_menu_item)
            val menuSpinner = spinnerItem?.actionView as? Spinner

            menuSpinner?.let {
                it.adapter = ArrayAdapter<String>(context, R.layout.spinner_item, menuItems)
                it.setSelection(search_fragment_view_pager.currentItem)
                it.onItemSelectedListener = onMenuItemSelectedListener
            }
        }

        fun initSearchView(menu: Menu?) {
            searchViewMenuItem = menu?.findItem(R.id.search_view_menu_item)
            val searchView = searchViewMenuItem?.actionView as? SearchView
            val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView?.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }

        inflater?.inflate(R.menu.search_fragment_menu, menu)
        initSpinner(menu)
        initSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun search(query: String) {
        searchViewMenuItem?.collapseActionView()
        (pagerAdapter.currentFragment as? MainSearchFragment)?.query = query
    }

    companion object {
        private val menuItems = arrayOf("Spotify", "Youtube")
    }
}
