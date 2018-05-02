package com.example.there.findclips.search

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
import com.example.there.findclips.main.MainFragment
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment(), MainFragment {

    override val bottomNavigationItemId: Int
        get() = R.id.action_search

    private val pagerAdapter: SearchFragmentPagerAdapter by lazy { SearchFragmentPagerAdapter(childFragmentManager) }

    private val view: SearchFragmentView by lazy { SearchFragmentView(pagerAdapter) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.searchFragmentView = view
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private val onMenuItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            search_fragment_view_pager.currentItem = position
        }
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
            val searchViewItem = menu?.findItem(R.id.search_view_menu_item)
            val searchView = searchViewItem?.actionView as? SearchView

            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (!searchView.isIconified) {
                        searchView.isIconified = true
                    }
                    searchViewItem.collapseActionView()

                    search(query)

                    return false
                }

                override fun onQueryTextChange(s: String): Boolean = true

                fun search(query: String) {
                    (pagerAdapter.currentFragment as? MainSearchFragment)?.query = query
                }
            })
        }

        inflater?.inflate(R.menu.search_fragment_menu, menu)
        initSpinner(menu)
        initSearchView(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    companion object {
        private val menuItems = arrayOf("Spotify", "Youtube")
    }
}
