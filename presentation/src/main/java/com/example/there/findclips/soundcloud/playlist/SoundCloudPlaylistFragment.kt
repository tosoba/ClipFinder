package com.example.there.findclips.soundcloud.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.there.findclips.R
import com.example.there.findclips.base.fragment.BaseVMFragment
import com.example.there.findclips.model.entity.soundcloud.SoundCloudPlaylist
import com.example.there.findclips.model.entity.soundcloud.SoundCloudSystemPlaylist


class SoundCloudPlaylistFragment : BaseVMFragment<SoundCloudPlaylistViewModel>(SoundCloudPlaylistViewModel::class.java) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_PLAYLIST)) {
                viewModel.loadTracksFromPlaylist(it.getParcelable<SoundCloudPlaylist>(ARG_PLAYLIST)!!.id.toString())
            } else if (it.containsKey(ARG_SYSTEM_PLAYLIST)) {
                viewModel.loadTracksWithIds(it.getParcelable<SoundCloudSystemPlaylist>(ARG_SYSTEM_PLAYLIST)!!.trackIds.map(Int::toString))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sound_cloud_playlist, container, false)
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
