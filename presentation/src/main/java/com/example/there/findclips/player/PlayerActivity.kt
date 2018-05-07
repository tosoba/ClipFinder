package com.example.there.findclips.player

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.there.findclips.Keys
import com.example.there.findclips.R
import com.example.there.findclips.entities.Video
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private val video: Video by lazy { intent.getParcelableExtra<Video>(EXTRA_VIDEO) }

    private var player: YouTubePlayer? = null
    private var lastSeekTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        savedInstanceState?.let { initFromSavedState(it) }

        player_view?.initialize(Keys.youtubeApi, this)
    }

    private fun initFromSavedState(savedInstanceState: Bundle) {
        lastSeekTime = savedInstanceState.getInt(KEY_SAVED_STATE_SEEKTIME)
    }

    override fun onPause() {
        super.onPause()
        lastSeekTime = player?.currentTimeMillis ?: 0
        player = null
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(KEY_SAVED_STATE_SEEKTIME, lastSeekTime)
    }

    override fun onInitializationSuccess(provider: YouTubePlayer.Provider?, player: YouTubePlayer?, wasRestored: Boolean) {
        this.player = player
        player?.loadVideo(video.id, lastSeekTime)
    }

    override fun onInitializationFailure(provider: YouTubePlayer.Provider?, errorReason: YouTubeInitializationResult?) {
        errorReason?.let {
            if (it.isUserRecoverableError) {
                it.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show()
            } else {
                val errorMessage = String.format(getString(R.string.error_player), it.toString())
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            player_view?.initialize(Keys.youtubeApi, this)
        }
    }

    companion object {
        private const val EXTRA_VIDEO = "EXTRA_VIDEO"

        private const val KEY_SAVED_STATE_SEEKTIME = "KEY_SAVED_STATE_SEEKTIME"

        private const val RECOVERY_DIALOG_REQUEST = 1

        fun start(activity: Activity, video: Video) {
            val intent = Intent(activity, PlayerActivity::class.java).apply {
                putExtra(EXTRA_VIDEO, video)
            }
            activity.startActivity(intent)
        }
    }
}
