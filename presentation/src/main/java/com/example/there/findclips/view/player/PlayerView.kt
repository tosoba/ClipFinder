package com.example.there.findclips.view.player

import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.there.findclips.Keys
import com.example.there.findclips.R
import com.example.there.findclips.fragments.relatedvideos.RelatedVideosFragment
import com.example.there.findclips.model.entities.Video
import com.example.there.findclips.util.getDimenFloat
import com.example.there.findclips.util.relatedVideosFragment
import com.example.there.findclips.view.OnSwipeTouchListener
import com.example.there.findclips.view.draggable.DraggableListener
import com.example.there.findclips.view.draggable.DraggablePanel
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment


abstract class PlayerView(activity: AppCompatActivity) {

    protected var lastVideo: Video? = null
    private var lastSeekTime = 0

    abstract fun playVideo(video: Video)

    protected var youtubePlayer: YouTubePlayer? = null
    protected var youtubePlayerFragment: YouTubePlayerSupportFragment? = null

    abstract fun initYoutubeFragment(binding: ViewDataBinding)

    protected val onYoutubeFragmentTouchListener: OnSwipeTouchListener by lazy {
        object : OnSwipeTouchListener(activity) {
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

    fun onCreate(savedInstanceState: Bundle?) {
        initFromSavedState(savedInstanceState)
        initView()
    }

    abstract fun initView()

    abstract val state: PlayerViewState

    private fun initFromSavedState(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            state.videoIsOpen.set(it.getBoolean(KEY_SAVED_VIDEO_IS_OPEN))
            lastVideo = it.getParcelable(KEY_SAVED_LAST_VIDEO)
            lastSeekTime = it.getInt(KEY_SAVED_LAST_SEEK_TIME)
        }
    }

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(KEY_SAVED_VIDEO_IS_OPEN, state.videoIsOpen.get() ?: false)
        outState?.putParcelable(KEY_SAVED_LAST_VIDEO, lastVideo)
        outState?.putInt(KEY_SAVED_LAST_SEEK_TIME, lastSeekTime)
    }

    fun onPause() {
        if (state.videoIsOpen.get() == true) lastSeekTime = youtubePlayer?.currentTimeMillis ?: 0
        youtubePlayer = null
        draggablePanel = null
    }

    private val draggableListener = object : DraggableListener {
        override fun onMaximized() {
            play()
            state.isMaximized.set(true)
            activity.supportActionBar?.hide()
        }

        override fun onMinimized() {
            state.isMaximized.set(false)
        }

        override fun onClosedToLeft() = closeVideo()

        override fun onClosedToRight() = closeVideo()
    }

    private var draggablePanel: DraggablePanel? = null

    protected fun initDraggablePanel(draggablePanel: DraggablePanel, supportFragmentManager: FragmentManager) = with(draggablePanel) {
        this@PlayerView.draggablePanel = this

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

            if (state.videoIsOpen.get() == true && lastVideo != null) {
                youtubePlayer?.loadVideo(lastVideo!!.id, lastSeekTime)
                activity.supportActionBar?.hide()
                play()
                draggablePanel?.relatedVideosFragment?.videoId = lastVideo!!.id
            }
        }

        override fun onInitializationFailure(provider: YouTubePlayer.Provider, error: YouTubeInitializationResult) {
            if (error.isUserRecoverableError) {
                error.getErrorDialog(activity, RECOVERY_DIALOG_REQUEST).show()
            } else {
                val errorMessage = String.format(activity.getString(R.string.error_player), error.toString())
                Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    abstract fun maximizeLandscapeFragmentContainer()
    abstract fun minimizeLandscapeFragmentContainer()

    abstract fun play()
    abstract fun pause()

    open fun closeVideo() {
        pause()
        state.videoIsOpen.set(false)
        lastVideo = null
        maximizeLandscapeFragmentContainer()
    }

    fun onActivityResult(requestCode: Int) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            youtubePlayerFragment?.initialize(Keys.youtubeApi, onPlayerInitializedListener)
        }
    }

    interface OnVideoSelectedListener {
        fun onVideoSelected(video: Video)
    }

    companion object {
        private const val KEY_SAVED_VIDEO_IS_OPEN = "KEY_SAVED_VIDEO_IS_OPEN"
        private const val KEY_SAVED_LAST_VIDEO = "KEY_SAVED_LAST_VIDEO"
        private const val KEY_SAVED_LAST_SEEK_TIME = "KEY_SAVED_LAST_SEEK_TIME"

        private const val RECOVERY_DIALOG_REQUEST = 1
    }
}