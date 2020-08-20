package com.example.soundclouddashboard.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.core.ext.castAs
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.fragment.HasMainToolbar
import com.example.core.android.base.handler.NavigationDrawerController
import com.example.core.android.headerItem
import com.example.core.android.lifecycle.ConnectivityComponent
import com.example.core.android.loadingIndicator
import com.example.core.android.model.LoadedSuccessfully
import com.example.core.android.model.Loading
import com.example.core.android.model.LoadingFailed
import com.example.core.android.model.soundcloud.clickableListItem
import com.example.core.android.reloadControl
import com.example.core.android.view.epoxy.carousel
import com.example.core.android.util.ext.mainContentFragment
import com.example.core.android.util.ext.navHostFragment
import com.example.core.android.util.ext.reloadingConnectivityComponent
import com.example.core.android.util.ext.showDrawerHamburger
import com.example.core.android.view.epoxy.injectedTypedController
import com.example.core.android.view.epoxy.withModelsFrom
import com.example.core.android.view.epoxy.Column
import com.example.soundclouddashboard.databinding.FragmentSoundCloudDashboardBinding
import kotlinx.android.synthetic.main.fragment_sound_cloud_dashboard.*
import org.koin.android.ext.android.inject

class SoundCloudDashboardFragment : BaseMvRxFragment(), HasMainToolbar {

    private val fragmentFactory: IFragmentFactory by inject()

    private val viewModel: SoundCloudDashboardViewModel by fragmentViewModel()

    private lateinit var binding: FragmentSoundCloudDashboardBinding
    override val toolbar: Toolbar get() = binding.soundCloudDashboardToolbar

    private val epoxyController by lazy(LazyThreadSafetyMode.NONE) {
        injectedTypedController<SoundCloudDashboardState> { (selections) ->
            when (selections.status) {
                is Loading -> loadingIndicator {
                    id("loading-indicator")
                }

                is LoadedSuccessfully -> selections.value.forEach { selection ->
                    headerItem {
                        id("${selection.id}-header")
                        text(selection.title)
                    }

                    carousel {
                        id("${selection.id}-playlists")
                        withModelsFrom(selection.playlists.chunked(2)) { chunk ->
                            Column(chunk.map { playlist ->
                                playlist.clickableListItem {
                                    navHostFragment?.showFragment(
                                        fragmentFactory.newSoundCloudPlaylistFragmentWithPlaylist(playlist),
                                        true
                                    )
                                }
                            })
                        }
                    }
                }

                is LoadingFailed<*> -> reloadControl {
                    id("reload-control")
                    onReloadClicked(View.OnClickListener { viewModel.loadSelections() })
                    message("Error occurred lmao") //TODO: better error msg
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentSoundCloudDashboardBinding.inflate(inflater, container, false)
        .apply {
            activity?.castAs<AppCompatActivity>()?.apply {
                setSupportActionBar(soundCloudDashboardToolbar)
                showDrawerHamburger()
            }
            soundCloudDashboardRecyclerView.setController(epoxyController)
            mainContentFragment?.disablePlayButton()
            binding = this
        }
        .root

    override fun invalidate() = withState(viewModel, epoxyController::setData)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (toolbar.menu?.size() == 0) {
            requireActivity().castAs<AppCompatActivity>()?.setSupportActionBar(toolbar)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = if (
        item.itemId == android.R.id.home
        && parentFragment?.childFragmentManager?.backStackEntryCount == 0
    ) {
        activity?.castAs<NavigationDrawerController>()?.openDrawer()
        true
    } else false
}
