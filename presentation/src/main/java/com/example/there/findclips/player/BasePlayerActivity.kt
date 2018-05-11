package com.example.there.findclips.player

import android.content.Intent
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.there.findclips.Keys
import com.example.there.findclips.R
import com.example.there.findclips.draggablefragment.DraggableListener
import com.example.there.findclips.draggablefragment.DraggablePanel
import com.example.there.findclips.entities.Video
import com.example.there.findclips.relatedvideos.RelatedVideosFragment
import com.example.there.findclips.trackdetails.TrackDetailsFragment
import com.example.there.findclips.util.OnSwipeTouchListener
import com.example.there.findclips.util.getDimenFloat
import com.example.there.findclips.util.relatedVideosFragment
import com.example.there.findclips.util.showStatusBar
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment


abstract class BasePlayerActivity : AppCompatActivity() {

    protected var lastVideo: Video? = null
    private var lastSeekTime = 0

    abstract fun playVideo(video: Video)

    protected var youtubePlayer: YouTubePlayer? = null
    protected var youtubePlayerFragment: YouTubePlayerSupportFragment? = null

    abstract fun initYoutubeFragment(binding: ViewDataBinding)

    protected val onYoutubeFragmentTouchListener: OnSwipeTouchListener by lazy {
        object : OnSwipeTouchListener(this@BasePlayerActivity) {
            override fun onSingleTap() {
                if (youtubePlayer?.isPlaying == true) youtubePlayer?.pause()
                else youtubePlayer?.play()
            }

            override fun onSwipeRight() = closeVideo()
            override fun onSwipeLeft() = closeVideo()
            override fun onSwipeTop() = maximizeLandscapeFragmentContainer()
            override fun onSwipeBottom() = minimizeLandscapeFragmentContainer()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFromSavedState(savedInstanceState)
        initView()
    }

    abstract fun initView()

    protected val viewState: PlayerViewState = PlayerViewState()

    private fun initFromSavedState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            viewState.videoIsOpen.set(it.getBoolean(KEY_SAVED_VIDEO_IS_OPEN))
            lastVideo = it.getParcelable(KEY_SAVED_LAST_VIDEO)
            lastSeekTime = it.getInt(KEY_SAVED_LAST_SEEK_TIME)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(KEY_SAVED_VIDEO_IS_OPEN, viewState.videoIsOpen.get() ?: false)
        outState?.putParcelable(KEY_SAVED_LAST_VIDEO, lastVideo)
        outState?.putInt(KEY_SAVED_LAST_SEEK_TIME, lastSeekTime)
    }

    override fun onPause() {
        super.onPause()
        if (viewState.videoIsOpen.get() == true) lastSeekTime = youtubePlayer?.currentTimeMillis ?: 0
        youtubePlayer = null
        draggablePanel = null
    }

    private val draggableListener = object : DraggableListener {
        override fun onMaximized() {
            play()
            viewState.isMaximized.set(true)
            supportActionBar?.hide()
        }

        override fun onMinimized() {
            viewState.isMaximized.set(false)
        }

        override fun onClosedToLeft() = closeVideo()

        override fun onClosedToRight() = closeVideo()
    }

    private var draggablePanel: DraggablePanel? = null

    protected fun initDraggablePanel(draggablePanel: DraggablePanel) = with(draggablePanel) {
        this@BasePlayerActivity.draggablePanel = this

        setFragmentManager(supportFragmentManager)
        setTopFragment(youtubePlayerFragment)
        val bottomFragment = RelatedVideosFragment()
        setBottomFragment(bottomFragment)

        setDraggableListener(draggableListener)
        isClickToMaximizeEnabled = true

        setXScaleFactor(resources.getDimenFloat(R.dimen.x_scale_factor))
        setYScaleFactor(resources.getDimenFloat(R.dimen.y_scale_factor))

        setTopViewHeight(resources.getDimensionPixelSize(R.dimen.video_player_fragment_height))
        setTopFragmentMarginRight(resources.getDimensionPixelSize(R.dimen.top_fragment_margin))
        setTopFragmentMarginBottom(resources.getDimensionPixelSize(R.dimen.top_fragment_margin))

        initializeView()
    }

    protected val onPlayerInitializedListener = object : YouTubePlayer.OnInitializedListener {
        override fun onInitializationSuccess(provider: YouTubePlayer.Provider, player: YouTubePlayer, wasRestored: Boolean) {
            youtubePlayer = player
            youtubePlayer?.setShowFullscreenButton(false)

            if (viewState.videoIsOpen.get() == true && lastVideo != null) {
                youtubePlayer?.loadVideo(lastVideo!!.id, lastSeekTime)
                supportActionBar?.hide()
                play()
                draggablePanel?.relatedVideosFragment?.videoId = lastVideo!!.id
            }
        }

        override fun onInitializationFailure(provider: YouTubePlayer.Provider, error: YouTubeInitializationResult) {
            if (error.isUserRecoverableError) {
                error.getErrorDialog(this@BasePlayerActivity, RECOVERY_DIALOG_REQUEST).show()
            } else {
                val errorMessage = String.format(getString(R.string.error_player), error.toString())
                Toast.makeText(this@BasePlayerActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    abstract fun maximizeLandscapeFragmentContainer()
    abstract fun minimizeLandscapeFragmentContainer()

    abstract fun play()
    abstract fun pause()

    private fun closeVideo() {
        pause()
        viewState.videoIsOpen.set(false)
        lastVideo = null
        maximizeLandscapeFragmentContainer()
        showStatusBar()
        supportActionBar?.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            youtubePlayerFragment?.initialize(Keys.youtubeApi, onPlayerInitializedListener)
        }
    }

    companion object {
        private const val KEY_SAVED_VIDEO_IS_OPEN = "KEY_SAVED_VIDEO_IS_OPEN"
        private const val KEY_SAVED_LAST_VIDEO = "KEY_SAVED_LAST_VIDEO"
        private const val KEY_SAVED_LAST_SEEK_TIME = "KEY_SAVED_LAST_SEEK_TIME"

        private const val RECOVERY_DIALOG_REQUEST = 1
    }
}