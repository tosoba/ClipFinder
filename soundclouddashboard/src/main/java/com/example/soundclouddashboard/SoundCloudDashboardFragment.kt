package com.example.soundclouddashboard

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.widget.Toolbar
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.example.coreandroid.base.IFragmentFactory
import com.example.coreandroid.base.fragment.HasMainToolbar
import com.example.coreandroid.headerItem
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.loadingIndicator
import com.example.coreandroid.model.LoadedSuccessfully
import com.example.coreandroid.model.Loading
import com.example.coreandroid.model.LoadingFailed
import com.example.coreandroid.model.soundcloud.clickableListItem
import com.example.coreandroid.reloadControl
import com.example.coreandroid.util.asyncController
import com.example.coreandroid.util.carousel
import com.example.coreandroid.util.ext.*
import com.example.coreandroid.util.withModelsFrom
import com.example.coreandroid.view.epoxy.Column
import com.example.soundclouddashboard.databinding.FragmentSoundCloudDashboardBinding
import kotlinx.android.synthetic.main.fragment_sound_cloud_dashboard.*
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named


class SoundCloudDashboardFragment : BaseMvRxFragment(), HasMainToolbar {

    private val fragmentFactory: IFragmentFactory by inject()

    private val viewModel: SoundCloudDashboardViewModel by fragmentViewModel()

    private lateinit var binding: FragmentSoundCloudDashboardBinding

    private val builder by inject<Handler>(named("builder"))
    private val differ by inject<Handler>(named("differ"))

    override val toolbar: Toolbar
        get() = sound_cloud_dashboard_toolbar

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { withState(viewModel) { !it.playlists.loadingFailed } },
                connectivitySnackbarHost!!.connectivitySnackbarParentView!!,
                viewModel::loadPlaylists
        )
    }

    private val epoxyController by lazy {
        asyncController(builder, differ, viewModel) { state ->
            when (state.playlists.status) {
                is Loading -> {
                    loadingIndicator {
                        id("loading-indicator")
                    }
                }

                is LoadedSuccessfully -> {
                    headerItem {
                        id("playlists-header")
                        text("Playlists")
                    }

                    carousel {
                        id("playlists")
                        withModelsFrom(state.playlists.value.regular.chunked(2)) { chunk ->
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

                    headerItem {
                        id("system-playlists-header")
                        text("System Playlists")
                    }

                    carousel {
                        id("system-playlists")
                        withModelsFrom(state.playlists.value.system.chunked(2)) { chunk ->
                            Column(chunk.map { playlist ->
                                playlist.clickableListItem {
                                    navHostFragment?.showFragment(
                                            fragmentFactory.newSoundCloudPlaylistFragmentWithSystemPlaylist(playlist),
                                            true
                                    )
                                }
                            })
                        }
                    }
                }

                is LoadingFailed<*> -> {
                    reloadControl {
                        id("reload-control")
                        onReloadClicked(View.OnClickListener { viewModel.loadPlaylists() })
                        message("Error occurred lmao") //TODO: better error msg
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun invalidate() {
        withState(viewModel) { state -> epoxyController.setData(state) }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = FragmentSoundCloudDashboardBinding.inflate(inflater, container, false)
            .apply {
                appCompatActivity?.setSupportActionBar(soundCloudDashboardToolbar)
                appCompatActivity?.showDrawerHamburger()
                soundCloudDashboardRecyclerView.apply {
                    setController(epoxyController)
                    //TODO: animation
                }
                mainContentFragment?.disablePlayButton()
                binding = this
            }.root


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        if (toolbar.menu?.size() == 0) appCompatActivity?.setSupportActionBar(toolbar)
    }

    override fun onOptionsItemSelected(
            item: MenuItem?
    ): Boolean = if (item?.itemId == android.R.id.home
            && parentFragment?.childFragmentManager?.backStackEntryCount == 0) {
        navigationDrawerController?.openDrawer()
        true
    } else false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }
}
