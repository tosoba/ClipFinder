package com.example.there.findclips.main

import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewCompat
import android.util.TypedValue
import android.view.ViewGroup
import com.example.there.findclips.Keys
import com.example.there.findclips.R
import com.example.there.findclips.databinding.ActivityMainBinding
import com.example.there.findclips.databinding.ActivityMainBindingLandImpl
import com.example.there.findclips.entities.Video
import com.example.there.findclips.player.BasePlayerActivity
import com.example.there.findclips.util.*
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BasePlayerActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (item.itemId == main_bottom_navigation_view.selectedItemId) {
            return@OnNavigationItemSelectedListener false
        }

        main_view_pager.currentItem = itemIds.indexOf(item.itemId)

        return@OnNavigationItemSelectedListener true
    }

    private val pagerAdapter by lazy { MainFragmentPagerAdapter(supportFragmentManager) }

    private val itemIds: Array<Int> = arrayOf(R.id.action_dashboard, R.id.action_favorites, R.id.action_search)

    private val onPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            main_bottom_navigation_view?.checkItem(itemIds[position])
        }
    }

    private val view: MainActivityView by lazy {
        MainActivityView(
                state = viewState,
                onNavigationItemSelectedListener = onNavigationItemSelectedListener,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener)
    }

    override fun initView() {
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainActivityView = view
        binding.mainViewPager.offscreenPageLimit = 2

        initYoutubeFragment(binding)
        if (binding is ActivityMainBindingLandImpl) {
            if (viewState.isMaximized.get() == false) minimizeLandscapeFragmentContainer()
            main_player_fragment?.view?.setOnTouchListener(onYoutubeFragmentTouchListener)
        } else {
            initDraggablePanel(binding.mainDraggablePanel!!)
        }
    }

    override fun maximizeLandscapeFragmentContainer() {
        if (viewState.isMaximized.get() == false) {
            main_player_fragment_container?.changeParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            main_player_fragment_container?.changeMarginParams(0, 0, 0, 0)
            viewState.isMaximized.set(true)
            supportActionBar?.hide()
        }
    }

    override fun initYoutubeFragment(binding: ViewDataBinding) {
        youtubePlayerFragment = if (binding is ActivityMainBindingLandImpl) {
            supportFragmentManager.findFragmentById(R.id.main_player_fragment) as? YouTubePlayerSupportFragment
        } else {
            YouTubePlayerSupportFragment()
        }

        youtubePlayerFragment?.initialize(Keys.youtubeApi, onPlayerInitializedListener)
    }

    override fun pause() {
        if (youtubePlayer?.isPlaying == true) {
            showStatusBar()
            supportActionBar?.show()
            youtubePlayer?.pause()
        }
    }

    override fun play() {
        if (youtubePlayer?.isPlaying == false) {
            hideStatusBar()
            view.state.videoIsOpen.set(true)
            main_draggable_panel?.let { ViewCompat.setTranslationZ(it, 0f) }
            youtubePlayer?.play()
        }
    }

    override fun playVideo(video: Video) {
        lastVideo = video
        view.state.videoIsOpen.set(true)
        youtubePlayer?.loadVideo(video.id)
        main_draggable_panel?.maximize()
        main_draggable_panel?.relatedVideosFragment?.videoId = video.id
        hideStatusBar()
        supportActionBar?.hide()
    }

    override fun minimizeLandscapeFragmentContainer() {
        if (viewState.isMaximized.get() == true) {
            main_player_fragment_container?.changeParams(
                    width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics).toInt(),
                    height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 225f, resources.displayMetrics).toInt())
            main_player_fragment_container?.changeMarginParams(0, 0, 20, 20)
            viewState.isMaximized.set(false)
            supportActionBar?.show()
        }
    }

    override fun onBackPressed() {
        if (view.state.videoIsOpen.get() == true && viewState.isMaximized.get() == true) {
            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                minimizeLandscapeFragmentContainer()
            } else {
                main_draggable_panel?.minimize()
                viewState.isMaximized.set(false)
            }
        } else {
            super.onBackPressed()
        }
    }
}
