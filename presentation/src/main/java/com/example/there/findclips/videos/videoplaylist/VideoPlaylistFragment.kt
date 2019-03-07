package com.example.there.findclips.videos.videoplaylist

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.there.findclips.R
import com.example.there.findclips.databinding.FragmentVideoPlaylistBinding
import com.example.there.findclips.model.entity.VideoPlaylist
import com.example.there.findclips.util.ext.appCompatActivity
import com.example.there.findclips.util.ext.setupWithBackNavigation
import com.example.there.findclips.util.ext.youtubePlayerController
import com.example.there.findclips.videos.search.VideosSearchFragment
import com.example.there.findclips.view.viewflipper.PlaylistThumbnailFlipperAdapter


class VideoPlaylistFragment : Fragment() {

    private val playlist: VideoPlaylist by lazy {
        arguments!!.getParcelable<VideoPlaylist>(ARG_PLAYLIST)
    }

    private val thumbnailUrls: Array<String> by lazy {
        arguments!!.getStringArray(ARG_THUMBNAIL_URLS)
    }

    private val videoFragment: VideosSearchFragment?
        get() = childFragmentManager.findFragmentById(R.id.videos_fragment_container_layout) as? VideosSearchFragment

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentVideoPlaylistBinding>(
            inflater,
            R.layout.fragment_video_playlist,
            container,
            false
    ).apply {
        view = VideoPlaylistView(
                VideoPlaylistViewState(playlist),
                PlaylistThumbnailFlipperAdapter(thumbnailUrls.toList()),
                View.OnClickListener { _ ->
                    videoFragment?.let {
                        if (it.videosLoaded) youtubePlayerController?.loadVideoPlaylist(playlist, it.videos)
                        else Toast.makeText(context, "Videos not loaded yet.", Toast.LENGTH_SHORT).show()
                    }
                }
        )
        videoPlaylistToolbar.setupWithBackNavigation(appCompatActivity)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showVideosFragment()
    }

    private fun showVideosFragment() = childFragmentManager.beginTransaction()
            .replace(R.id.videos_fragment_container_layout, VideosSearchFragment.newInstanceWithVideoPlaylist(playlist))
            .commit()

    companion object {
        private const val ARG_PLAYLIST = "ARG_PLAYLIST"
        private const val ARG_THUMBNAIL_URLS = "ARG_THUMBNAIL_URLS"

        fun newInstance(
                playlist: VideoPlaylist,
                thumbnailUrls: Array<String>
        ) = VideoPlaylistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PLAYLIST, playlist)
                putStringArray(ARG_THUMBNAIL_URLS, thumbnailUrls)
            }
        }
    }
}
