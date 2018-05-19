package com.example.there.findclips.activities.main

import android.content.Intent
import android.content.res.Configuration
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.ViewGroup
import com.example.there.findclips.Keys
import com.example.there.findclips.R
import com.example.there.findclips.view.player.PlayerView
import com.example.there.findclips.view.player.PlayerViewState
import com.example.there.findclips.databinding.ActivityMainBinding
import com.example.there.findclips.databinding.ActivityMainBindingLandImpl
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.util.*
import com.example.there.findclips.view.OnPageChangeListener
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), PlayerView.OnVideoSelectedListener {

    override fun onVideoSelected(video: Video) = playerView.playVideo(video)

    private val playerView: PlayerView by lazy {
        object : PlayerView(this@MainActivity) {
            override val state: PlayerViewState = PlayerViewState()

            override fun initView() {
                val binding: ActivityMainBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
                binding.mainActivityView = view
                binding.mainViewPager.offscreenPageLimit = 2

                initYoutubeFragment(binding)
                if (binding is ActivityMainBindingLandImpl) {
                    if (state.isMaximized.get() == false) minimizeLandscapeFragmentContainer()
                    main_player_fragment?.view?.setOnTouchListener(onYoutubeFragmentTouchListener)
                } else {
                    initDraggablePanel(binding.mainDraggablePanel!!, this@MainActivity.supportFragmentManager)
                }
            }

            override fun maximizeLandscapeFragmentContainer() {
                if (state.isMaximized.get() == false) {
                    main_player_fragment_container?.changeParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                    main_player_fragment_container?.changeMarginParams(0, 0, 0, 0)
                    state.isMaximized.set(true)
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
                if (state.isMaximized.get() == true) {
                    main_player_fragment_container?.changeParams(
                            width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300f, resources.displayMetrics).toInt(),
                            height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 225f, resources.displayMetrics).toInt())
                    main_player_fragment_container?.changeMarginParams(0, 0, 20, 20)
                    state.isMaximized.set(false)
                    supportActionBar?.show()
                }
            }

            override fun closeVideo() {
                super.closeVideo()
                showStatusBar()
                supportActionBar?.show()
            }
        }
    }

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


    private val view: MainView by lazy {
        MainView(state = playerView.state,
                onNavigationItemSelectedListener = onNavigationItemSelectedListener,
                pagerAdapter = pagerAdapter,
                onPageChangeListener = onPageChangeListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerView.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        playerView.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        playerView.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        playerView.onActivityResult(requestCode)
    }

    override fun onBackPressed() {
        if (view.state.videoIsOpen.get() == true && playerView.state.isMaximized.get() == true) {
            if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                playerView.minimizeLandscapeFragmentContainer()
            } else {
                main_draggable_panel?.minimize()
                playerView.state.isMaximized.set(false)
            }
        } else {
            super.onBackPressed()
        }
    }
}
