package com.example.spotify.search.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.example.core.android.util.ext.dpToPx
import com.example.core.android.util.ext.mainContentFragment
import com.example.core.android.util.ext.setHeight
import com.example.core.android.util.ext.setupWithBackNavigation
import com.example.core.android.view.binding.viewBinding
import com.example.core.android.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.spotify.search.R
import com.example.spotify.search.databinding.FragmentSpotifySearchMainBinding
import org.koin.android.ext.android.inject

class SpotifySearchMainFragment : Fragment(R.layout.fragment_spotify_search_main) {

    private val binding: FragmentSpotifySearchMainBinding by viewBinding(FragmentSpotifySearchMainBinding::bind)

    private val query: String by args()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainContentFragment?.disablePlayButton()
        with(binding) {
            val fragmentFactory: IFragmentFactory by inject()
            val spotifyFragmentsFactory: ISpotifyFragmentsFactory by inject()
            val adapter = CustomCurrentStatePagerAdapter(
                childFragmentManager,
                arrayOf(
                    spotifyFragmentsFactory.newSpotifySearchFragment(query),
                    fragmentFactory.newVideosSearchFragment(query)
                )
            )
            searchFragmentViewPager.adapter = adapter
            searchFragmentViewPager.swipeLocked = true
            searchFragmentViewPager.offscreenPageLimit = adapter.count - 1
            searchBottomNavigationView.setOnNavigationItemSelectedListener { item ->
                if (item.itemId == binding.searchBottomNavigationView.selectedItemId) {
                    return@setOnNavigationItemSelectedListener false
                }
                binding.searchFragmentViewPager.currentItem = itemIds.indexOf(item.itemId)
                true
            }
            searchBottomNavigationView.setHeight(requireContext().dpToPx(40f).toInt())
            searchToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
            //TODO: MutableLiveData<String> for query + revert to databinding?
            searchToolbar.title = query
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    companion object {
        private val itemIds: Array<Int> = arrayOf(R.id.action_spotify, R.id.action_videos)

        fun newInstance(query: String) = SpotifySearchMainFragment().apply {
            arguments = Bundle().apply { putString(MvRx.KEY_ARG, query) }
        }
    }
}
