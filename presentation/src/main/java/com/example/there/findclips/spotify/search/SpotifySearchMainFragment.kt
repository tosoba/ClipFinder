package com.example.there.findclips.spotify.search

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.databinding.FragmentSpotifySearchMainBinding
import com.example.there.findclips.spotify.search.spotify.SpotifySearchFragment
import com.example.there.findclips.util.ext.appCompatActivity
import com.example.there.findclips.util.ext.dpToPx
import com.example.there.findclips.util.ext.setHeight
import com.example.there.findclips.util.ext.setupWithBackNavigation
import com.example.there.findclips.videos.search.VideosSearchFragment
import com.example.there.findclips.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import kotlinx.android.synthetic.main.fragment_spotify_search_main.*


class SpotifySearchMainFragment : Fragment() {

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(childFragmentManager, arrayOf(
                SpotifySearchFragment.newInstanceWithQuery(query),
                VideosSearchFragment.newInstanceWithQuery(query)
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