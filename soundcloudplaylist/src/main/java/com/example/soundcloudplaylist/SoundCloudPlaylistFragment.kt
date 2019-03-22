package com.example.soundcloudplaylist

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.coreandroid.lifecycle.ConnectivityComponent
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.model.soundcloud.ISoundCloudPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudPlaylist
import com.example.coreandroid.model.soundcloud.SoundCloudSystemPlaylist
import com.example.coreandroid.util.ext.*
import com.example.itemlist.soundcloud.SoundCloudTracksFragment
import com.example.soundcloudplaylist.databinding.FragmentSoundCloudPlaylistBinding
import com.squareup.picasso.Picasso


class SoundCloudPlaylistFragment : com.example.coreandroid.base.fragment.BaseVMFragment<SoundCloudPlaylistViewModel>(SoundCloudPlaylistViewModel::class.java) {

    private val tracksFragment: SoundCloudTracksFragment
        get() = childFragmentManager.findFragmentById(R.id.sound_cloud_playlist_tracks_fragment) as SoundCloudTracksFragment

    private val connectivityComponent: ConnectivityComponent by lazy {
        ConnectivityComponent(
                activity!!,
                { viewModel.tracks.value != null },
                connectivitySnackbarHost!!.connectivitySnackbarParentView!!,
                ::loadData,
                true
        )
    }

    private val playlist: ISoundCloudPlaylist by lazy {
        if (arguments!!.containsKey(ARG_PLAYLIST)) {
            arguments!!.getParcelable<ISoundCloudPlaylist>(ARG_PLAYLIST)
        } else {
            arguments!!.getParcelable(ARG_SYSTEM_PLAYLIST)
        }
    }

    private val view: SoundCloudPlaylistView by lazy {
        SoundCloudPlaylistView(
                state = viewModel.viewState,
                playlist = playlist,
                onFavouriteBtnClickListener = View.OnClickListener {},
                onPlayBtnClickListener = View.OnClickListener {}
        )
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        lifecycle.addObserver(disposablesComponent)
        loadData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentSoundCloudPlaylistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sound_cloud_playlist, container, false)
        return binding.apply {
            view = this@SoundCloudPlaylistFragment.view
            playlist.artworkUrl?.let { url ->
                disposablesComponent.add(Picasso.with(context).getBitmapSingle(url, { bitmap ->
                    bitmap.generateColorGradient {
                        soundCloudPlaylistToolbarGradientBackgroundView.background = it
                        soundCloudPlaylistToolbarGradientBackgroundView.invalidate()
                    }
                }))
            }
            soundCloudPlaylistToolbar.setupWithBackNavigation(appCompatActivity)
        }.root
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lifecycle.addObserver(connectivityComponent)
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.tracks.observe(this, Observer { tracks ->
            tracks?.let { tracksFragment.updateItems(it, false) }
        })
    }

    private fun loadData() {
        arguments?.let {
            if (it.containsKey(ARG_PLAYLIST)) {
                viewModel.loadTracksFromPlaylist(it.getParcelable<SoundCloudPlaylist>(ARG_PLAYLIST)!!.id.toString())
            } else if (it.containsKey(ARG_SYSTEM_PLAYLIST)) {
                viewModel.loadTracksWithIds(it.getParcelable<SoundCloudSystemPlaylist>(ARG_SYSTEM_PLAYLIST)!!.trackIds.map(Int::toString))
            }
        }
    }

    companion object {
        private const val ARG_PLAYLIST = "ARG_PLAYLIST"
        private const val ARG_SYSTEM_PLAYLIST = "ARG_SYSTEM_PLAYLIST"

        fun newInstance(systemPlaylist: SoundCloudSystemPlaylist) = SoundCloudPlaylistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_SYSTEM_PLAYLIST, systemPlaylist)
            }
        }

        fun newInstance(playlist: SoundCloudPlaylist) = SoundCloudPlaylistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PLAYLIST, playlist)
            }
        }
    }
}
