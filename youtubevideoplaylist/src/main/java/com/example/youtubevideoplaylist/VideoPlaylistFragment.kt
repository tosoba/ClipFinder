package com.example.youtubevideoplaylist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.core.ext.castAs
import com.example.core.android.base.IFragmentFactory
import com.example.core.android.base.fragment.IYoutubeSearchFragment
import com.example.core.android.base.handler.YoutubePlayerController
import com.example.core.android.model.videos.VideoPlaylist
import com.example.core.android.util.ext.setupWithBackNavigation
import com.example.core.android.view.viewflipper.PlaylistThumbnailFlipperAdapter
import com.example.youtubevideoplaylist.databinding.FragmentVideoPlaylistBinding
import org.koin.android.ext.android.inject


class VideoPlaylistFragment : Fragment() {

    private val fragmentFactory: IFragmentFactory by inject()

    private val playlist: VideoPlaylist by lazy {
        arguments!!.getParcelable<VideoPlaylist>(ARG_PLAYLIST)
    }

    private val thumbnailUrls: Array<String> by lazy {
        arguments!!.getStringArray(ARG_THUMBNAIL_URLS)
    }

    private val videoFragment: IYoutubeSearchFragment?
        get() = childFragmentManager.findFragmentById(R.id.videos_fragment_container_layout) as? IYoutubeSearchFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = DataBindingUtil.inflate<FragmentVideoPlaylistBinding>(
        inflater, R.layout.fragment_video_playlist, container, false
    ).apply {
        view = VideoPlaylistView(
            VideoPlaylistViewState(playlist),
            PlaylistThumbnailFlipperAdapter(thumbnailUrls.toList()),
            View.OnClickListener { _ ->
                videoFragment?.let {
                    if (it.videosLoaded) activity?.castAs<YoutubePlayerController>()
                        ?.loadVideoPlaylist(playlist, it.videos)
                    else Toast.makeText(context, "Videos not loaded yet.", Toast.LENGTH_SHORT).show()
                }
            }
        )
        videoPlaylistToolbar.setupWithBackNavigation(requireActivity() as? AppCompatActivity)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showVideosFragment()
    }

    private fun showVideosFragment() = childFragmentManager.beginTransaction()
        .replace(R.id.videos_fragment_container_layout, fragmentFactory.newVideosSearchFragment(playlist))
        .commit()

    companion object {
        private const val ARG_PLAYLIST = "ARG_PLAYLIST"
        private const val ARG_THUMBNAIL_URLS = "ARG_THUMBNAIL_URLS"

        fun newInstance(
            playlist: VideoPlaylist, thumbnailUrls: Array<String>
        ) = VideoPlaylistFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PLAYLIST, playlist)
                putStringArray(ARG_THUMBNAIL_URLS, thumbnailUrls)
            }
        }
    }
}
