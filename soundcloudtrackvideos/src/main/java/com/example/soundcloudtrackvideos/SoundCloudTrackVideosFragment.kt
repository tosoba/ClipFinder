package com.example.soundcloudtrackvideos

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coreandroid.base.fragment.BaseVMFragment
import com.example.coreandroid.di.Injectable
import com.example.coreandroid.lifecycle.DisposablesComponent
import com.example.coreandroid.lifecycle.OnPropertyChangedCallbackComponent
import com.example.coreandroid.model.soundcloud.SoundCloudTrack
import com.example.coreandroid.util.ext.appCompatActivity
import com.example.coreandroid.util.ext.generateColorGradient
import com.example.coreandroid.util.ext.getBitmapSingle
import com.example.coreandroid.util.ext.hideAndShow
import com.example.coreandroid.view.OnPageChangeListener
import com.example.coreandroid.view.OnTabSelectedListener
import com.example.coreandroid.view.viewpager.adapter.CustomCurrentStatePagerAdapter
import com.example.youtubesearch.VideosSearchFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_sound_cloud_track_videos.*


class SoundCloudTrackVideosFragment : BaseVMFragment<SoundCloudTrackVideosViewModel>(SoundCloudTrackVideosViewModel::class.java), Injectable {

    private val argTrack: SoundCloudTrack by lazy { arguments!!.getParcelable<SoundCloudTrack>(ARG_TRACK) }

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            sound_cloud_track_videos_tab_layout?.getTabAt(position)?.select()
//            updateCurrentFragment(viewModel.viewState.track.get()!!)
        }
    }

    private val onTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let { sound_cloud_track_videos_viewpager?.currentItem = it.position }
        }
    }

    private val pagerAdapter: CustomCurrentStatePagerAdapter by lazy {
        CustomCurrentStatePagerAdapter(
                fragmentManager = childFragmentManager,
                fragments = arrayOf(
                        VideosSearchFragment.newInstanceWithQuery(argTrack.title)
                //TODO: related soundcloud tracks fragment
                )
        )
    }

    private val view: SoundCloudTrackVideosView by lazy {
        SoundCloudTrackVideosView(
                state = viewModel.viewState,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener,
                onTabSelectedListener = onTabSelectedListener,
                onFavouriteBtnClickListener = View.OnClickListener {  },
                onPlayBtnClickListener = View.OnClickListener {  }
        )
    }

    private val disposablesComponent = DisposablesComponent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: com.example.soundcloudtrackvideos.databinding.FragmentSoundCloudTrackVideosBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_sound_cloud_track_videos, container, false)
        lifecycle.addObserver(OnPropertyChangedCallbackComponent(viewModel.viewState.isSavedAsFavourite) { _, _ ->
            binding.soundCloudTrackFavouriteFab.hideAndShow()
        })
        return binding.apply {
            view = this@SoundCloudTrackVideosFragment.view
            argTrack.artworkUrl?.let { loadCollapsingToolbarBackgroundGradient(it) }
            soundCloudTrackVideosViewpager.offscreenPageLimit = 1
//            soundCloudTrackVideosToolbar.setupWithBackNavigation(appCompatActivity, ::onBackPressed)
        }.root
    }

    private fun loadCollapsingToolbarBackgroundGradient(
            url: String
    ) = disposablesComponent.add(Picasso.with(context).getBitmapSingle(url, { bitmap ->
        bitmap.generateColorGradient {
            sound_cloud_track_videos_toolbar_gradient_background_view?.background = it
            sound_cloud_track_videos_toolbar_gradient_background_view?.invalidate()
        }
    }))

    companion object {
        private const val ARG_TRACK = "ARG_TRACK"

        fun newInstance(track: SoundCloudTrack) = SoundCloudTrackVideosFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_TRACK, track)
            }
        }
    }
}
