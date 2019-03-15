package com.example.there.findclips.soundcloud.trackvideos

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.there.findclips.R
import com.example.there.findclips.model.entity.soundcloud.SoundCloudTrack


class SoundCloudTrackVideosFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sound_cloud_track_videos, container, false)
    }

    companion object {
        private const val ARG_TRACK = "ARG_TRACK"

        fun newInstance(track: SoundCloudTrack) = SoundCloudTrackVideosFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TRACK, track)
            }
        }
    }
}
