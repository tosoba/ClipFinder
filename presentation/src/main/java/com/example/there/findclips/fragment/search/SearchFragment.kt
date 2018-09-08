package com.example.there.findclips.fragment.search

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.FragmentSearchBinding
import com.example.there.findclips.util.ext.dpToPx
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private val pagerAdapter: SearchFragmentPagerAdapter by lazy { SearchFragmentPagerAdapter(childFragmentManager) }

    private val itemIds: Array<Int> = arrayOf(R.id.action_spotify, R.id.action_videos)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == search_bottom_navigation_view?.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        search_fragment_view_pager?.currentItem = itemIds.indexOf(item.itemId)

        return@OnNavigationItemSelectedListener true
    }

    private val view: com.example.there.findclips.fragment.search.SearchView by lazy {
        SearchView(pagerAdapter, onNavigationItemSelectedListener)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.searchFragmentView = view
        binding.searchFragmentViewPager.offscreenPageLimit = 1
        val params = binding.searchBottomNavigationView.layoutParams
        params.height = activity!!.dpToPx(40f).toInt()
        binding.searchBottomNavigationView.layoutParams = params

        return binding.root
    }

    fun search(query: String) {
        (pagerAdapter.currentFragment as? MainSearchFragment)?.query = query
    }
}
