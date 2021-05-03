package com.clipfinder.spotify.search

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.mvrx.args
import com.clipfinder.core.android.base.IFragmentFactory
import com.clipfinder.core.android.spotify.navigation.ISpotifyFragmentsFactory
import com.clipfinder.core.android.util.ext.*
import com.clipfinder.core.android.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.clipfinder.spotify.search.databinding.FragmentSpotifySearchMainBinding
import org.koin.android.ext.android.get

class SpotifySearchMainFragment : Fragment(R.layout.fragment_spotify_search_main) {
    private val binding: FragmentSpotifySearchMainBinding by viewBinding(
        FragmentSpotifySearchMainBinding::bind
    )
    private val query: String by args()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mainContentFragment?.disablePlayButton()
        with(binding) {
            val adapter =
                CustomCurrentStatePagerAdapter(
                    childFragmentManager,
                    arrayOf(
                        get<ISpotifyFragmentsFactory>().newSpotifySearchFragment(query),
                        get<IFragmentFactory>().newVideosSearchFragment(query)
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
            // TODO: MutableLiveData<String> for query + revert to databinding?
            searchToolbar.title = query
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = false

    companion object {
        private val itemIds: Array<Int> = arrayOf(R.id.action_spotify, R.id.action_videos)

        fun newInstance(query: String): SpotifySearchMainFragment = newMvRxFragmentWith(query)
    }
}
