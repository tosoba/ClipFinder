package com.example.spotifysearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.util.ext.appCompatActivity
import com.example.coreandroid.util.ext.dpToPx
import com.example.coreandroid.util.ext.setHeight
import com.example.coreandroid.util.ext.setupWithBackNavigation
import com.example.coreandroid.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.spotifysearch.databinding.FragmentSpotifySearchMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_spotify_search_main.*
import org.koin.android.ext.android.inject

class SpotifySearchMainFragment : Fragment() {

    private val fragmentFactory: IFragmentFactory by inject()

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(childFragmentManager, arrayOf(
                fragmentFactory.newSpotifySearchFragment(query),
                fragmentFactory.newVideosSearchFragment(query)
        ))
    }

    private val itemIds: Array<Int> = arrayOf(R.id.action_spotify, R.id.action_videos)

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == search_bottom_navigation_view?.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        search_fragment_view_pager?.currentItem = itemIds.indexOf(item.itemId)

        return@OnNavigationItemSelectedListener true
    }

    private val view: SpotifySearchMainView by lazy {
        SpotifySearchMainView(query, pagerAdapter, onNavigationItemSelectedListener, 1)
    }

    private val query: String by lazy { arguments!!.getString(ARG_QUERY) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSpotifySearchMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_spotify_search_main, container, false)
        return binding.apply {
            searchFragmentView = view
            searchBottomNavigationView.setHeight(activity!!.dpToPx(40f).toInt())
            searchToolbar.setupWithBackNavigation(appCompatActivity)
        }.root
    }

    companion object {
        private const val ARG_QUERY = "ARG_QUERY"

        fun newInstance(query: String) = SpotifySearchMainFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_QUERY, query)
            }
        }
    }
}